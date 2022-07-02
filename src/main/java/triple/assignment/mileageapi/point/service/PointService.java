package triple.assignment.mileageapi.point.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import triple.assignment.mileageapi.point.domain.Point;
import triple.assignment.mileageapi.point.domain.PointRepository;
import triple.assignment.mileageapi.user.domain.User;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PointService {
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
}
