package triple.assignment.mileageapi.review.domain;

import lombok.*;
import org.hibernate.annotations.Type;
import triple.assignment.mileageapi.global.base.BaseTimeEntity;
import triple.assignment.mileageapi.place.domain.Place;
import triple.assignment.mileageapi.review.controller.dto.ReviewResponse;
import triple.assignment.mileageapi.review.domain.enumerated.ActionType;
import triple.assignment.mileageapi.user.domain.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Place place;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Transient
    private UUID placeId;

    @Transient
    private UUID userId;

    @Transient
    private ActionType actionType;

    public Review setUser(User user) {
        this.user = user;
        return this;
    }

    public Review setPlace(Place place) {
        this.place = place;
        return this;
    }

    public int getPhotoPoint() {
        return getPhotos().isEmpty() ? 0 : 1;
    }

    public int getContentPoint() {
        return getContent().isEmpty() ? 0 : 1;
    }


    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
        photos.forEach(e -> e.setReview(this));
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
                .createdAt(getCreatedAt())
                .lastModifiedAt(getLastModifiedAt())
                .build();
    }

    public Review changeContent(String content) {
        this.content = content;
        return this;
    }

    public void clearAllPhotos() {
        getPhotos().forEach(Photo::clearReview);
        this.photos.clear();
    }

    public void addPhotos(List<Photo> photos) {
        photos.forEach(e -> {
            e.setReview(this);
            this.photos.add(e);
        });
    }
}
