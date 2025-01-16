package rs.delimo.item;

import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.multipart.MultipartFile;
import rs.delimo.item.dto.ItemDto;
import rs.delimo.item.dto.ItemRequestDto;

import java.util.List;


public interface ItemService {
    List<ItemDto> getAll(Long userId);

    ItemDto getById(Long itemId);

    ItemDto editOne(Long id, ItemDto item, Long userId);

    List<ItemDto> searchByText(String text);

    ItemDto create(ItemRequestDto item, OidcUser userId, List<MultipartFile> images);

}
