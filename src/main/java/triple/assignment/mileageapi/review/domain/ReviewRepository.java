package triple.assignment.mileageapi.review.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import triple.assignment.mileageapi.place.domain.Place;
import triple.assignment.mileageapi.review.domain.Review;
import triple.assignment.mileageapi.user.domain.User;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByUserAndPlace(User user, Place place);
}
