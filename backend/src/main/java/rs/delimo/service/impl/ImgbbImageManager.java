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

/**
 * Implementation of {@link ImageManager} that uploads images to the imgbb service.
 * <p>
 * This class uses the imgbb API to upload images. It encodes the images to Base64 and sends them
 * using a {@link RestTemplate}. The API key is injected via Spring's {@code @Value} annotation.
 * </p>
 *
 * <p>
 * Note: Only a maximum of {@value #MAX_IMAGES_COUNT} images can be uploaded per operation.
 * </p>
 */
@Component
@Slf4j
public class ImgbbImageManager implements ImageManager {

    /**
     * Maximum number of images that can be uploaded at once.
     */
    private static final int MAX_IMAGES_COUNT = 5;

    /**
     * The API key used for authenticating requests to the imgbb service.
     */
    private final String imgbbKey;

    /**
     * The {@link RestTemplate} used to make HTTP requests.
     */
    private final RestTemplate restTemplate;

    /**
     * The {@link ObjectMapper} used to parse JSON responses.
     */
    private final ObjectMapper objectMapper;

    /**
     * Constructs a new {@code ImgbbImageManager}.
     *
     * @param imgbbKey     the API key for the imgbb service, injected from configuration (e.g. {@code app.imgbb.key}).
     * @param restTemplate the {@link RestTemplate} to use for HTTP requests.
     * @param objectMapper the {@link ObjectMapper} to use for JSON processing.
     */
    @Autowired
    public ImgbbImageManager(@Value("${app.imgbb.key}") String imgbbKey, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.imgbbKey = imgbbKey;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Uploads a list of images to the imgbb service.
     * <p>
     * This method processes the list of {@link MultipartFile} images, validates the number of images,
     * and uploads each image individually.
     * </p>
     *
     * @param images the list of images to be uploaded.
     * @return a list of URLs corresponding to the successfully uploaded images.
     * @throws ImageUploadException if the number of images exceeds the allowed maximum.
     */
    @Override
    public List<String> uploadImages(List<MultipartFile> images) {
        return processImages(images);
    }

    /**
     * Processes the given list of images by validating and uploading them.
     *
     * @param images the list of images to process.
     * @return a list of URLs for the uploaded images. If the list is null or empty, an empty list is returned.
     */
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

    /**
     * Validates that the number of images does not exceed the maximum allowed.
     *
     * @param images the list of images to validate.
     * @throws ImageUploadException if the number of images is greater than {@value #MAX_IMAGES_COUNT}.
     */
    private static void validateImages(List<MultipartFile> images) {
        if (images.size() > MAX_IMAGES_COUNT) {
            log.error("Illegal image count: {}", images.size());
            throw new ImageUploadException("An item can have up to " + MAX_IMAGES_COUNT + " images.");
        }
    }

    /**
     * Uploads a single image to the imgbb service.
     * <p>
     * The image is converted to a Base64 encoded string and then sent to the imgbb API.
     * The method returns the URL of the uploaded image if successful; otherwise, it returns {@code null}.
     * </p>
     *
     * @param image the {@link MultipartFile} image to be uploaded.
     * @return the URL of the uploaded image, or {@code null} if an error occurs during upload.
     */
    private String uploadImageToImgbb(MultipartFile image) {
        try {
            log.warn("Start upload");
            String uploadUrl = "https://api.imgbb.com/1/upload";
            String encodedImage = Base64.getEncoder().encodeToString(image.getBytes());

            LinkedMultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("key", imgbbKey);
            formData.add("image", encodedImage);

            String response = restTemplate.postForObject(uploadUrl, formData, String.class);
            JsonNode root = objectMapper.readTree(response);
            log.warn("Response: {}", response);
            return root.path("data").path("url").asText();
        } catch (IOException e) {
            log.error("Error uploading image to imgbb: {}", e.getMessage());
            return null;
        }
    }
}
