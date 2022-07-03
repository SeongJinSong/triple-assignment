package triple.assignment.mileageapi.point.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import triple.assignment.mileageapi.point.controller.dto.PointHistoryResponse;
import triple.assignment.mileageapi.point.controller.dto.PointResponse;
import triple.assignment.mileageapi.point.domain.Point;
import triple.assignment.mileageapi.point.domain.PointRepository;
import triple.assignment.mileageapi.user.domain.User;
import triple.assignment.mileageapi.user.service.UserService;

import java.util.UUID;
import java.util.stream.Collectors;

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
                        pointRepository.findAllByUser(userService.getUserWithUUID(userId).orElseThrow())
                                .stream()
                                .map(Point::getScore)
                                .reduce(0, Integer::sum)
                )
                .build();
    }


    public PointHistoryResponse getPointHistoryByUser(UUID userId) {
        return PointHistoryResponse.builder()
                .userId(userId)
                .pointHistory(
                        pointRepository.findAllByUser(userService.getUserWithUUID(userId).orElseThrow())
                                .stream()
                                .map(Point::toDto)
                                .collect(Collectors.toList())
                )
                .build();

    }
}
