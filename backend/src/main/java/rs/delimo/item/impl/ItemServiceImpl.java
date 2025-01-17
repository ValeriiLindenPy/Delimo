package rs.delimo.item.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import rs.delimo.error.exception.NotFoundException;
import rs.delimo.error.exception.OwnerException;
import rs.delimo.item.ItemMapper;
import rs.delimo.item.ItemRepository;
import rs.delimo.item.ItemService;
import rs.delimo.item.dto.ItemDto;
import rs.delimo.item.Item;
import rs.delimo.item.dto.ItemRequestDto;
import rs.delimo.user.User;
import rs.delimo.user.UserRepository;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final int MAX_IMAGES_COUNT =  5;

    @Override
    public Page<ItemDto> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        // The custom query that fetches images
        Page<Item> itemsPage = itemRepository.findAllWithImages(pageable);

        // Convert to DTO
        return itemsPage.map(ItemMapper::toItemDto);
    }

    @Override
    public Page<ItemDto> getAllByOwner(int page, int size, OidcUser user) {
        Pageable pageable = PageRequest.of(page, size);

        User owner = userRepository.findByEmail(user.getAttribute("email")).orElseThrow(
                () -> new NotFoundException("User not found")
        );

        Page<Item> itemsPage = itemRepository.findAllByOwnerWithImages(pageable,owner.getId());

        return itemsPage.map(ItemMapper::toItemDto);
    }

    @Override
    @Transactional
    public ItemDto getById(Long itemId) {
        return itemRepository.findByIdWithImages(itemId)
                .map(ItemMapper::toItemDto)
                .orElseThrow(
                        () -> new NotFoundException("Item with id - %d not found"
                                .formatted(itemId))
                );
    }

    @Override
    public ItemDto getByUserAndId(Long id, OidcUser user) {
        User owner = userRepository.findByEmail(user.getAttribute("email")).orElseThrow(
                () -> new NotFoundException("User not found")
        );

        return itemRepository.findOneByUserIdAndItemId(owner.getId(), id)
                .map(ItemMapper::toItemDto)
                .orElseThrow(
                        () -> new NotFoundException("Item with id - %d not found"
                                .formatted(id))
                );
    }


    @Override
    public ItemDto editOne(Long id, ItemDto item, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User with id - %d not found"
                        .formatted(userId))
        );

        Item oldItem = itemRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Item with id - %d not found"
                        .formatted(id))
        );

        if (!Objects.equals(oldItem.getOwner().getId(), user.getId())) {
            throw new OwnerException("Item with id %d does not belong to user with id %d"
                    .formatted(id, user.getId()));
        }

        if (item.getTitle() != null) {
            oldItem.setTitle(item.getTitle());
        }
        if (item.getDescription() != null) {
            oldItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            oldItem.setAvailable(item.getAvailable());
        }

        return ItemMapper.toItemDto(itemRepository.save(oldItem));
    }

    @Override
    public List<ItemDto> searchByText(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }
        String lowerText = text.toLowerCase();

        return itemRepository.findAll().stream()
                .filter(Item::getAvailable)
                .filter(item -> item.getTitle().toLowerCase().contains(lowerText)
                        || item.getDescription().toLowerCase().contains(lowerText))
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    @Transactional
    public ItemDto create(ItemRequestDto item, OidcUser oidcUser, List<MultipartFile> images) {

        User owner = userRepository.findByEmail(oidcUser.getAttribute("email")).orElseThrow(
                () -> new NotFoundException("User not found")
        );
        
        log.trace("upload to imgbb");
        List<String> imageUrls = new ArrayList<>();
        if (images != null && !images.isEmpty()) {
            if (images.size() > MAX_IMAGES_COUNT) {
                log.error("illegal image count");
                throw new IllegalArgumentException("An item can have up to " + MAX_IMAGES_COUNT + " images.");
            }
            imageUrls = images.stream()
                    .map(this::uploadImageToImgbb)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }

        log.trace("Check user data");
        if ((item.getCity() != null) && (owner.getCity() == null) ||
                (item.getViber() != null) && (owner.getViber() == null) ||
                (item.getPhone() != null) && (owner.getPhone() == null)) {

            if ((item.getCity() != null) && (owner.getCity() == null)) {
                owner.setCity(item.getCity());
            }

            if ((item.getViber() != null) && (owner.getViber() == null)) {
                owner.setViber(item.getViber());
            }

            if ((item.getPhone() != null) && (owner.getPhone() == null)) {
                owner.setPhone(item.getPhone());
            }

            userRepository.save(owner);
        }

        log.trace("convert to dto");
        Item newItem = ItemMapper.toItem(item);

        log.trace("adjust item");
        newItem.setImages(imageUrls);
        newItem.setAvailable(true);
        newItem.setOwner(owner);


        return ItemMapper.toItemDto(itemRepository.save(newItem));
    }

    private String uploadImageToImgbb(MultipartFile image) {
        try {
            Dotenv dotenv = Dotenv.load();
            String key = dotenv.get("IMGBB_KEY");
            String uploadUrl = "https://api.imgbb.com/1/upload";
            String encodedImage = Base64.getEncoder().encodeToString(image.getBytes());
            LinkedMultiValueMap linkedMultiValueMap = new LinkedMultiValueMap();
            linkedMultiValueMap.add("key", key);
            linkedMultiValueMap.add("image", encodedImage);
            RestTemplate restTemplate = new RestTemplate();
            String response = (String)restTemplate.postForObject(uploadUrl, linkedMultiValueMap, String.class, new Object[0]);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);
            return root.path("data").path("url").asText();
        } catch (Exception e) {
            log.error("Failed to upload image to Imgbb: {}", e.getMessage());
            return null;
        }
    }


    private void isUserExist(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id - %d not found"
                        .formatted(userId))
                );
    }
}
