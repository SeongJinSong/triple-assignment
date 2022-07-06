package triple.assignment.mileageapi.point.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import triple.assignment.mileageapi.point.controller.dto.PointDto;
import triple.assignment.mileageapi.user.domain.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Type(type = "org.hibernate.type.UUIDCharType")
    @Column(columnDefinition = "char(36)")
    private UUID reviewId;

    @Column(updatable = false)
    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private LocalDateTime createdAt;

    public PointDto toDto() {
        return PointDto.builder()
                .reviewId(reviewId)
                .score(score)
                .createdAt(getCreatedAt())
                .build();
    }

    public void setUser(User user) {
        this.user = user;
    }
}
