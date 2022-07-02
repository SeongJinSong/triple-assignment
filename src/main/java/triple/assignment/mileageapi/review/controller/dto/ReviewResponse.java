package triple.assignment.mileageapi.review.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter // deserialization
@Builder
@AllArgsConstructor
public class ReviewResponse {

    private Long id;

    private UUID userId;

    private UUID reviewId;

    private UUID placeId;

    private String content;

    private List<UUID> photos = new ArrayList<>();

    private int point;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "YYYY-MM-DD hh:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "YYYY-MM-DD hh:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime LastModifiedAt;
}
