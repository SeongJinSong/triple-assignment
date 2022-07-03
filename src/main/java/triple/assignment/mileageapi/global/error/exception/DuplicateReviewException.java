package triple.assignment.mileageapi.global.error.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import triple.assignment.mileageapi.global.error.ErrorCode;

@Getter
@RequiredArgsConstructor
public class DuplicateReviewException extends RuntimeException {
    private final ErrorCode errorCode;

}
