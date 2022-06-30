package triple.assignment.mileageapi.user.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import triple.assignment.mileageapi.point.domain.Point;
import triple.assignment.mileageapi.review.domain.Review;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private UUID userId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Point> points = new ArrayList<>();

    @Transient
    private Integer totalPoint;

    public int getPoint() {
        if (totalPoint != null) {
            return totalPoint;
        }
        totalPoint = points.stream()
                .map(Point::getScore)
                .reduce(Integer::sum)
                .orElse(0);
        return totalPoint;
    }
}
