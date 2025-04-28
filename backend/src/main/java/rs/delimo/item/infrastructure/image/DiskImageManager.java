package rs.delimo.item.infrastructure.image;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import rs.delimo.common.exception.ImageUploadException;
import rs.delimo.item.domain.service.ImageManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Profile("test")
public class DiskImageManager implements ImageManager {
    private final Path uploadDir;

    public DiskImageManager(@Value("${app.upload.dir}")Path uploadDir) {
        this.uploadDir = Paths.get(uploadDir.toUri()).toAbsolutePath().normalize();
    }

    @PostConstruct
    private void init() {
        try {
            Files.createDirectories(uploadDir);
        } catch (IOException e) {
            throw new ImageUploadException("Could not create upload directory: " + e);
        }
    }

    @Override
    public List<String> uploadImages(List<MultipartFile> images) {
        if (images == null || images.isEmpty()) {
            return List.of();
        }
        return images.stream()
                .map(this::storeAndGetUrl)
                .collect(Collectors.toList());
    }

    private String storeAndGetUrl(MultipartFile file) {
        String ext = "";
        var original = file.getOriginalFilename();
        if (original != null && original.contains(".")) {
            ext = original.substring(original.lastIndexOf("."));
        }
        String filename = UUID.randomUUID() + ext;
        try {
            Path target = uploadDir.resolve(filename);
            Files.copy(file.getInputStream(), target);
        } catch (IOException e) {
            throw new ImageUploadException("Failed to store file " + filename);
        }
        // get all URL: http://<host>/images/{filename}
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/images/")
                .path(filename)
                .toUriString();
    }
}