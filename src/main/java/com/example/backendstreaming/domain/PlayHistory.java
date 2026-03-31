package com.example.backendstreaming.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// PlayHistory
@Data
@Entity
@Table(name = "play_history")
@AllArgsConstructor
@NoArgsConstructor
public class PlayHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_id", nullable = false)
    private Song song;

    @Column(name = "played_at", updatable = false)
    private LocalDateTime playedAt;

    @Min(0)
    @Column(name = "seconds_played")
    private Integer secondsPlayed = 0;

    @PrePersist
    protected void onCreate() { this.playedAt = LocalDateTime.now(); }
}