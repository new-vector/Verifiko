package com.verifico.server.feed_algorithm.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_seen_videos", indexes = {
    @Index(name = "idx_seen_user_served", columnList = "user_id,served_at"),
    @Index(name = "idx_seen_user_session", columnList = "user_id,session_id")
})
public class UserSeenVideo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "video_id", nullable = false)
    private Long videoId;

    @Column(name = "session_id", nullable = false, length = 80)
    private String sessionId;

    @Column(name = "served_at", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant servedAt = Instant.now();

    public UserSeenVideo() {
    }

    public UserSeenVideo(Long userId, Long videoId, String sessionId, Instant servedAt) {
        this.userId = userId;
        this.videoId = videoId;
        this.sessionId = sessionId;
        this.servedAt = servedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getVideoId() {
        return videoId;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Instant getServedAt() {
        return servedAt;
    }

    public void setServedAt(Instant servedAt) {
        this.servedAt = servedAt;
    }
}
