package triple.assignment.mileageapi.point.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import triple.assignment.mileageapi.global.error.exception.UserNotFoundException;
import triple.assignment.mileageapi.point.controller.dto.PointHistoryResponse;
import triple.assignment.mileageapi.point.controller.dto.PointResponse;
import triple.assignment.mileageapi.point.domain.Point;
import triple.assignment.mileageapi.point.domain.PointRepository;
import triple.assignment.mileageapi.user.domain.User;
import triple.assignment.mileageapi.user.service.UserService;

import java.util.UUID;
import java.util.stream.Collectors;

import static triple.assignment.mileageapi.global.error.ErrorCode.*;


@RequiredArgsConstructor
@Service
public class PointService {
    private final UserService userService;
    private final PointRepository pointRepository;

    @Transactional
    public void savePointHistory(User user, UUID reviewId, int score) {
        pointRepository.save(
                Point.builder()
                        .user(user)
                        .reviewId(reviewId)
                        .score(score)
                        .build()
        );
    }


    public PointResponse getPointByUser(UUID userId) {
        return PointResponse.builder()
                .userId(userId)
                .totalPoint(
                        userService.getUserWithUUID(userId)
                                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND))
                                .getPoint()
                )
                .build();
    }


    public PointHistoryResponse getPointHistoryByUser(UUID userId) {
        return PointHistoryResponse.builder()
                .userId(userId)
                .pointHistory(
                        userService.getUserWithUUID(userId)
                                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND))
                                .getPoints().stream()
                                .map(Point::toDto)
                                .collect(Collectors.toList())
                )
                .build();

    }
}
