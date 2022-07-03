package triple.assignment.mileageapi.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import triple.assignment.mileageapi.place.domain.Place;
import triple.assignment.mileageapi.place.service.PlaceService;
import triple.assignment.mileageapi.point.service.PointService;
import triple.assignment.mileageapi.review.domain.Review;
import triple.assignment.mileageapi.review.domain.ReviewRepository;
import triple.assignment.mileageapi.review.domain.enumerated.ActionType;
import triple.assignment.mileageapi.user.domain.User;
import triple.assignment.mileageapi.user.service.UserService;

import java.util.Optional;
import java.util.UUID;


@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ReviewService {
    private final PlaceService placeService;
    private final UserService userService;
    private final PointService pointService;
    private final ReviewRepository reviewRepository;

    public Review handleReview(Review review) {
        if (review.getActionType() == ActionType.ADD) return create(review);
        else if (review.getActionType() == ActionType.MOD) return patch(review);
        else return delete(review);
    }


    @Transactional
    public Review create(Review review) {
        final Place place = placeService.getPlaceWithUUID(review.getPlaceId()).orElseThrow(); // TODO Exception
        final User user = userService.getUserWithUUID(review.getUserId()).orElseThrow(); // TODO Exception

        checkAlreadyWritten(user, place);

        final Review saveReview = reviewRepository.save(  review.setUser(user).setPlace(place)  );
        pointService.savePointHistory(user, review.getReviewId(), getSavePoint(saveReview));

        return saveReview;
    }


    @Transactional
    public Review patch(Review review) {
        final Review findReview = getReview(review.getReviewId())
                .orElseThrow()
                .changeContent(review.getContent())
                .changePhotos(review.getPhotos());

        pointService.savePointHistory(  findReview.getUser(), findReview.getReviewId(), getSavePoint(findReview)  );

        return review;
    }


    @Transactional
    public Review delete(Review review) {
        final Review findReview = getReview(review.getReviewId()).orElseThrow();

        pointService.savePointHistory(  findReview.getUser(), review.getReviewId(), getReleasePoint(findReview)  );
        reviewRepository.delete(findReview);

        return findReview;
    }


    public Optional<Review> getReview(UUID reviewId) {
        return reviewRepository.findByReviewId(reviewId);
    }


    private void checkAlreadyWritten(User user, Place place) {
        if (reviewRepository.existsByUserAndPlace(user, place)) {
            throw new RuntimeException(); // TODO
        }
    }



    private int getSavePoint(Review review) {
        return review.getPhotoPoint() + review.getContentPoint() + review.getPlace().getFirstReviewPoint();
    }

    private int getReleasePoint(Review review) {
        return -1 * (review.getPhotoPoint() + review.getContentPoint() + getFirstReviewPoint(review));
    }

    private int getFirstReviewPoint(Review review) {
        final Review firstReview = reviewRepository.findTopByPlaceOrderByCreatedAtDesc(review.getPlace())
                .orElseThrow();
        return review.getReviewId().equals(firstReview.getReviewId()) ? 1 : 0;
    }

}
