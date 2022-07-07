package triple.assignment.mileageapi.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import triple.assignment.mileageapi.place.domain.Place;
import triple.assignment.mileageapi.review.controller.ReviewController;
import triple.assignment.mileageapi.review.domain.Photo;
import triple.assignment.mileageapi.review.domain.Review;
import triple.assignment.mileageapi.review.service.ReviewService;
import triple.assignment.mileageapi.user.domain.User;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = ReviewController.class)
public class ReviewControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ReviewService reviewService;

    private Review review;

    @BeforeEach
    public void setup() {
        Place place = Place.builder().reviews(new ArrayList<>()).placeId(UUID.randomUUID()).build();

        User user = User.builder().reviews(new ArrayList<>()).userId(UUID.randomUUID()).build();

        Photo photo1 = Photo.builder().photoId(UUID.randomUUID()).build();
        Photo photo2 = Photo.builder().photoId(UUID.randomUUID()).build();
        List<Photo> list = new ArrayList<>();
        list.add(photo1);
        list.add(photo2);

        review = Review.builder()
                .reviewId(UUID.randomUUID())
                .place(place)
                .user(user)
                .photos(list) // List.of -> ImmutableCollections 반환하는 문제
                .content("test review-content")
                .build();
    }


    @DisplayName("handle ADD type request")
    @Test
    public void addReviewEventTest() throws Exception {
        // given
        final String addRequest = Files.readString(Paths.get("src/test/resources/AddRequest.json"));
        given(reviewService.create(any())).willReturn(review);

        // when & then
        mockMvc.perform(
                post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("200"))
                .andExpect(jsonPath("$.message").value("handle review create event success."));
    }


    @DisplayName("handle MOD type request")
    @Test
    public void patchReviewEventTest() throws Exception {
        // given
        final String patchRequest = Files.readString(Paths.get("src/test/resources/ModRequest.json"));
        given(reviewService.patch(any())).willReturn(review);

        // when & then
        mockMvc.perform(
                post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("200"))
                .andExpect(jsonPath("$.message").value("handle review patch event success."));
    }


    @DisplayName("handle DELETE type request")
    @Test
    public void deleteReviewEventTest() throws Exception {
        // given
        final String deleteRequest = Files.readString(Paths.get("src/test/resources/DeleteRequest.json"));
        doNothing().when(reviewService).delete(any());
        // when & then
        mockMvc.perform(
                post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(deleteRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("200"))
                .andExpect(jsonPath("$.message").value("handle review delete event success."));
    }

}
