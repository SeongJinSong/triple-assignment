package triple.assignment.mileageapi.global;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import triple.assignment.mileageapi.place.domain.Place;
import triple.assignment.mileageapi.place.domain.PlaceRepository;
import triple.assignment.mileageapi.user.domain.User;
import triple.assignment.mileageapi.user.domain.UserRepository;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class DataInitializer {
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;

    /**
     * resources 디렉토리 하위의 data.sql 파일 수행 방식은 환경에 따라 수행되지 않는 경우도 발생해
     * Application ready event 방식으로 init data 처리
     */
    @Transactional
    @EventListener
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        userRepository.save(User.builder()
                .userId(UUID.fromString("3ede0ef2-92b7-4817-a5f3-0c575361f745"))
                .build());

        placeRepository.save(Place.builder()
                .placeId(UUID.fromString("2e4baf1c-5acb-4efb-a1af-eddada31b00f"))
                .build());

    }
}