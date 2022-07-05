package triple.assignment.mileageapi.point.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import triple.assignment.mileageapi.point.domain.Point;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class PointResponse {

    private UUID userId;

    private int totalPoint;

    public static PointResponse of(Point point) {
        return PointResponse.builder()
                .userId(point.getUser().getUserId())
                .totalPoint(point.getScore())
                .build();
    }
}
