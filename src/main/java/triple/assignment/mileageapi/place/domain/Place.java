package triple.assignment.mileageapi.place.domain;

import lombok.*;
import triple.assignment.mileageapi.review.domain.Review;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private UUID placeId;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    public boolean hasReview() {
        return !reviews.isEmpty();
    }


    public int getFirstReviewPoint() {
        return hasReview() ? 0 : 1;
    }
}