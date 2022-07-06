package triple.assignment.mileageapi.global.error.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import triple.assignment.mileageapi.global.error.ErrorCode;

@Getter
@RequiredArgsConstructor
public class InvalidEventTypeException extends RuntimeException {
    private final ErrorCode errorCode;
}
