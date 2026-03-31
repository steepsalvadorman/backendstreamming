package com.example.backendstreaming.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "user_library")
@AllArgsConstructor
@NoArgsConstructor
public class UserLibrary {

    @EmbeddedId
    private UserLibraryId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("songId")
    @JoinColumn(name = "song_id")
    private Song song;

    @Column(name = "saved_at", updatable = false)
    private LocalDateTime savedAt;

    @PrePersist
    protected void onCreate() { this.savedAt = LocalDateTime.now(); }
}