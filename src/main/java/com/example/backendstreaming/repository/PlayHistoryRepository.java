package com.example.backendstreaming.repository;

import com.example.backendstreaming.domain.PlayHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayHistoryRepository extends JpaRepository<PlayHistory, Long> {}