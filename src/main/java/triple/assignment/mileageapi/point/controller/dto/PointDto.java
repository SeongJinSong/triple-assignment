package triple.assignment.mileageapi.point.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import triple.assignment.mileageapi.global.dto.enumerated.ActionType;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonPropertyOrder({"reviewId", "actionType", "score", "createdAt"})
@Getter
@Builder
@AllArgsConstructor
public class PointDto {
    private UUID reviewId;

    private int score;

    private ActionType actionType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
}
