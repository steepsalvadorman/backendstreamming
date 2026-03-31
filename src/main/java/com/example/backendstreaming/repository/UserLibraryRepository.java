package com.example.backendstreaming.repository;


import com.example.backendstreaming.domain.UserLibrary;
import com.example.backendstreaming.domain.UserLibraryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLibraryRepository extends JpaRepository<UserLibrary, UserLibraryId> {}
