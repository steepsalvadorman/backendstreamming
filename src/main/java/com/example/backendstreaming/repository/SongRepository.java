package com.example.backendstreaming.repository;

import com.example.backendstreaming.domain.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    boolean existsByAudioUrl(String audioUrl);
}