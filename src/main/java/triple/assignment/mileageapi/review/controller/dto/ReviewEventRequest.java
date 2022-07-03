package triple.assignment.mileageapi.review.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import triple.assignment.mileageapi.review.domain.Photo;
import triple.assignment.mileageapi.review.domain.Review;
import triple.assignment.mileageapi.review.domain.enumerated.ActionType;
import triple.assignment.mileageapi.review.domain.enumerated.EventType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class ReviewEventRequest {

    private EventType type;

    private ActionType action;

    private UUID reviewId;

    private String content;

    private final List<UUID> attachedPhotoIds = new ArrayList<>();

    private UUID userId;

    private UUID placeId;

    public void validate() {
        if (type != EventType.REVIEW) {
            throw new RuntimeException();
        }
    }

    public Review toReview() {
        Review review = Review.builder()
                .reviewId(reviewId)
                .placeId(placeId)
                .userId(userId)
                .content(content)
                .actionType(action)
                .build();
        review.setPhotos(attachedPhotoIds.stream()
                .map(e -> Photo.builder().photoId(e).build())
                .collect(Collectors.toList()));
        return review;
    }


}
