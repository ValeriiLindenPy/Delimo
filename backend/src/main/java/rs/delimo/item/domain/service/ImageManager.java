package rs.delimo.item.domain.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageManager {
    List<String> uploadImages(List<MultipartFile> images);
    void deleteImages(List<String> imageUrls);
}
