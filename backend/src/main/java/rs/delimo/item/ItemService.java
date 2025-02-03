package rs.delimo.item;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import rs.delimo.item.dto.ItemDto;
import rs.delimo.item.dto.ItemRequestDto;
import rs.delimo.item.dto.ItemTitle;
import rs.delimo.user.User;

import java.util.List;


public interface ItemService {
    Page<ItemDto> getAll(int page, int size);

    Page<ItemDto> getAll(String city, int page, int size);

    ItemDto getById(Long itemId);

    Page<ItemDto> searchByText(String text, int page, int pageSize, String city);

    ItemDto create(ItemRequestDto item, User userId, List<MultipartFile> images);

    ItemDto getByUserAndId(Long id, User user);

    Page<ItemDto> getAllByOwner(int page, int size, User user);

    ItemDto editOne(Long itemId, ItemRequestDto item, User user, List<MultipartFile> images, String existingImagesJson);

    void delete(Long itemId, User user);

    List<ItemTitle> searchTitles(String q, int limit);
}
