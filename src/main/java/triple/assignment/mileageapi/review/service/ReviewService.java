package triple.assignment.mileageapi.review.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import triple.assignment.mileageapi.place.domain.Place;
import triple.assignment.mileageapi.place.service.PlaceService;
import triple.assignment.mileageapi.point.service.PointService;
import triple.assignment.mileageapi.review.domain.Review;
import triple.assignment.mileageapi.review.domain.ReviewRepository;
import triple.assignment.mileageapi.user.domain.User;
import triple.assignment.mileageapi.user.service.UserService;


@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewService {
    private final PlaceService placeService;
    private final UserService userService;
    private final PointService pointService;
    private final ReviewRepository reviewRepository;


    @Transactional
    public Review create(Review review) {
        final Place place = placeService.getPlaceWithUUID(review.getPlaceId()).orElseThrow(); // TODO Exception
        final User user = userService.getUserWithUUID(review.getUserId()).orElseThrow(); // TODO Exception

        checkAlreadyWritten(user, place);

        pointService.savePointHistory(user, review.getReviewId(), getSavePoint(review, place));

        return reviewRepository.save(  review.setUser(user).setPlace(place)  );
    }


    @Transactional
    public Review patch(Review review) { // TODO point baseTimeEntity
        final Review findReview = reviewRepository.findByReviewId(review.getReviewId())
                .orElseThrow();

        findReview.clearAllPhotos();
        findReview.addPhotos(  review.getPhotos()  );

        pointService.savePointHistory(findReview.getUser(), findReview.getReviewId(), getChangePoint(review, findReview));

        return findReview.changeContent(review.getContent());
    }


    @Transactional
    public Review delete(Review review) {
        final Review findReview = reviewRepository.findByReviewId(review.getReviewId()).orElseThrow();

        pointService.savePointHistory(  findReview.getUser(), review.getReviewId(), getReleasePoint(findReview)  );
        reviewRepository.delete(findReview);

        return findReview;
    }


    private void checkAlreadyWritten(User user, Place place) {
        if (reviewRepository.existsByUserAndPlace(user, place)) {
            throw new RuntimeException(); // TODO
        }
    }


    private int getChangePoint(Review after, Review before) {
        return (after.getContentPoint() + after.getPhotoPoint()) - (before.getContentPoint() + before.getPhotoPoint());
    }

    private int getSavePoint(Review review, Place place) {
        return review.getPhotoPoint() + review.getContentPoint() + place.getFirstReviewPoint() ;
    }

    private int getReleasePoint(Review review) {
        return -1 * (review.getPhotoPoint() + review.getContentPoint() + getFirstReviewPoint(review));
    }


    private int getFirstReviewPoint(Review review) { // TODO
        final Review firstReview = reviewRepository.findTopByPlaceOrderByCreatedAtDesc(review.getPlace())
                .orElseThrow();
        return review.getReviewId().equals(firstReview.getReviewId()) ? 1 : 0;
    }

}
