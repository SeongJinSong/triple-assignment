package triple.assignment.mileageapi.place.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import triple.assignment.mileageapi.place.domain.Place;
import triple.assignment.mileageapi.place.domain.PlaceRepository;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PlaceService {
    private final PlaceRepository placeRepository;

    public Optional<Place> getPlaceWithUUID(UUID placeId) {
        return placeRepository.findByPlaceId(placeId);
    }
}
