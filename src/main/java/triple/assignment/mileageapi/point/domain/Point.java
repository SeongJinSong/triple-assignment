package triple.assignment.mileageapi.point.domain;

import lombok.*;
import org.hibernate.annotations.Type;
import triple.assignment.mileageapi.point.controller.dto.PointDto;
import triple.assignment.mileageapi.global.base.BaseTimeEntity;
import triple.assignment.mileageapi.user.domain.User;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Point extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Type(type = "org.hibernate.type.UUIDCharType")
    @Column(columnDefinition = "char(36)")
    private UUID reviewId;

    public PointDto toDto() {
        return PointDto.builder()
                .reviewId(reviewId)
                .score(score)
                .createdAt(getCreatedAt())
                .build();
    }
}
