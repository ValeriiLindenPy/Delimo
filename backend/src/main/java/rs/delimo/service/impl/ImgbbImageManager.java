package rs.delimo.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import rs.delimo.error.exception.ImageUploadException;
import rs.delimo.service.ImageManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ImgbbImageManager implements ImageManager {
    private static final int MAX_IMAGES_COUNT = 5;
    private final String imgbbKey;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public ImgbbImageManager(@Value("${imgbb.key}") String imgbbKey, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.imgbbKey = imgbbKey;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }


    @Override
    public List<String> uploadImages(List<MultipartFile> images) {
        return processImages(images);
    }

    private List<String> processImages(List<MultipartFile> images) {
        log.warn("images: {}", images);
        List<String> imageUrls = new ArrayList<>();
        if (images != null && !images.isEmpty()) {
            validateImages(images);
            imageUrls = images.stream()
                    .map(this::uploadImageToImgbb)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        return imageUrls;
    }

    private static void validateImages(List<MultipartFile> images) {
        if (images.size() > MAX_IMAGES_COUNT) {
            log.error("illegal image count");
            throw new ImageUploadException("An item can have up to " + MAX_IMAGES_COUNT + " images.");
        }
    }

    private String uploadImageToImgbb(MultipartFile image) {
        try {
            log.warn("start upload");
            String uploadUrl = "https://api.imgbb.com/1/upload";
            String encodedImage = Base64.getEncoder().encodeToString(image.getBytes());
            LinkedMultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("key", imgbbKey);
            formData.add("image", encodedImage);
            String response = restTemplate.postForObject(uploadUrl, formData, String.class);
            JsonNode root = objectMapper.readTree(response);
            log.warn("response: {}", response);
            return root.path("data").path("url").asText();
        } catch (IOException e ) {
            log.error("Error to upload images to imgbb {}", e.getMessage());
            return null;
        }
    }
}
