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

    private Review delete(Review review) {
        return null;
    }

    private Review patch(Review review) {
        return null;
    }


    @Transactional
    public Review create(Review review) {
        final Place place = placeService.getPlaceWithUUID(review.getPlaceId()).orElseThrow(); // TODO Exception
        final User user = userService.getUserWithUUID(review.getUserId()).orElseThrow(); // TODO Exception

        // 이미 작성했던 리뷰가 있는지 확인
        checkAlreadyWritten(user, place);

        // 포인트 계산 및 이력 저장
        final int point = determinePoint(review, place);
        pointService.savePointHistory(user, review.getReviewId(), point);

        // 리뷰 저장
        return reviewRepository.save(
                review.setUser(user).setPlace(place).setPoint(point)
        );
    }

    private void checkAlreadyWritten(User user, Place place) {
        if (reviewRepository.existsByUserAndPlace(user, place)) {
            throw new RuntimeException(); // TODO
        }
    }

    private int determinePoint(Review review, Place place) {
        return review.getPhotoPoint() + review.getContentPoint() + place.getFirstReviewPoint();
    }

}
