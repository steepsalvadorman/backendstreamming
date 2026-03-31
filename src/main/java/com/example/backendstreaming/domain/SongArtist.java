package com.example.backendstreaming.domain;

import com.example.backendstreaming.domain.enums.ArtistRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Entity
@Data
@Entity
@Table(name = "song_artists")
@AllArgsConstructor
@NoArgsConstructor
public class SongArtist {

    @EmbeddedId
    private SongArtistId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("songId")
    @JoinColumn(name = "song_id")
    private Song song;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("artistId")
    @JoinColumn(name = "artist_id")
    private Artist artist;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ArtistRole role = ArtistRole.MAIN;
}