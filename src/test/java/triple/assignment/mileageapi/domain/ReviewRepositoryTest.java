package triple.assignment.mileageapi.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import triple.assignment.mileageapi.place.domain.Place;
import triple.assignment.mileageapi.place.domain.PlaceRepository;
import triple.assignment.mileageapi.review.domain.Review;
import triple.assignment.mileageapi.review.domain.ReviewRepository;
import triple.assignment.mileageapi.user.domain.User;
import triple.assignment.mileageapi.user.domain.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
public class ReviewRepositoryTest {
    @PersistenceContext
    private EntityManager em;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private ReviewRepository reviewRepository;


    @DisplayName("retrieve with valid parameter and then get true")
    @Test
    void existsByUserAndPlace_true() {
        // given
        final User user = userRepository.save(User.builder().userId(UUID.randomUUID()).build());
        final Place place = placeRepository.save(Place.builder().placeId(UUID.randomUUID()).build());
        final Review review = reviewRepository.save(
                Review.builder().reviewId(UUID.randomUUID()).place(place).user(user).content("test-content").build());
        em.flush();
        em.clear();
        // when
        boolean hasReview = reviewRepository.existsByUserAndPlace(user, place);
        // then
        assertTrue(hasReview);
    }


    @DisplayName("retrieve with invalid user and then get false")
    @Test
    void existsByUserAndPlace_false() {
        // given
        final User user = userRepository.save(User.builder().userId(UUID.randomUUID()).build());
        final User invalidUser = userRepository.save(User.builder().userId(UUID.randomUUID()).build());
        final Place place = placeRepository.save(Place.builder().placeId(UUID.randomUUID()).build());
        final Review review = reviewRepository.save(
                Review.builder().reviewId(UUID.randomUUID()).place(place).user(user).content("test-content").build());
        em.flush();
        em.clear();
        // when
        boolean hasReview = reviewRepository.existsByUserAndPlace(invalidUser, place);
        // then
        assertFalse(hasReview);
    }


    @Test
    void findByReviewId() {
        // given
        final User user = userRepository.save(User.builder().userId(UUID.randomUUID()).build());
        final Place place = placeRepository.save(Place.builder().placeId(UUID.randomUUID()).build());
        final Review review = reviewRepository.save(
                Review.builder().reviewId(UUID.randomUUID()).place(place).user(user).content("test-content").build());
        // when
        Review findReview = reviewRepository.findByReviewId(review.getReviewId()).orElseThrow();
        // then
        assertEquals(review.getReviewId(), findReview.getReviewId());
    }

}
