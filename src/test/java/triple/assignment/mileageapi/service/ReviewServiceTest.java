package triple.assignment.mileageapi.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import triple.assignment.mileageapi.global.error.exception.DuplicateReviewException;
import triple.assignment.mileageapi.place.domain.Place;
import triple.assignment.mileageapi.place.service.PlaceService;
import triple.assignment.mileageapi.point.service.PointService;
import triple.assignment.mileageapi.review.domain.Photo;
import triple.assignment.mileageapi.review.domain.Review;
import triple.assignment.mileageapi.review.domain.ReviewRepository;
import triple.assignment.mileageapi.review.service.ReviewService;
import triple.assignment.mileageapi.user.domain.User;
import triple.assignment.mileageapi.user.service.UserService;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@Slf4j
@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {
    @Mock
    private PlaceService placeService;
    @Mock
    private UserService userService;
    @Mock
    private PointService pointService;
    @Mock
    private ReviewRepository reviewRepository;
    @InjectMocks
    private ReviewService reviewService;

    private Place place;
    private User user;
    private Photo photo1;
    private Photo photo2;
    private Review review;

    @BeforeEach
    public void setup() {
        place = Place.builder().reviews(new ArrayList<>()).placeId(UUID.randomUUID()).build();

        user = User.builder().reviews(new ArrayList<>()).userId(UUID.randomUUID()).build();

        photo1 = Photo.builder().photoId(UUID.randomUUID()).build();
        photo2 = Photo.builder().photoId(UUID.randomUUID()).build();
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


    @DisplayName("WHEN create review with correct info THEN return success")
    @Test
    public void createReview() {
        // given
        given(placeService.getPlaceByIdOrThrow(any())).willReturn(place);
        given(userService.getUserByIdOrThrow(any())).willReturn(user);
        given(reviewRepository.existsByUserAndPlace(any(), any())).willReturn(Boolean.FALSE);
        doNothing().when(pointService).savePointHistory(any(), any(), anyInt());
        given(reviewRepository.save(any())).willReturn(review);

        // when
        Review createReview = reviewService.create(review);

        // then
        assertNotNull(createReview);
        verify(pointService).savePointHistory(any(), any(), anyInt());
        verify(reviewRepository).existsByUserAndPlace(any(), any());
        verify(reviewRepository).save(any());
    }


    @DisplayName("WHEN create duplicated reviews in one place THEN failed")
    @Test
    public void createTwoReviewsInOnePlace() {
        // given
        given(placeService.getPlaceByIdOrThrow(any())).willReturn(place);
        given(userService.getUserByIdOrThrow(any())).willReturn(user);
        given(reviewRepository.existsByUserAndPlace(any(), any())).willReturn(Boolean.TRUE);

        // when & then
        assertThatThrownBy(() -> reviewService.create(review))
                .isInstanceOf(DuplicateReviewException.class);
        verify(reviewRepository).existsByUserAndPlace(any(), any());
    }


//    @DisplayName("WHEN create first review with content and photos THEN get 3 points")
//    @Test
//    public void createFirstReview() {
//        // given
//        given(placeService.getPlaceByIdOrThrow(any())).willReturn(place);
//        given(userService.getUserByIdOrThrow(any())).willReturn(user);
//        given(reviewRepository.existsByUserAndPlace(any(), any())).willReturn(Boolean.FALSE);
//        doNothing().when(pointService).savePointHistory(any(), any(), anyInt());
//        given(reviewRepository.save(any())).willReturn(review);
//
//        // when
//        Review createReview = reviewService.create(review);
//
//        // then
//        assertNotNull(createReview);
//        verify(reviewRepository).existsByUserAndPlace(any(), any());
//        verify(reviewRepository).save(any());
//    }


    @DisplayName("WHEN path reviews THEN get changed photos and content")
    @Test
    public void patchPhotosAndContent() {
        // given
        Photo newPhoto1 = Photo.builder().photoId(UUID.randomUUID()).build();
        Photo newPhoto2 = Photo.builder().photoId(UUID.randomUUID()).build();

        Review targetReview = Review.builder()
                .reviewId(UUID.randomUUID())
                .place(place)
                .user(user)
                .photos(List.of(newPhoto1, newPhoto2))
                .content("change review-content")
                .build();

        given(reviewRepository.findByReviewId(any())).willReturn(Optional.of(review));
        doNothing().when(pointService).savePointHistory(any(), any(), anyInt());

        // when
        Review afterReview = reviewService.patch(targetReview);

        // then
        verify(pointService).savePointHistory(any(), any(), anyInt());
        assertEquals(afterReview.getContent(), targetReview.getContent());
        assertThat(afterReview.getPhotos()).contains(newPhoto1, newPhoto2);

    }


    @DisplayName("WHEN delete review THEN all relational mappings to be null")
    @Test
    public void deleteReview() {
        // given
        place.getReviews().add(review);
        user.getReviews().add(review);
        given(reviewRepository.findByReviewId(any())).willReturn(Optional.of(review));
        doNothing().when(pointService).savePointHistory(any(), any(), anyInt());
        doNothing().when(reviewRepository).delete(any());

        // when
        reviewService.delete(review);

        // then
        verify(pointService).savePointHistory(any(), any(), anyInt());
        verify(reviewRepository).delete(any());
        assertTrue(review.getPhotos().isEmpty());
        assertNull(review.getPlace());
        assertNull(review.getUser());

    }

}
