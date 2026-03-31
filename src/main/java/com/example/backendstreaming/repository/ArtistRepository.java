package com.example.backendstreaming.repository;

import com.example.backendstreaming.domain.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {
    Optional<Artist> findByName(String name);
    // Búsqueda insensible a mayúsculas/minúsculas
    Optional<Artist> findByNameIgnoreCase(String name);
}
