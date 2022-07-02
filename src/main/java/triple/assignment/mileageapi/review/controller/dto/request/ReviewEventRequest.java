package triple.assignment.mileageapi.review.controller.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import triple.assignment.mileageapi.review.domain.enumerated.ActionType;
import triple.assignment.mileageapi.review.domain.enumerated.EventType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
}
