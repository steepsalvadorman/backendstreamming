package com.example.backendstreaming.repository;

import com.example.backendstreaming.domain.PlaylistSong;
import com.example.backendstreaming.domain.PlaylistSongId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaylistSongRepository extends JpaRepository<PlaylistSong, PlaylistSongId> {}
