package rs.delimo.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageManager {
    List<String> uploadImages(List<MultipartFile> images);
}
