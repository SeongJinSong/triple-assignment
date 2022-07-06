package triple.assignment.mileageapi.point.domain;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import triple.assignment.mileageapi.user.domain.User;

import java.util.List;


@Repository
public interface PointRepository extends JpaRepository<Point, Long> {
    List<Point> findAllByUser(User user, Pageable pageable);
}
