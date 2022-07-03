package triple.assignment.mileageapi.point.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class PointResponse {

    private UUID userId;

    private int totalPoint;

}
