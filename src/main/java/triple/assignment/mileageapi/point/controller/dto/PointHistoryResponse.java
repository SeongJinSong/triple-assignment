package triple.assignment.mileageapi.point.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import triple.assignment.mileageapi.point.domain.Point;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class PointHistoryResponse {
    private UUID userId;

    private List<PointDto> pointHistory;

    public static PointHistoryResponse of(List<Point> points) {
        return PointHistoryResponse.builder()
                .pointHistory(points.stream()
                        .map(e -> PointDto.builder()
                                .createdAt(e.getCreatedAt())
                                .reviewId(e.getReviewId())
                                .score(e.getScore())
                                .build()
                        ).collect(Collectors.toList())
                )
                .build();
    }
}
