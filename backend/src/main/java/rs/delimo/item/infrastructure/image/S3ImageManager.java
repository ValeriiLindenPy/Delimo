package rs.delimo.item.infrastructure.image;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import rs.delimo.common.exception.ImageUploadException;
import rs.delimo.item.domain.service.ImageManager;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Profile("test")
public class S3ImageManager implements ImageManager {

    private final S3Client s3;
    private final String bucket;

    public S3ImageManager(S3Client s3, @Value("${aws.s3.bucket}") String bucket) {
        this.s3 = s3;
        this.bucket = bucket;
    }

    @PostConstruct
    private void ensureBucket() {
        if (s3.listBuckets().buckets().stream().noneMatch(b -> b.name().equals(bucket))) {
            s3.createBucket(b -> b.bucket(bucket));
        }
    }

    @Override
    public List<String> uploadImages(List<MultipartFile> images) {
        if (images == null || images.isEmpty()) return List.of();

        return images.stream()
                .map(file -> {
                    String key = UUID.randomUUID() + getExt(file.getOriginalFilename());
                    try {
                        s3.putObject(
                                PutObjectRequest.builder()
                                        .bucket(bucket)
                                        .key(key)
                                        .contentType(file.getContentType())
                                        .contentLength(file.getSize())
                                        .build(),
                                RequestBody.fromInputStream(file.getInputStream(), file.getSize())
                        );
                    } catch (IOException e) {
                        throw new ImageUploadException("S3 upload failed: " + e.getMessage());
                    }
                    return s3.utilities()
                            .getUrl(b -> b.bucket(bucket).key(key))
                            .toString();
                })
                .collect(Collectors.toList());
    }

    private String getExt(String name) {
        return (name != null && name.contains("."))
                ? name.substring(name.lastIndexOf(".")) : "";
    }
}
