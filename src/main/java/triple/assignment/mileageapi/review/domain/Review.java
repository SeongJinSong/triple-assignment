package triple.assignment.mileageapi.review.domain;

import lombok.*;
import org.hibernate.annotations.Type;
import triple.assignment.mileageapi.global.base.BaseTimeEntity;
import triple.assignment.mileageapi.global.error.ErrorCode;
import triple.assignment.mileageapi.global.error.exception.InvalidReviewException;
import triple.assignment.mileageapi.place.domain.Place;
import triple.assignment.mileageapi.review.controller.dto.ReviewResponse;
import triple.assignment.mileageapi.global.dto.enumerated.ActionType;
import triple.assignment.mileageapi.user.domain.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        indexes = @Index(name = "idx_review_uuid", columnList = "reviewId")
)
@Entity
public class Review extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Type(type = "org.hibernate.type.UUIDCharType")
    @Column(columnDefinition = "char(36)")
    private UUID reviewId;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Photo> photos = new ArrayList<>();

    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Transient
    private UUID placeId;

    @Transient
    private UUID userId;

    @Transient
    private ActionType actionType;

    @Transient
    private int point;

    public Review setPoint(int point) {
        this.point = point;
        return this;
    }


    public Review changeContent(String content) {
        this.content = content;
        return this;
    }

    /**
     * @throws InvalidReviewException (내용점수) + (사진점수) = 0 인 경우(둘 다 없는 경우) 예외 리턴
     * @return 리뷰의 내용 점수와 사진 점수를 합산한 결과를 리턴한다.
     */
    public int getContentAndPhotoPoint() {
        int sum = getContentPoint() + getPhotoPoint();
        if (sum == 0) {
            throw new InvalidReviewException(ErrorCode.INVALID_REVIEW_INFO);
        }
        return sum;
    }

    public int getPhotoPoint() {
        return getPhotos().isEmpty() ? 0 : 1;
    }

    public int getContentPoint() {
        return getContent().isBlank() ? 0 : 1;
    }


    public Review setUser(User user) {
        this.user = user;
        return this;
    }

    public Review setPlace(Place place) {
        this.place = place;
        return this;
    }

    public void addPhotos(List<Photo> photos) {
        if (this.photos == null) {
            this.photos = new ArrayList<>();
        }
        photos.forEach(e -> {
            e.setReview(this);
            this.photos.add(e);
        });
    }


    public void clearAllMappings() {
        clearPlace();
        clearUser();
        clearAllPhotos();
    }

    public void clearPlace() {
        this.place.getReviews().remove(this);
        this.place = null;
    }

    public void clearUser() {
        this.user.getReviews().remove(this);
        this.user = null;
    }
    public void clearAllPhotos() {
        getPhotos().forEach(Photo::clearReview);
        this.photos.clear();
    }


    public ReviewResponse toResponse() {
        return ReviewResponse.builder()
                .id(id)
                .reviewId(reviewId)
                .placeId(placeId)
                .userId(userId)
                .photos(
                        getPhotos().stream()
                                .map(Photo::getPhotoId)
                                .collect(Collectors.toList())
                )
                .content(content)
                .point(point)
                .createdAt(getCreatedAt())
                .lastModifiedAt(getModifiedAt())
                .build();
    }

}
