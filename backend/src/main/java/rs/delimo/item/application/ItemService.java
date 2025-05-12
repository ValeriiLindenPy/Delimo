package rs.delimo.item.application;

import org.springframework.web.multipart.MultipartFile;
import rs.delimo.api.dto.*;
import rs.delimo.common.valueobject.UserId;

import java.util.List;
import java.util.UUID;


public interface ItemService {

    ItemDto getById(UUID itemId);

    ItemDto create(ItemRequestDto item, UserId user, List<MultipartFile> images);

    ItemDto getByUserAndId(UUID id, UserId user);

    ItemDto editOne(UUID itemId, ItemUpdateDto item, UserId user, List<MultipartFile> images, String existingImagesJson);

    void delete(UUID itemId, UserId user);

    List<ItemTitle> searchTitles(String q, int limit);

    ItemPageResponse search(Integer page, Integer pageSize, ItemFilterDto itemFilterDto);

    ItemPageResponse getAll(UserId user, Integer page, Integer size);

    ItemPageResponse listItems(Integer page, Integer size);
}
