package triple.assignment.mileageapi.place.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import triple.assignment.mileageapi.global.error.exception.PlaceNotFoundException;
import triple.assignment.mileageapi.place.domain.Place;
import triple.assignment.mileageapi.place.domain.PlaceRepository;

import java.util.UUID;

import static triple.assignment.mileageapi.global.error.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class PlaceService {
    private final PlaceRepository placeRepository;

    public Place getPlaceByIdOrThrow(UUID placeId) {
        return placeRepository.findByPlaceId(placeId)
                .orElseThrow(() -> new PlaceNotFoundException(PLACE_NOT_FOUND));
    }
}
