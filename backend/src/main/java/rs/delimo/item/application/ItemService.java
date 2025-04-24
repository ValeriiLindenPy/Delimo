package rs.delimo.item.application;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import rs.delimo.api.dto.ItemDto;
import rs.delimo.api.dto.ItemRequestDto;
import rs.delimo.api.dto.ItemTitle;
import rs.delimo.user.domain.User;

import java.util.List;
import java.util.UUID;


public interface ItemService {
    Page<ItemDto> getAll(int page, int size);

    Page<ItemDto> getAll(String city, int page, int size);

    ItemDto getById(UUID itemId);

    Page<ItemDto> searchByText(String text, int page, int pageSize, String city);

    ItemDto create(ItemRequestDto item, User user, List<MultipartFile> images);

    ItemDto getByUserAndId(UUID id, User user);

    Page<ItemDto> getAllByOwner(int page, int size, User user);

    ItemDto editOne(UUID itemId, ItemRequestDto item, User user, List<MultipartFile> images, String existingImagesJson);

    void delete(UUID itemId, User user);

    List<ItemTitle> searchTitles(String q, int limit);
}
