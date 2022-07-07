# triple-assignment


## 0. 개발 환경



- JAVA 11
- Spring Boot v2.6.4
- Gradle v7.4.1
- MySQL v5.7

## 1. DDL / ERD


### - **엔티티 관계 다이어그램**

![ERD](https://user-images.githubusercontent.com/58846192/177761847-095b2ca5-134f-4ccb-a5fa-730ee442138e.png)


#

### - **TABLE DDL**

```sql
create table place
(
    id       bigint auto_increment primary key,
    place_id char(36) not null
);


create table users
(
    id      bigint auto_increment primary key,
    user_id char(36) not null
);


create table point
(
    id          bigint auto_increment primary key,
    action_type varchar(255) null,
    created_at  datetime null,
    review_id   char(36) null,
    score       int not null,
    user_id     bigint null,
    constraint fk_point_user_id foreign key (user_id) references users (id)
);


create table review
(
    id          bigint auto_increment primary key,
    created_at  datetime null,
    modified_at datetime null,
    content     longtext null,
    review_id   char(36) not null,
    place_id    bigint   not null,
    user_id     bigint   not null,
    constraint fk_review_user_id foreign key (user_id) references users (id),
    constraint fk_review_place_id foreign key (place_id) references place (id)
);


create table photo
(
    id         bigint auto_increment primary key,
    created_at datetime null,
    photo_id   char(36) null,
    review_id  bigint null,
    constraint fk_photo_review_id foreign key (review_id) references review (id)
);
```

#

### - **INDEX DDL**

```sql
create index idx_review_uuid on review (review_id); -- review uuid

create index idx_photo_uuid on photo (photo_id); -- photo uuid

create index idx_place_uuid on place (place_id); -- place uuid

create index idx_users_uuid on users (user_id); -- user uuid
```

MySQL InnoDB 에서는 FK에 대해 자동 인덱스를 생성하기 때문에 나머지 검색조건에 대해서만 설정    

#


## 2. 애플리케이션 실행 방법



- 프로젝트 홈 경로에서 (docker-compose.yml이 위치한 경로) 터미널을 열고, 다음 명령어 수행

```bash
# 앱 컨테이너 로딩 완료 후에도 db 컨테이너와의 연결에 시간이 수 초 걸릴 수 있습니다
$ docker-compose up -d  # (host port : 8080)
```

```bash
# mysql mode의 h2-inmemory db로도 테스트 가능합니다 (ide로 project open -> run applicaiton)
```

#

- api 테스트를 위해 필요한 초기 데이터는 과제 PDF의 예제 데이터 형식을 바탕으로 생성

```bash
insert into place(place_id) values ('2e4baf1c-5acb-4efb-a1af-eddada31b00f');
insert into users(user_id) values ('3ede0ef2-92b7-4817-a5f3-0c575361f745');
```

(**위 스크립트는 애플리케이션 시작 시점에 자동 수행)**

#

## 3. API 명세 및 테스트 예시



아래의 엔드포인트와 HTTP 메서드, 요청 본문으로 테스트 가능

- api 명세 간단 요약

| Method | Request URI | Description |
| --- | --- | --- |
| POST | /events | 리뷰 이벤트 발생 api |
| GET | /users/{user-id}/points/summary | 유저의 현재 포인트 조회 |
| GET | /users/{user-id}/points/history | 유저의 전체 포인트 기록 조회 |

#

### **1) 리뷰 이벤트 발생 API**

리뷰 작성/수정/삭제 시 발생하는 이벤트 핸들링을 위한 엔드포인트

#

**1-1 리뷰 생성 이벤트**

```json
// POST  localhost:8080/events

{
    "type": "REVIEW",
    "action": "ADD",
    "reviewId": "240a0658-dc5f-4878-9381-ebb7b2667772",
    "content": "좋아요!",
    "attachedPhotoIds": [
        "e4d1a64e-a531-46de-88d0-ff0ed70c0bb8",
        "afb0cef2-851d-4a50-bb07-9cc15cbdc332"
    ],
    "userId": "3ede0ef2-92b7-4817-a5f3-0c575361f745",
    "placeId": "2e4baf1c-5acb-4efb-a1af-eddada31b00f"
}

----------------------- ↓ response ↓ -----------------------

{
    "status": 200,
    "message": "handle review create event success.",
    "data": {
        "id": 1,
        "userId": "3ede0ef2-92b7-4817-a5f3-0c575361f745",
        "reviewId": "240a0658-dc5f-4878-9381-ebb7b2667772",
        "placeId": "2e4baf1c-5acb-4efb-a1af-eddada31b00f",
        "content": "좋아요!",
        "photos": [
            "e4d1a64e-a531-46de-88d0-ff0ed70c0bb8",
            "afb0cef2-851d-4a50-bb07-9cc15cbdc332"
        ],
        "point": 3,
        "createdAt": "2022-07-07 01:34:16",
        "lastModifiedAt": "2022-07-07 01:34:16"
    }
}
```

#

**1-2 리뷰 수정 이벤트**

```json
POST  localhost:8080/events

{
    "type": "REVIEW",
    "action": "MOD",
    "reviewId": "240a0658-dc5f-4878-9381-ebb7b2667772",
    "content": "싫어요!",
    "attachedPhotoIds": [
    ],
    "userId": "3ede0ef2-92b7-4817-a5f3-0c575361f745",
    "placeId": "2e4baf1c-5acb-4efb-a1af-eddada31b00f"
}

----------------------- ↓ response ↓ -----------------------

{
    "status": 200,
    "message": "handle review patch event success.",
    "data": {
        "id": 1,
        "userId": "3ede0ef2-92b7-4817-a5f3-0c575361f745",
        "reviewId": "240a0658-dc5f-4878-9381-ebb7b2667772",
        "placeId": "2e4baf1c-5acb-4efb-a1af-eddada31b00f",
        "content": "싫어요!",
        "photos": [
        ],
        "point": -1,
        "createdAt": "2022-07-07 01:34:16",
        "lastModifiedAt": "2022-07-07 01:34:19"
    }
}
```

- point: 이전과 비교해 발생한 포인트의 증감분을 표시

#

**1-3 리뷰 삭제 이벤트**

```json
POST  localhost:8080/events

{
    "type": "REVIEW",
    "action": "DELETE",
    "reviewId": "240a0658-dc5f-4878-9381-ebb7b2667772",
    "content": "1",
    "attachedPhotoIds": [
        "4e4baf1c-5acb-4efb-a1af-eddada31b00f",
        "3ede0ef2-92b7-4817-a5f3-0c575361f746"
    ],
    "userId": "3ede0ef2-92b7-4817-a5f3-0c575361f745",
    "placeId": "2e4baf1c-5acb-4efb-a1af-eddada31b00f"
}

----------------------- ↓ response ↓ -----------------------

{
    "status": 200,
    "message": "handle review delete event success.",
    "data": null
}
```

#

### **2) 유저 포인트 조회 API**

유저가 현재 보유한 포인트 조회를 위한 엔드포인트
#


**2-1 유저의 현재 포인트 조회 서비스**

```json
GET  localhost:8080/users/3ede0ef2-92b7-4817-a5f3-0c575361f745/points/summary

----------------------- ↓ response ↓ -----------------------

{
    "status": 200,
    "message": "get user total point success",
    "data": {
        "userId": "3ede0ef2-92b7-4817-a5f3-0c575361f745",
        "totalPoint": 2
    }
```

#

**2-2 유저의 전체 포인트 변경 내역 조회 서비스**

- 필요 시 쿼리 파라미터로 페이징 처리 가능

| page | size | sort |
| --- | --- | --- |
| 페이지 넘버 | 페이지 사이즈 | 정렬 기준 |
- ex) ~/history?page=2&size=20&sort=createdAt,asc
- 별도의 쿼리 파라미터가 없는경우, 20개 조회

```json
GET  localhost:8080/users/3ede0ef2-92b7-4817-a5f3-0c575361f745/points/history

----------------------- ↓ response ↓ -----------------------

{
    "status": 200,
    "message": "get user point history success",
    "data": {
        "userId": "3ede0ef2-92b7-4817-a5f3-0c575361f745",
        "pointHistory": [
            {
                "reviewId": "240a0658-dc5f-4878-9381-ebb7b2667772",
                "actionType": "ADD",
                "score": 3,
                "createdAt": "2022-07-06 17:27:45"
            },
            {
                "reviewId": "240a0658-dc5f-4878-9381-ebb7b2667772",
                "actionType": "MOD",
                "score": 0,
                "createdAt": "2022-07-06 17:27:50"
            },
            {
                "reviewId": "240a0658-dc5f-4878-9381-ebb7b2667772",
                "actionType": "MOD",
                "score": -1,
                "createdAt": "2022-07-06 17:27:57"
            },
            {
                "reviewId": "240a0658-dc5f-4878-9381-ebb7b2667772",
                "actionType": "DELETE",
                "score": -2,
                "createdAt": "2022-07-06 17:30:33"
            }
        ]
    }
}
```

#
