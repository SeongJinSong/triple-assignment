package triple.assignment.mileageapi.point.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class PointHistoryResponse {
    private UUID userId;

    private List<PointDto> pointHistory;
}
