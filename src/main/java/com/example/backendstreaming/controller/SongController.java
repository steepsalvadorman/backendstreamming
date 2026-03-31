package com.example.backendstreaming.controller;

import com.example.backendstreaming.domain.Song;
import com.example.backendstreaming.services.SongService;
import com.example.backendstreaming.services.SongStreamService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/songs")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class SongController {

    private final SongService songService;
    private final SongStreamService songStreamService;

    // ─────────────────────────────────────────────
    // Subir canción
    // ─────────────────────────────────────────────
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Song> uploadSong(
            @RequestPart("title") String title,
            @RequestPart("duration") String duration,
            @RequestPart(value = "trackNumber", required = false) String trackNumber,
            @RequestPart(value = "isExplicit", required = false) String isExplicit,
            @RequestPart(value = "albumId", required = false) String albumId,
            @RequestPart("audio") MultipartFile audioFile) throws IOException {

        Song saved = songService.uploadSong(
                title,
                Integer.parseInt(duration),
                trackNumber != null ? Integer.parseInt(trackNumber) : null,
                isExplicit != null ? Boolean.parseBoolean(isExplicit) : false,
                albumId != null ? Long.parseLong(albumId) : null,
                audioFile
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // ─────────────────────────────────────────────
    // Eliminar canción
    // ─────────────────────────────────────────────
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable Long id) throws IOException {
        songService.deleteSong(id);
        return ResponseEntity.noContent().build();
    }

    // ─────────────────────────────────────────────
    // Streaming de audio
    // ─────────────────────────────────────────────
    @GetMapping("/{id}/stream")
    public ResponseEntity<ResourceRegion> streamAudio(
            @PathVariable Long id,
            @RequestHeader HttpHeaders headers) throws IOException {

        ResourceRegion region = songStreamService.getAudioRegion(id, headers);
        MediaType mediaType = songStreamService.getMediaType(id);

        long contentLength = region.getCount();
        long start = region.getPosition();
        long end = start + contentLength - 1;
        long total = songStreamService.getContentLength(id);

        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .contentType(mediaType)
                .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(contentLength))
                .header(HttpHeaders.CONTENT_RANGE, "bytes " + start + "-" + end + "/" + total)
                .header(HttpHeaders.CACHE_CONTROL, "no-cache")
                .body(region);
    }


    @GetMapping
    public ResponseEntity<List<Song>> getAllSongs() {
        List<Song> songs = songService.getAllSongs(); // tu servicio debe devolver la lista completa
        return ResponseEntity.ok(songs);
    }
}