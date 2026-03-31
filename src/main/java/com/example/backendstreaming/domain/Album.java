package com.example.backendstreaming.domain;

import com.example.backendstreaming.domain.enums.AlbumType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "albums")
@AllArgsConstructor
@NoArgsConstructor
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String genre;

    @NotBlank
    private String language;

    @Column(name = "cover_url")
    private String coverUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "album_type", nullable = false)
    private AlbumType albumType = AlbumType.ALBUM;
}