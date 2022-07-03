package triple.assignment.mileageapi.review.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import triple.assignment.mileageapi.global.dto.ResponseWrapper;
import triple.assignment.mileageapi.review.controller.dto.ReviewEventRequest;
import triple.assignment.mileageapi.review.controller.dto.ReviewResponse;
import triple.assignment.mileageapi.review.domain.enumerated.ActionType;
import triple.assignment.mileageapi.review.service.ReviewService;

@RequiredArgsConstructor
@RequestMapping(path = "/events")
@RestController
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ResponseWrapper<ReviewResponse>> handleReviewEvent(@RequestBody ReviewEventRequest request) {
        request.validate(); // TODO
        ReviewResponse response;
        if (request.getAction() == ActionType.ADD) {
             response = reviewService.create(request.toReview()).toResponse();
        }
        else if (request.getAction() == ActionType.MOD) {
             response = reviewService.patch(request.toReview()).toResponse();
        }
        else {
             response = reviewService.delete(request.toReview()).toResponse();
        }

        return ResponseWrapper.ok("handle review event success.", response);
    }
}
