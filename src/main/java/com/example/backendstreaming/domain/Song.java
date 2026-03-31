package com.example.backendstreaming.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Data
@Entity
@Table(name = "songs")
@AllArgsConstructor
@NoArgsConstructor
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 255)
    private String title;

    @NotNull(message = "La duración es obligatoria")
    @Positive(message = "La duración debe ser mayor a 0")
    private Integer duration;

    @NotBlank(message = "La URL del audio es obligatoria")
    @Column(name = "audio_url", nullable = false)
    private String audioUrl;

    @Column(name = "track_number")
    @Min(value = 1)
    private Integer trackNumber;

    @Column(name = "play_count")
    private Integer playCount = 0;

    @Column(name = "is_explicit")
    private Boolean isExplicit = false;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // Relación con Album (FK album_id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id")
    private Album album;

    // Relación con artistas via song_artists
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "song_artists",
            joinColumns = @JoinColumn(name = "song_id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id")
    )
    private Set<Artist> artists = new HashSet<>(); // valor por defecto

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}