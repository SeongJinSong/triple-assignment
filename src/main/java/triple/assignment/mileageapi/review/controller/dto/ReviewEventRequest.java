package triple.assignment.mileageapi.review.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import triple.assignment.mileageapi.place.domain.Place;
import triple.assignment.mileageapi.review.domain.Photo;
import triple.assignment.mileageapi.review.domain.Review;
import triple.assignment.mileageapi.user.domain.User;
import triple.assignment.mileageapi.review.domain.enumerated.ActionType;
import triple.assignment.mileageapi.review.domain.enumerated.EventType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter // jackson (역)직렬화
@NoArgsConstructor
public class ReviewEventRequest {
    private EventType eventType;

    private ActionType actionType;

    private UUID reviewId;

    private String content;

    private final List<UUID> attachedPhotoIds = new ArrayList<>();

    private UUID userId;

    private UUID placeId;

    public void validate() {
        if (eventType != EventType.REVIEW) {
            throw new RuntimeException();
        }
    }

    public Review toReview() {
        return Review.builder()
                .photos(
                        attachedPhotoIds.stream()
                                .map(e -> Photo.builder().photoId(e).build())
                                .collect(Collectors.toList())
                )
                .reviewId(reviewId)
                .placeId(placeId)
                .userId(userId)
                .content(content)
                .actionType(actionType)
                .build();
    }

    public User toUser() {
        return User.builder().userId(userId).build();
    }

    public Place toPlace() {
        return Place.builder().placeId(placeId).build();
    }
}
