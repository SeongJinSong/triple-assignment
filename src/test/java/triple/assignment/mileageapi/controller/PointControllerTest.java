package triple.assignment.mileageapi.controller;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import triple.assignment.mileageapi.point.controller.PointController;
import triple.assignment.mileageapi.point.controller.dto.PointDto;
import triple.assignment.mileageapi.point.controller.dto.PointHistoryResponse;
import triple.assignment.mileageapi.point.controller.dto.PointResponse;
import triple.assignment.mileageapi.point.domain.Point;
import triple.assignment.mileageapi.point.service.PointService;
import triple.assignment.mileageapi.user.domain.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = PointController.class)
public class PointControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PointService pointService;

    private Point point;
    private PointResponse response;
    private User user;

    @BeforeEach
    public void setup() {
        user = User.builder().userId(UUID.randomUUID()).points(new ArrayList<>()).build();
        point = Point.builder().user(user).score(1).build();
        response = PointResponse.builder().totalPoint(1).build();
    }


    @DisplayName("유저의 현재 누적 포인트를 조회한다.")
    @Test
    public void getPointSummaryTest() throws Exception {
        // given
        given(pointService.getCurrentPointByUser(any())).willReturn(point);

        // when & then
        mockMvc.perform(
                get("/users/{user-id}/points/summary", user.getUserId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userId").value(user.getUserId().toString()))
                .andExpect(jsonPath("$.data.totalPoint").value(response.getTotalPoint()))
                .andDo(print());
    }


    @DisplayName("유저의 포인트 변경 내역 리스트를 조회한다.")
    @Test
    public void getUserPointHistoryTest() throws Exception {
        // given
        List<Point> list = List.of(
                Point.builder().score(1).reviewId(UUID.randomUUID()).createdAt(LocalDateTime.now()).build(),
                Point.builder().score(3).reviewId(UUID.randomUUID()).createdAt(LocalDateTime.now()).build()
        );
        final PointHistoryResponse response = PointHistoryResponse.builder()
                .userId(user.getUserId())
                .pointHistory(List.of(
                        PointDto.builder().score(1).reviewId(UUID.randomUUID()).createdAt(LocalDateTime.now()).build(),
                        PointDto.builder().score(3).reviewId(UUID.randomUUID()).createdAt(LocalDateTime.now()).build())
                )
                .build();
        given(pointService.getPointHistoryByUser(any(), any())).willReturn(list);

        // when & then
        mockMvc.perform(
                get("/users/{user-id}/points/history", user.getUserId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userId").value(user.getUserId().toString()))
                .andExpect(jsonPath("$.data.pointHistory").isNotEmpty())
                .andDo(print());
    }


}
