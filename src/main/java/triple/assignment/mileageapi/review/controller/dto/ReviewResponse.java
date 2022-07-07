package triple.assignment.mileageapi.review.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import triple.assignment.mileageapi.review.domain.Photo;
import triple.assignment.mileageapi.review.domain.Review;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter // deserialization
@Setter
@Builder
@AllArgsConstructor
public class ReviewResponse {

    private Long id;

    private UUID userId;

    private UUID reviewId;

    private UUID placeId;

    private String content;

    private List<UUID> photos;

    private int point;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modifiedAt;

    public static ReviewResponse of(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .reviewId(review.getReviewId())
                .placeId(review.getPlaceId())
                .userId(review.getUserId())
                .photos(
                        review.getPhotos().stream()
                                .map(Photo::getPhotoId)
                                .collect(Collectors.toList())
                )
                .content(review.getContent())
                .createdAt(review.getCreatedAt())
                .modifiedAt(review.getModifiedAt())
                .point(review.getPoint())
                .build();
    }

}
