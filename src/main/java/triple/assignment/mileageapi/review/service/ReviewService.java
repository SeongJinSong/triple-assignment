package triple.assignment.mileageapi.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import triple.assignment.mileageapi.place.domain.Place;
import triple.assignment.mileageapi.place.service.PlaceService;
import triple.assignment.mileageapi.point.service.PointService;
import triple.assignment.mileageapi.review.controller.dto.ReviewEventRequest;
import triple.assignment.mileageapi.review.controller.dto.ReviewResponse;
import triple.assignment.mileageapi.review.domain.Photo;
import triple.assignment.mileageapi.review.domain.Review;
import triple.assignment.mileageapi.review.domain.ReviewRepository;
import triple.assignment.mileageapi.user.domain.User;
import triple.assignment.mileageapi.user.service.UserService;

import java.util.List;
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

    public ReviewResponse handleReview(Review review) {
//        switch (review.getActionType()) {
//            case ADD: create(review);
//            case MODE: patch(review);
//            case DELETE: delete(review);
//            default:
//                throw new RuntimeException(); // TODO
//        }
        return null;
    }

    private void delete(Review review) {
    }

    private void patch(Review review) {
    }


    @Transactional
    public ReviewResponse create(Review review) {
        final Place place = placeService.getPlaceWithUUID(review.getPlaceId()).orElseThrow(); // TODO Exception
        final User user = userService.getUserWithUUID(review.getUserId()).orElseThrow(); // TODO Exception

        // 이미 작성했던 리뷰가 있는지 확인
        checkAlreadyWritten(user, place);

        // 포인트 계산 및 이력 저장
        final int point = determinePoint(review, place);
        pointService.savePointHistory(user, review.getReviewId(), point);


        return review
                .setUser(user)
                .setPlace(place)
                .setPoint(point)
                .toResponse();
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
