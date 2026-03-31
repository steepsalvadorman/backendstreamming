package com.example.backendstreaming.services;

import com.example.backendstreaming.domain.Album;
import com.example.backendstreaming.domain.Song;
import com.example.backendstreaming.repository.SongRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class SongService {

    private SongRepository songRepository;

    private AudioStorageService audioStorageService;

    public Song uploadSong(String title, Integer duration, Integer trackNumber,
                           Boolean isExplicit, Long albumId, MultipartFile audioFile) throws IOException {

        String audioUrl = audioStorageService.saveAudio(audioFile);

        Song song = new Song();
        song.setTitle(title);
        song.setDuration(duration);
        song.setTrackNumber(trackNumber);
        song.setIsExplicit(isExplicit != null ? isExplicit : false);
        song.setAudioUrl(audioUrl);
        song.setPlayCount(0);

        // Si viene albumId lo seteamos
        if (albumId != null) {
            Album album = new Album();
            album.setId(albumId);
            song.setAlbum(album);
        }

        return songRepository.save(song);
    }

    public void deleteSong(Long id) throws IOException {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Canción no encontrada"));

        audioStorageService.deleteAudio(song.getAudioUrl());
        songRepository.delete(song);
    }

    public List<Song> getAllSongs() {
        return songRepository.findAll(); // si usas JPA
    }
}