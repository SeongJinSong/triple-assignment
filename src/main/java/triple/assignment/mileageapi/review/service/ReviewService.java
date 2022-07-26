package triple.assignment.mileageapi.review.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import triple.assignment.mileageapi.global.dto.enumerated.ActionType;
import triple.assignment.mileageapi.global.error.exception.DuplicateReviewException;
import triple.assignment.mileageapi.global.error.exception.ReviewNotFoundException;
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
        final Place place = placeService.getPlaceByIdOrThrow(review.getPlaceId());
        final User user = userService.getUserByIdOrThrow(review.getUserId());

        checkAlreadyWritten(user, place);

        final int point = calculatePointForSave(review, place);
        pointService.savePointHistory(user, review.getReviewId(), point,ActionType.ADD);

        return reviewRepository.save(review.setUser(user).setPlace(place)).setPoint(point);
    }


    @Transactional
    public Review patch(Review targetReview) {
        final Review previousReview = reviewRepository.findByReviewId(targetReview.getReviewId())
                .orElseThrow(() -> new ReviewNotFoundException(REVIEW_NOT_FOUND));

        final int point = calculatePointForUpdate(targetReview, previousReview);
        pointService.savePointHistory(previousReview.getUser(), previousReview.getReviewId(), point, ActionType.MOD);

        previousReview.clearAllPhotos();
        previousReview.addPhotos(targetReview.getPhotos());

        return previousReview.changeContent(targetReview.getContent()).setPoint(point);
    }


    @Transactional
    public void delete(Review review) {
        final Review targetReview = reviewRepository.findByReviewId(review.getReviewId())
                .orElseThrow(() -> new ReviewNotFoundException(REVIEW_NOT_FOUND));

        pointService.savePointHistory(
                targetReview.getUser(), review.getReviewId(), calculatePointForDelete(targetReview), ActionType.DELETE);

        targetReview.clearAllMappings();

        reviewRepository.delete(targetReview);
    }


    private void checkAlreadyWritten(User user, Place place) {
        if (reviewRepository.existsByUserAndPlace(user, place)) {
            throw new DuplicateReviewException(DUPLICATE_REVIEW);
        }
    }



    private int calculatePointForUpdate(Review after, Review before) {
        return after.getContentAndPhotoPoint() - before.getContentAndPhotoPoint();
    }

    private int calculatePointForSave(Review review, Place place) {
        return review.getContentAndPhotoPoint() + place.getFirstReviewPoint() ;
    }

    private int calculatePointForDelete(Review review) {
        return -1 * (review.getContentAndPhotoPoint() + getFirstReviewPoint(review));
    }

    private int getFirstReviewPoint(Review review) {
        return review.getPlace().isFirstReview(review.getReviewId()) ? 1 : 0;
    }

}
