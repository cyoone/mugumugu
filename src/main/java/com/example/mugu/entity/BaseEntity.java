package com.example.mugu.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public class BaseEntity {
    @CreatedDate
    @Column(updatable = false, name = "created_at")
    private String createdAt;

    @CreatedDate
    @Column(updatable = true)
    private String updated_at;

    @PrePersist
    public void onPrePersist() {
        this.createdAt = String.valueOf(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        this.updated_at = String.valueOf(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
    }

    @PreUpdate
    public void onPreUpdate() {
        this.updated_at = String.valueOf(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
    }
}
