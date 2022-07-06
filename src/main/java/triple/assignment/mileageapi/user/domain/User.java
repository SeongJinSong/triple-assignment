package triple.assignment.mileageapi.user.domain;

import lombok.*;
import org.hibernate.annotations.Type;
import triple.assignment.mileageapi.point.domain.Point;
import triple.assignment.mileageapi.global.base.BaseTimeEntity;
import triple.assignment.mileageapi.review.domain.Review;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "USERS",
        indexes = @Index(name = "idx_users_uuid", columnList = "userId")
)
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Type(type = "org.hibernate.type.UUIDCharType")
    @Column(columnDefinition = "char(36)")
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

    public void addReview(Review review) {
        getReviews().add(review);
        review.setUser(this);
    }

    public void addPoint(Point point) {
        if (this.points == null) {
            this.points = new ArrayList<>();
        }
        this.points.add(point);
        point.setUser(this);
    }
}
