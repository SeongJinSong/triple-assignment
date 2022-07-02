package triple.assignment.mileageapi.place.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {
    Optional<Place> findByPlaceId(UUID placeId);
}
