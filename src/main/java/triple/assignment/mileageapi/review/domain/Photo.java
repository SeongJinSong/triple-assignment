package triple.assignment.mileageapi.review.domain;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID photoId;

    @ManyToOne
    @JoinColumn(name = "review_id")
    private Review review;

}
