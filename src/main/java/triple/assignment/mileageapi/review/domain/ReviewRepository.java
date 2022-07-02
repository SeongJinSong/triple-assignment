package triple.assignment.mileageapi.review.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import triple.assignment.mileageapi.review.domain.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

}
