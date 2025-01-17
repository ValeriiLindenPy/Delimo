package rs.delimo.item;

import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.multipart.MultipartFile;
import rs.delimo.item.dto.ItemDto;
import rs.delimo.item.dto.ItemRequestDto;

import java.util.List;


public interface ItemService {
    Page<ItemDto> getAll(int page, int size);

    ItemDto getById(Long itemId);
    
    List<ItemDto> searchByText(String text);

    ItemDto create(ItemRequestDto item, OidcUser userId, List<MultipartFile> images);

    ItemDto getByUserAndId(Long id, OidcUser user);

    Page<ItemDto> getAllByOwner(int page, int size, OidcUser user);

    ItemDto editOne(Long itemId, ItemRequestDto item, OidcUser user, List<MultipartFile> images);
}
