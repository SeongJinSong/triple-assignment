package triple.assignment.mileageapi.review.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import triple.assignment.mileageapi.global.error.exception.DuplicateReviewException;
import triple.assignment.mileageapi.global.error.exception.PlaceNotFoundException;
import triple.assignment.mileageapi.global.error.exception.ReviewNotFoundException;
import triple.assignment.mileageapi.global.error.exception.UserNotFoundException;
import triple.assignment.mileageapi.place.domain.Place;
import triple.assignment.mileageapi.place.service.PlaceService;
import triple.assignment.mileageapi.point.service.PointService;
import triple.assignment.mileageapi.review.domain.Review;
import triple.assignment.mileageapi.review.domain.ReviewRepository;
import triple.assignment.mileageapi.user.domain.User;
import triple.assignment.mileageapi.user.service.UserService;

import static triple.assignment.mileageapi.global.error.ErrorCode.*;


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
        final Place place = placeService.getPlaceWithUUID(review.getPlaceId())
                .orElseThrow(() -> new PlaceNotFoundException(PLACE_NOT_FOUND));
        final User user = userService.getUserWithUUID(review.getUserId())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        checkAlreadyWritten(user, place);

        pointService.savePointHistory(user, review.getReviewId(), getSavePoint(review, place));

        return reviewRepository.save(  review.setUser(user).setPlace(place)  );
    }


    @Transactional
    public Review patch(Review review) {
        final Review findReview = reviewRepository.findByReviewId(review.getReviewId())
                .orElseThrow(() -> new ReviewNotFoundException(REVIEW_NOT_FOUND));

        pointService.savePointHistory(findReview.getUser(), findReview.getReviewId(), getChangePoint(review, findReview));

        findReview.clearAllPhotos();
        findReview.addPhotos(  review.getPhotos()  );

        return findReview.changeContent(review.getContent());
    }


    @Transactional
    public Review delete(Review review) {
        final Review findReview = reviewRepository.findByReviewId(review.getReviewId())
                .orElseThrow(() -> new ReviewNotFoundException(REVIEW_NOT_FOUND));

        pointService.savePointHistory(  findReview.getUser(), review.getReviewId(), getReleasePoint(findReview)  );
        reviewRepository.delete(findReview);

        return findReview;
    }


    private void checkAlreadyWritten(User user, Place place) {
        if (reviewRepository.existsByUserAndPlace(user, place)) {
            throw new DuplicateReviewException(DUPLICATE_REVIEW);
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


    private int getFirstReviewPoint(Review review) {
        final Review firstReview = reviewRepository.findTopByPlaceOrderByCreatedAtDesc(review.getPlace())
                .orElseThrow(() -> new ReviewNotFoundException(REVIEW_NOT_FOUND));
        return review.getReviewId().equals(firstReview.getReviewId()) ? 1 : 0;
    }

}
