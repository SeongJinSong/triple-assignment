package triple.assignment.mileageapi.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import triple.assignment.mileageapi.user.domain.User;
import triple.assignment.mileageapi.user.domain.UserRepository;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public Optional<User> getUserWithUUID(UUID userId) {
        return userRepository.findByUserId(userId);
    }
}
