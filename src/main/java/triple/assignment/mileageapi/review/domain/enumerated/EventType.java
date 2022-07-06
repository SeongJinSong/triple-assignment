package triple.assignment.mileageapi.review.domain.enumerated;

import com.fasterxml.jackson.annotation.JsonCreator;
import triple.assignment.mileageapi.global.error.ErrorCode;
import triple.assignment.mileageapi.global.error.exception.InvalidEventTypeException;

public enum EventType {
    REVIEW;

    @JsonCreator
    public static EventType typeOf(String valueFromClient) {
        if (REVIEW.toString().equalsIgnoreCase(valueFromClient)) {
            return REVIEW;
        }
        throw new InvalidEventTypeException(ErrorCode.INVALID_EVENT_TYPE);
    }
}
