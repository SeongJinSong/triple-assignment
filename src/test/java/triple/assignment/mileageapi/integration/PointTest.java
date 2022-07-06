package triple.assignment.mileageapi.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import triple.assignment.mileageapi.place.domain.Place;
import triple.assignment.mileageapi.place.domain.PlaceRepository;
import triple.assignment.mileageapi.point.domain.Point;
import triple.assignment.mileageapi.point.domain.PointRepository;
import triple.assignment.mileageapi.review.controller.dto.ReviewEventRequest;
import triple.assignment.mileageapi.review.domain.Photo;
import triple.assignment.mileageapi.review.domain.PhotoRepository;
import triple.assignment.mileageapi.review.domain.Review;
import triple.assignment.mileageapi.review.domain.ReviewRepository;
import triple.assignment.mileageapi.global.dto.enumerated.ActionType;
import triple.assignment.mileageapi.global.dto.enumerated.EventType;
import triple.assignment.mileageapi.user.domain.User;
import triple.assignment.mileageapi.user.domain.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PointTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private PhotoRepository photoRepository;
    @Autowired
    private PointRepository pointRepository;

    private User user1;
    private Photo photo;
    private Place placeWithNoReview;
    private Place placeWithReview;

    /**
     *     place  :  placeWithReview   placeWithNoReview
     *     review :     u1, u2                x
     */
    @BeforeEach
    public void setup() {
        user1 = User.builder().userId(UUID.randomUUID()).reviews(new ArrayList<>()).build();
        User user2 = User.builder().userId(UUID.randomUUID()).reviews(new ArrayList<>()).build();
        userRepository.saveAll(List.of(user1, user2));

        placeWithReview = Place.builder().placeId(UUID.randomUUID()).reviews(new ArrayList<>()).build();
        placeWithNoReview = Place.builder().placeId(UUID.randomUUID()).reviews(new ArrayList<>()).build();
        placeRepository.saveAll(List.of(placeWithNoReview, placeWithReview));

        photo = Photo.builder().photoId(UUID.randomUUID()).build();
        photoRepository.save(photo);

        Review review1 = Review.builder()
                .reviewId(UUID.randomUUID())
                .user(user1)
                .place(placeWithReview)
                .content("review-1")
                .build();
        Review review2 = Review.builder()
                .reviewId(UUID.randomUUID())
                .user(user2)
                .place(placeWithReview)
                .content("review-2")
                .build();

        List<Photo> photoList = new ArrayList<>();
        photoList.add(photo);
        review1.addPhotos(photoList);
        review2.addPhotos(photoList);

        reviewRepository.saveAll(List.of(review1, review2));
        user1.addReview(review1);
        user2.addReview(review2);

        placeWithReview.addReview(review1);
        placeWithReview.addReview(review2);

    }


    @DisplayName("첫 리뷰 등록 시 포인트 3점 부여")
    @Test
    public void writeReviewFirst() throws Exception {
        // given
        final String request = objectMapper.writeValueAsString(addRequestOf(user1.getUserId(), placeWithNoReview));

        // when & then
        mockMvc.perform(
                post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.point").value(3))
                .andDo(print());
    }



    @DisplayName("첫 리뷰가 아닌 경우 보너스 포인트 제외한 점수 부여")
    @Test
    public void writeSecondReview() throws Exception {
        // given
        User newUser = userRepository.save(User.builder().userId(UUID.randomUUID()).build());
        final String request = objectMapper.writeValueAsString(addRequestOf(newUser.getUserId(), placeWithReview));

        // when & then
        mockMvc.perform(
                post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.point").value(2))
                .andDo(print());
    }



    @DisplayName("리뷰 삭제 시 점수 차감")
    @Test
    public void deleteReview() throws Exception {
        // given
        Point point = pointRepository.save(Point.builder().score(3).user(user1).build());
        user1.addPoint(point);
        final int beforePoint = user1.getPoint();
        final String request = objectMapper.writeValueAsString(deleteRequestOf(user1.getUserId(), placeWithReview));

        // when & then
        mockMvc.perform(
                post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andDo(print());

        final int afterPoint = pointRepository.findAllByUser(user1, Pageable.unpaged()).stream()
                .map(Point::getScore)
                .reduce(0, Integer::sum);
        assertEquals(beforePoint - 3, afterPoint);
    }


    private ReviewEventRequest addRequestOf(UUID userId, Place place) {
        ReviewEventRequest request = new ReviewEventRequest();
        request.setAction(ActionType.ADD);
        request.setType(EventType.REVIEW);
        request.setUserId(userId);
        request.setReviewId(UUID.randomUUID());
        request.setContent("new test");
        request.setPlaceId(place.getPlaceId());
        request.getAttachedPhotoIds().add(photo.getPhotoId());
        return request;
    }


    private ReviewEventRequest deleteRequestOf(UUID userId, Place place) {
        ReviewEventRequest request = new ReviewEventRequest();
        request.setAction(ActionType.DELETE);
        request.setType(EventType.REVIEW);
        request.setUserId(userId);
        request.setReviewId(user1.getReviews().get(0).getReviewId());
        request.setContent("new test");
        request.setPlaceId(place.getPlaceId());
        request.getAttachedPhotoIds().add(photo.getPhotoId());
        return request;
    }


}
