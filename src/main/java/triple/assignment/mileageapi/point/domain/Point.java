package triple.assignment.mileageapi.point.domain;

import lombok.*;
import triple.assignment.mileageapi.review.domain.Review;
import triple.assignment.mileageapi.user.domain.User;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private UUID reviewId;
}
