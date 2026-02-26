package com.verifico.server.feed_algorithm.model;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name = "feed_nodes", indexes = {
    @Index(name = "idx_feed_nodes_active_created", columnList = "active,created_at")
})
public class FeedNode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 1500)
    private String description;

    @Column(name = "video_url", nullable = false, length = 1200)
    private String videoUrl;

    @Column(name = "tags_csv", length = 800)
    private String tagsCsv;

    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant createdAt = Instant.now();

    public FeedNode() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getTagsCsv() {
        return tagsCsv;
    }

    public void setTagsCsv(String tagsCsv) {
        this.tagsCsv = tagsCsv;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public List<String> getTags() {
        if (tagsCsv == null || tagsCsv.isBlank()) {
            return List.of();
        }
        return Arrays.stream(tagsCsv.split(","))
            .map(String::trim)
            .filter(tag -> !tag.isBlank())
            .toList();
    }

    public void setTags(List<String> tags) {
        this.tagsCsv = String.join(",", tags);
    }
}
