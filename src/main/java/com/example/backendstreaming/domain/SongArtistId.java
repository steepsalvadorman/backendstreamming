package com.example.backendstreaming.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class SongArtistId implements Serializable {
    @NotNull
    @Column(name = "song_id", nullable = false)
    private Integer songId;

    @NotNull
    @Column(name = "artist_id", nullable = false)
    private Integer artistId;


}