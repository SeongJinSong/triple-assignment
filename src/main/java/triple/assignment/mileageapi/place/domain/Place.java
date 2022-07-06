package triple.assignment.mileageapi.place.domain;

import lombok.*;
import org.hibernate.annotations.Type;
import triple.assignment.mileageapi.global.base.BaseTimeEntity;
import triple.assignment.mileageapi.review.domain.Review;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        indexes = @Index(name = "idx_place_uuid", columnList = "placeId")
)
@Entity
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Type(type = "org.hibernate.type.UUIDCharType")
    @Column(columnDefinition = "char(36)")
    private UUID placeId;

    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    public boolean hasReview() {
        return !reviews.isEmpty();
    }


    public int getFirstReviewPoint() {
        return hasReview() ? 0 : 1;
    }

    public boolean isFirstReview(UUID reviewId) {
        return this.reviews.stream()
                .min(Comparator.comparing(BaseTimeEntity::getCreatedAt))
                .orElseThrow()
                .getReviewId().equals(reviewId);
    }

    public void addReview(Review review) {
        getReviews().add(review);
        review.setPlace(this);
    }
}
