package triple.assignment.mileageapi.point.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import triple.assignment.mileageapi.global.dto.ResponseWrapper;
import triple.assignment.mileageapi.point.controller.dto.PointHistoryResponse;
import triple.assignment.mileageapi.point.controller.dto.PointResponse;
import triple.assignment.mileageapi.point.service.PointService;

import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping(path = "/points")
@RestController
public class PointController {
    private final PointService pointService;

    @GetMapping(path = "/summary")
    public ResponseEntity<ResponseWrapper<PointResponse>> getUserPointSummary(@RequestParam("user-id") UUID userId) {
        final PointResponse response = pointService.getCurrentPointByUser(userId);
        return ResponseWrapper.ok("get user total point success", response);
    }

    @GetMapping(path = "/history")
    public ResponseEntity<ResponseWrapper<PointHistoryResponse>> getUserPointHistory(@RequestParam("user-id") UUID userId) {
        final PointHistoryResponse response = pointService.getPointHistoryByUser(userId);
        return ResponseWrapper.ok("get user point history success", response);
    }
}
