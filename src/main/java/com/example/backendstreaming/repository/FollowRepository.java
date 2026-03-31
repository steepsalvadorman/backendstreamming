package com.example.backendstreaming.repository;


import com.example.backendstreaming.domain.Follow;
import com.example.backendstreaming.domain.FollowId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow, FollowId> {
}
