-- Base schema required before V2 feed-algorithm migration.

CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    bio VARCHAR(500),
    avatar_url VARCHAR(255),
    credits INTEGER NOT NULL DEFAULT 0,
    version BIGINT,
    joined_date DATE NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_email ON users (email);
CREATE INDEX IF NOT EXISTS idx_username ON users (username);

CREATE TABLE IF NOT EXISTS posts (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(100) NOT NULL,
    tagline VARCHAR(150) NOT NULL,
    category VARCHAR(255) NOT NULL,
    stage VARCHAR(255) NOT NULL,
    problem_description TEXT NOT NULL,
    solution_description TEXT NOT NULL,
    live_demo_url VARCHAR(255),
    is_boosted BOOLEAN NOT NULL DEFAULT FALSE,
    boosted_until DATE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_posts_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE INDEX IF NOT EXISTS idx_created_at ON posts (created_at);
CREATE INDEX IF NOT EXISTS idx_category_created ON posts (category, created_at);
CREATE INDEX IF NOT EXISTS idx_user_created_posts ON posts (user_id, created_at);

CREATE TABLE IF NOT EXISTS post_screenshot_urls (
    post_id BIGINT NOT NULL,
    screenshot_urls VARCHAR(255),
    CONSTRAINT fk_post_screenshot_urls_post FOREIGN KEY (post_id) REFERENCES posts (id)
);

CREATE INDEX IF NOT EXISTS idx_post_screenshot_urls_post_id ON post_screenshot_urls (post_id);

CREATE TABLE IF NOT EXISTS comments (
    id BIGSERIAL PRIMARY KEY,
    post_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    is_marked_helpful BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_comments_post FOREIGN KEY (post_id) REFERENCES posts (id),
    CONSTRAINT fk_comments_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE INDEX IF NOT EXISTS idx_post_created ON comments (post_id, created_at);

CREATE TABLE IF NOT EXISTS payments (
    id BIGSERIAL PRIMARY KEY,
    payment_intent_id VARCHAR(255) NOT NULL UNIQUE,
    idempotency_key VARCHAR(255) NOT NULL UNIQUE,
    type VARCHAR(255) NOT NULL DEFAULT 'PURCHASE_CREDITS',
    purchased_package VARCHAR(255) NOT NULL,
    amount_in_cents BIGINT NOT NULL,
    credits_awarded BOOLEAN DEFAULT FALSE,
    user_id BIGINT NOT NULL,
    status VARCHAR(255) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_payments_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE INDEX IF NOT EXISTS idx_usr_payment_id ON payments (user_id, id);
CREATE INDEX IF NOT EXISTS idx_payment_intent ON payments (payment_intent_id);
CREATE INDEX IF NOT EXISTS idx_idempotency_key ON payments (idempotency_key);

CREATE TABLE IF NOT EXISTS credit_transactions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    amount INTEGER NOT NULL,
    transaction_type VARCHAR(255) NOT NULL,
    related_post_id BIGINT,
    related_comment_id BIGINT,
    balance_after INTEGER NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_credit_transactions_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT uk_credit_txn_user_type_comment UNIQUE (user_id, transaction_type, related_comment_id)
);

CREATE INDEX IF NOT EXISTS idx_user_created_credit_transactions
    ON credit_transactions (user_id, created_at);

CREATE TABLE IF NOT EXISTS refresh_tokens (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(255) NOT NULL UNIQUE,
    expiry_date TIMESTAMP WITH TIME ZONE NOT NULL,
    revoked BOOLEAN NOT NULL,
    user_id BIGINT NOT NULL UNIQUE,
    CONSTRAINT fk_refresh_tokens_user FOREIGN KEY (user_id) REFERENCES users (id)
);
