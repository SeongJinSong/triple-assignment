package triple.assignment.mileageapi.review.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import triple.assignment.mileageapi.place.domain.Place;
import triple.assignment.mileageapi.point.domain.Point;
import triple.assignment.mileageapi.user.domain.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private UUID reviewId;

    @OneToMany(mappedBy = "review")
    private List<Photo> photos = new ArrayList<>();

    @Lob
    private String content;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "place_id")
    private Place place;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
