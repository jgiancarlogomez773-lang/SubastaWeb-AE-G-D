package com.subastaweb.persistence.storage;

import com.subastaweb.application.ImageStorage;
import com.subastaweb.config.AppProperties;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/** Guarda las imágenes en disco con nombre aleatorio (nunca se usa el nombre que envía el cliente). */
@Component
public class LocalImageStorage implements ImageStorage {

    private static final Logger log = LoggerFactory.getLogger(LocalImageStorage.class);

    private final Path root;

    public LocalImageStorage(AppProperties props) {
        this.root = Path.of(props.storage().dir()).toAbsolutePath().normalize();
    }

    @Override
    public String store(byte[] content, String extension) {
        String name = UUID.randomUUID() + "." + extension;
        try {
            Files.createDirectories(root);
            Files.write(root.resolve(name), content, StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo guardar la imagen", e);
        }
        return name;
    }

    @Override
    public void delete(String name) {
        try {
            Files.deleteIfExists(root.resolve(name));
        } catch (IOException e) {
            log.warn("No se pudo borrar un archivo temporal de imagen", e);
        }
    }
}
