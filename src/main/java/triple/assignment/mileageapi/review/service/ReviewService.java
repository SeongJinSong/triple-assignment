package triple.assignment.mileageapi.review.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import triple.assignment.mileageapi.review.controller.dto.request.ReviewEventRequest;
import triple.assignment.mileageapi.review.controller.dto.response.ReviewResponse;
import triple.assignment.mileageapi.review.domain.Review;
import triple.assignment.mileageapi.review.domain.enumerated.ActionType;
import triple.assignment.mileageapi.review.domain.ReviewRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

//    public ReviewResponse handleReview(ReviewEventRequest request) {
//    }


//    public ReviewResponse create(ReviewEventRequest request) {
//
//    }


    public Optional<Review> getReview(Long id) {
        return reviewRepository.findById(id);
    }
}
