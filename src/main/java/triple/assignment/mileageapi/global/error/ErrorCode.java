package triple.assignment.mileageapi.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INVALID_EVENT_TYPE(HttpStatus.BAD_REQUEST, "invalid event type."),

    INVALID_ACTION_TYPE(HttpStatus.BAD_REQUEST, "invalid action type."),

    INVALID_REVIEW_INFO(HttpStatus.BAD_REQUEST, "at least one of content or photo is required"),

    DUPLICATE_REVIEW(HttpStatus.BAD_REQUEST, "your review of the place already exists."),

    PLACE_NOT_FOUND(HttpStatus.NOT_FOUND, "there is no place matching the placeId."),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "there is no user matching the userId."),

    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "there is no review matching the reviewId.");



    private final HttpStatus httpStatus;
    private final String messageDetails;

}
