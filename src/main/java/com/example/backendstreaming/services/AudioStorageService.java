package com.example.backendstreaming.services;


import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class AudioStorageService {

    @Value("${app.audio.path}")
    private String audioPath;



    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(Paths.get(audioPath));
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el directorio de audio", e);
        }
    }


    public String saveAudio(MultipartFile file) throws IOException {

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("audio/")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El tipo debe ser de tipo audio");

        }

        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String fileName = UUID.randomUUID() + "." + extension;
        Path targetPath = Paths.get(audioPath).resolve(fileName);

        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        return  targetPath.toString();

    }

    public void deleteAudio(String audioUrl) throws IOException {
        Path filePath = Paths.get(audioUrl);
        Files.deleteIfExists(filePath);
    }


}
