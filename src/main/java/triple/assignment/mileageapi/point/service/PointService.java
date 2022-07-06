package triple.assignment.mileageapi.point.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import triple.assignment.mileageapi.point.domain.Point;
import triple.assignment.mileageapi.point.domain.PointRepository;
import triple.assignment.mileageapi.user.domain.User;
import triple.assignment.mileageapi.user.service.UserService;

import java.util.List;
import java.util.UUID;


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

    @Transactional(readOnly = true)
    public Point getCurrentPointByUser(UUID userId) {
        final User user = userService.getUserByIdOrThrow(userId);
        return Point.builder()
                .user(user)
                .score(user.getPoint())
                .build();
    }


    @Transactional(readOnly = true)
    public List<Point> getPointHistoryByUser(UUID userId, Pageable pageable) {
        final User user = userService.getUserByIdOrThrow(userId);
        return pointRepository.findAllByUser(user, pageable);
    }
}
