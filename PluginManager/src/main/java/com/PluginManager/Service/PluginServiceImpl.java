package com.PluginManager.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@Slf4j
public class PluginServiceImpl implements PluginService {

    @Override
    public ResponseEntity<String> installPlugin(
            MultipartFile plugin
    ) throws IOException {

        ApplicationHome applicationHome = new ApplicationHome();

        Path pluginFolder = applicationHome
                .getDir()
                .toPath()
                .resolve("plugin");

        Files.createDirectories(pluginFolder);

        String originalFilename = plugin.getOriginalFilename();

        if (originalFilename == null) {
            return ResponseEntity.badRequest()
                    .body("Nome file non valido");
        }

        String filename = Paths.get(originalFilename)
                .getFileName()
                .toString();

        if (!filename.toLowerCase().endsWith(".jar")) {
            return ResponseEntity.badRequest()
                    .body("Il file deve essere un JAR");
        }

        Path destination = pluginFolder.resolve(filename);

        try (InputStream inputStream = plugin.getInputStream()) {
            Files.copy(
                    inputStream,
                    destination,
                    StandardCopyOption.REPLACE_EXISTING
            );
        }

        return ResponseEntity.ok(
                "Plugin installato: " + filename
        );
    }
}
