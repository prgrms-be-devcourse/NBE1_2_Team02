-- Concert 테이블 생성
CREATE TABLE concert (
                         concert_id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- ID 필드, 기본 키
                         title VARCHAR(255) NOT NULL,                   -- 제목
                         total_stock INT NOT NULL,                      -- 총 재고
                         start_date DATE NOT NULL,                      -- 시작 날짜
                         end_date DATE NOT NULL,                        -- 종료 날짜
                         price INT NOT NULL,                            -- 가격
                         start_hour INT NOT NULL,                             -- 시작 시간 (시간)
                         reservation_start_at TIMESTAMP, -- 예약 가능 시간
                         created_at TIMESTAMP,                          -- 생성 시간 (BaseEntity에서 상속된 필드)
                         updated_at TIMESTAMP                           -- 수정 시간 (BaseEntity에서 상속된 필드)
);

-- LikeConcert 테이블 생성 (Concert와 1:N 관계)
CREATE TABLE like_concert (
                              like_concert_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              concert_id BIGINT,
                              FOREIGN KEY (concert_id) REFERENCES concert(concert_id) ON DELETE CASCADE
);

-- Review 테이블 생성 (Concert와 1:N 관계)
CREATE TABLE review (
                        review_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        content TEXT,
                        rating INT,
                        concert_id BIGINT,
                        FOREIGN KEY (concert_id) REFERENCES concert(concert_id) ON DELETE CASCADE
);

-- Seat 테이블 생성 (Concert와 1:N 관계)
CREATE TABLE seat (
                      seat_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      concert_id BIGINT,
                      seat_number INT,  -- 좌석 번호 (가정)
                      FOREIGN KEY (concert_id) REFERENCES concert(concert_id) ON DELETE CASCADE
);
