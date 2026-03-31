package com.example.backendstreaming.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistSongId implements Serializable {
    @NotNull
    @Column(name = "playlist_id", nullable = false)
    private Integer playlistId;

    @NotNull
    @Column(name = "song_id", nullable = false)
    private Integer songId;


}