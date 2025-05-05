package rs.delimo.item.application;

import org.springframework.web.multipart.MultipartFile;
import rs.delimo.api.dto.*;
import rs.delimo.user.domain.User;

import java.util.List;
import java.util.UUID;


public interface ItemService {

    ItemDto getById(UUID itemId);

    ItemDto create(ItemRequestDto item, User user, List<MultipartFile> images);

    ItemDto getByUserAndId(UUID id, User user);

    ItemDto editOne(UUID itemId, ItemUpdateDto item, User user, List<MultipartFile> images, String existingImagesJson);

    void delete(UUID itemId, User user);

    List<ItemTitle> searchTitles(String q, int limit);

    ItemPageResponse search(Integer page, Integer pageSize, ItemFilterDto itemFilterDto);

    ItemPageResponse getAll(User user, Integer page, Integer size);
}
