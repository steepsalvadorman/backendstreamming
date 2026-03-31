package com.example.backendstreaming.services;

import com.example.backendstreaming.domain.Artist;
import com.example.backendstreaming.domain.Song;
import com.example.backendstreaming.repository.ArtistRepository;
import com.example.backendstreaming.repository.SongRepository;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

@Service
public class MusicScannerService {

    private final SongRepository songRepository;
    private final ArtistRepository artistRepository;

    public MusicScannerService(SongRepository songRepository, ArtistRepository artistRepository) {
        this.songRepository = songRepository;
        this.artistRepository = artistRepository;
    }

    @Value("${app.audio.path}")
    private String musicPath;

    @PostConstruct
    public void scanMusicFolder() {
        System.out.println("📁 Escaneando: " + musicPath);

        try {
            Files.walk(Paths.get(musicPath))  // recorre subcarpetas
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().toLowerCase().endsWith(".flac"))
                    .forEach(this::processFile);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processFile(Path path) {
        try {
            String absolutePath = path.toAbsolutePath().toString();

            // Evitar duplicados
            if (songRepository.existsByAudioUrl(absolutePath)) return;

            AudioFile audioFile = AudioFileIO.read(path.toFile());
            int duration = audioFile.getAudioHeader().getTrackLength();
            Tag tag = audioFile.getTag();

            String title = path.getFileName().toString();
            String tagArtist = null;

            if (tag != null) {
                String tagTitle = tag.getFirst(FieldKey.TITLE);
                if (tagTitle != null && !tagTitle.isEmpty()) {
                    title = tagTitle;
                }
                tagArtist = tag.getFirst(FieldKey.ARTIST);
            }

            // Crear canción
            Song song = new Song();
            song.setTitle(title);
            song.setDuration(duration);
            song.setArtists(resolveArtists(tagArtist));
            song.setAudioUrl(absolutePath);
            song.setPlayCount(0);

            // Solo crear el recurso si lo necesitas para streaming
            new UrlResource(Paths.get(song.getAudioUrl()).toUri());

            songRepository.save(song); // Hibernate persiste song y asocia artistas existentes

            System.out.println("🎵 Agregada: " + title);

        } catch (Exception e) {
            System.out.println("Error procesando: " + path);
            e.printStackTrace();
        }
    }

    // ✅ Método privado para manejar artistas y persistir los nuevos
    private Set<Artist> resolveArtists(String tagArtist) {
        Set<Artist> artists = new HashSet<>();
        if (tagArtist == null || tagArtist.isEmpty()) return artists;

        String[] names = tagArtist.split(";");
        for (String rawName : names) {
            String name = rawName.trim();

            Artist artist = artistRepository.findByName(name)
                    .orElseGet(() -> {
                        Artist newArtist = new Artist();
                        newArtist.setName(name);
                        newArtist.setOrigin("Desconocido");
                        return artistRepository.save(newArtist); // guardado antes de agregar
                    });

            artists.add(artist); // ✅ agregar al Set local
        }

        return artists;
    }
}