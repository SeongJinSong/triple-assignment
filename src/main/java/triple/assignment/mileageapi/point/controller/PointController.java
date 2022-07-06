package triple.assignment.mileageapi.point.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import triple.assignment.mileageapi.global.dto.ResponseWrapper;
import triple.assignment.mileageapi.point.controller.dto.PointHistoryResponse;
import triple.assignment.mileageapi.point.controller.dto.PointResponse;
import triple.assignment.mileageapi.point.service.PointService;

import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping(path = "/users/{user-id}/points")
@RestController
public class PointController {
    private final PointService pointService;

    @GetMapping(path = "/summary")
    public ResponseEntity<ResponseWrapper<PointResponse>> getUserPointSummary(@PathVariable("user-id") UUID userId) {
        final PointResponse response = PointResponse.of(pointService.getCurrentPointByUser(userId));
        return ResponseWrapper.ok("get user total point success", response);
    }

    @GetMapping(path = "/history")
    public ResponseEntity<ResponseWrapper<PointHistoryResponse>> getUserPointHistory(@PathVariable("user-id") UUID userId,
                                                                                     Pageable pageable) {
        final PointHistoryResponse response =
                PointHistoryResponse.of(pointService.getPointHistoryByUser(userId, pageable));
        return ResponseWrapper.ok("get user point history success", response);
    }
}
