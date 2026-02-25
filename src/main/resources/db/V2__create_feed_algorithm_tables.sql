-- Feed content nodes
CREATE TABLE IF NOT EXISTS feed_nodes (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description VARCHAR(1500),
    video_url VARCHAR(1200) NOT NULL,
    tags_csv VARCHAR(800),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_feed_nodes_active_created
    ON feed_nodes (active, created_at);

CREATE TABLE IF NOT EXISTS user_tag_preferences (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    tag VARCHAR(100) NOT NULL,
    positive_count BIGINT NOT NULL DEFAULT 0,
    negative_count BIGINT NOT NULL DEFAULT 0,
    CONSTRAINT uq_user_tag_preferences_user_tag UNIQUE (user_id, tag)
);

CREATE TABLE IF NOT EXISTS user_seen_videos (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    video_id BIGINT NOT NULL,
    session_id VARCHAR(80) NOT NULL,
    served_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_seen_user_served
    ON user_seen_videos (user_id, served_at);

CREATE INDEX IF NOT EXISTS idx_seen_user_session
    ON user_seen_videos (user_id, session_id);

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'fk_user_tag_preferences_user'
    ) THEN
        ALTER TABLE user_tag_preferences
            ADD CONSTRAINT fk_user_tag_preferences_user
            FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'fk_user_seen_videos_user'
    ) THEN
        ALTER TABLE user_seen_videos
            ADD CONSTRAINT fk_user_seen_videos_user
            FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'fk_user_seen_videos_video'
    ) THEN
        ALTER TABLE user_seen_videos
            ADD CONSTRAINT fk_user_seen_videos_video
            FOREIGN KEY (video_id) REFERENCES feed_nodes (id) ON DELETE CASCADE;
    END IF;
END $$;
