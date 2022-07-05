package triple.assignment.mileageapi.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import triple.assignment.mileageapi.point.controller.dto.PointHistoryResponse;
import triple.assignment.mileageapi.point.controller.dto.PointResponse;
import triple.assignment.mileageapi.point.domain.Point;
import triple.assignment.mileageapi.point.domain.PointRepository;
import triple.assignment.mileageapi.point.service.PointService;
import triple.assignment.mileageapi.user.domain.User;
import triple.assignment.mileageapi.user.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
@ExtendWith(MockitoExtension.class)
public class PointServiceTest {
    @Mock
    private UserService userService;
    @Mock
    private PointRepository pointRepository;
    @InjectMocks
    private PointService pointService;

    private User user;
    private List<Point> points;

    /**
     * 조회 테스트 - 한번만 초기화
     */
    @BeforeAll
    public void setup() {
        user = User.builder().userId(UUID.randomUUID()).points(new ArrayList<>()).build();
        points = IntStream.rangeClosed(1, 5)
                .mapToObj(e -> Point.builder().score(3).user(user).build())
                .collect(Collectors.toList());
        user.getPoints().addAll(points);
    }

    @DisplayName("get user's current point")
    @Test
    public void getCurrentPointByUserTest() {
        // given
        given(userService.getUserByIdOrThrow(any())).willReturn(user);
        // when
        PointResponse response = PointResponse.of(pointService.getCurrentPointByUser(user.getUserId()));
        // then
        assertEquals(points.size() * 3, response.getTotalPoint());
    }

    @DisplayName("get user's total point history")
    @Test
    public void getPointHistoryByUserTest() {
        // when
        PointHistoryResponse response = pointService.getPointHistoryByUser(user.getUserId());
        // then
        assertEquals(response.getPointHistory().size(), 5);
    }

}
