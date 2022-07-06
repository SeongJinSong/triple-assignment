package triple.assignment.mileageapi.global.error;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import triple.assignment.mileageapi.global.error.exception.*;

@Slf4j
@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = UserNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        log.error("handleUserNotFoundException - ");
        return ErrorResponse.toResponseEntity(ex.getErrorCode());
    }


    @ExceptionHandler(value = PlaceNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handlePlaceNotFoundException(PlaceNotFoundException ex) {
        log.error("handlePlaceNotFoundException - ");
        return ErrorResponse.toResponseEntity(ex.getErrorCode());
    }


    @ExceptionHandler(value = ReviewNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleReviewNotFoundException(ReviewNotFoundException ex) {
        log.error("handleReviewNotFoundException - ");
        return ErrorResponse.toResponseEntity(ex.getErrorCode());
    }


    @ExceptionHandler(value = DuplicateReviewException.class)
    protected ResponseEntity<ErrorResponse> handleDuplicateReviewException(DuplicateReviewException ex) {
        log.error("handleDuplicateReviewException - ");
        return ErrorResponse.toResponseEntity(ex.getErrorCode());
    }


    @ExceptionHandler(value = InvalidReviewException.class)
    protected ResponseEntity<ErrorResponse> handleInvalidReviewException(InvalidReviewException ex) {
        log.error("handleInvalidReviewException - ");
        return ErrorResponse.toResponseEntity(ex.getErrorCode());
    }


    @ExceptionHandler(value = InvalidActionTypeException.class)
    protected ResponseEntity<ErrorResponse> handleInvalidActionTypeException(InvalidActionTypeException ex) {
        log.error("handleInvalidActionTypeException - ");
        return ErrorResponse.toResponseEntity(ex.getErrorCode());
    }


    @ExceptionHandler(value = InvalidEventTypeException.class)
    protected ResponseEntity<ErrorResponse> handleInvalidEventTypeException(InvalidEventTypeException ex) {
        log.error("handleInvalidEventTypeException - ");
        return ErrorResponse.toResponseEntity(ex.getErrorCode());
    }
}
