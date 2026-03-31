package com.example.backendstreaming.services;

import com.example.backendstreaming.domain.Song;
import com.example.backendstreaming.repository.SongRepository;
import lombok.AllArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@AllArgsConstructor
public class SongStreamService {

    private final SongRepository songRepository;

    // ─────────────────────────────────────────────
    // Obtener recurso de audio
    // ─────────────────────────────────────────────
    private UrlResource getAudioResource(Long id) throws IOException {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Canción no encontrada"));

        UrlResource audio = new UrlResource(Paths.get(song.getAudioUrl()).toUri());

        if (!audio.exists() || !audio.isReadable()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Archivo de audio no encontrado");
        }

        return audio;
    }

    // ─────────────────────────────────────────────
    // Streaming con RANGE
    // ─────────────────────────────────────────────
    public ResourceRegion getAudioRegion(Long id, HttpHeaders headers) throws IOException {
        UrlResource audio = getAudioResource(id);
        long contentLength = audio.contentLength();
        long chunkSize = 1024 * 1024; // 1 MB

        return headers.getRange().stream()
                .findFirst()
                .map(range -> {
                    long start = range.getRangeStart(contentLength);
                    long end = range.getRangeEnd(contentLength);
                    long rangeLength = Math.min(chunkSize, end - start + 1);
                    return new ResourceRegion(audio, start, rangeLength);
                })
                .orElse(new ResourceRegion(audio, 0, Math.min(chunkSize, contentLength)));
    }

    // ─────────────────────────────────────────────
    // Tipo MIME (forzar por extensión si es necesario)
    // ─────────────────────────────────────────────
    public MediaType getMediaType(Long id) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Canción no encontrada"));

        Path path = Paths.get(song.getAudioUrl());
        String fileName = path.getFileName().toString().toLowerCase();

        if (fileName.endsWith(".flac")) return MediaType.parseMediaType("audio/flac");
        if (fileName.endsWith(".mp3"))  return MediaType.parseMediaType("audio/mpeg");
        if (fileName.endsWith(".wav"))  return MediaType.parseMediaType("audio/wav");

        return MediaType.APPLICATION_OCTET_STREAM;
    }

    // ─────────────────────────────────────────────
    // Tamaño total
    // ─────────────────────────────────────────────
    public long getContentLength(Long id) throws IOException {
        return getAudioResource(id).contentLength();
    }
}