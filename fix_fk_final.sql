-- User 삭제가 가능하도록 외래 키를 ON DELETE CASCADE로 재설정
USE `street-ball`;

-- 1. participation 테이블의 외래 키 재설정
ALTER TABLE participation DROP FOREIGN KEY FKfputwcduinudasn7es02c12ra;
ALTER TABLE participation
ADD CONSTRAINT fk_participation_user
FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE;

-- 2. review 테이블의 외래 키 재설정
ALTER TABLE review DROP FOREIGN KEY FKiyf57dy48lyiftdrf7y87rnxi;
ALTER TABLE review
ADD CONSTRAINT fk_review_user
FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE;

-- 완료 메시지
SELECT 'Foreign keys updated successfully!' AS Result;
