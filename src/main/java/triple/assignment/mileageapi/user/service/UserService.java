package triple.assignment.mileageapi.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import triple.assignment.mileageapi.global.error.exception.UserNotFoundException;
import triple.assignment.mileageapi.user.domain.User;
import triple.assignment.mileageapi.user.domain.UserRepository;

import java.util.UUID;

import static triple.assignment.mileageapi.global.error.ErrorCode.USER_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public User getUserByIdOrThrow(UUID userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
    }
}
