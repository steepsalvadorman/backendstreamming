package com.example.backendstreaming.repository;

import com.example.backendstreaming.domain.SongArtist;
import com.example.backendstreaming.domain.SongArtistId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongArtistRepository extends JpaRepository<SongArtist, SongArtistId> {}

