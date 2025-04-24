package rs.delimo.item.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import rs.delimo.api.controller.MyItemsApi;
import rs.delimo.api.dto.ItemDto;
import rs.delimo.api.dto.ItemPageResponse;
import rs.delimo.api.dto.ItemRequestDto;
import rs.delimo.item.application.ItemService;
import rs.delimo.user.domain.User;

import java.util.List;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
public class MyItemController implements MyItemsApi {
    private final ItemService service;

    @Override
    public ResponseEntity<ItemDto> createMyItem(ItemRequestDto item, List<MultipartFile> images) {
        User user = getCurrentUser();
        return ResponseEntity.ok(service.create(item,user, images));
    }

    @Override
    public ResponseEntity<Void> deleteMyItem(UUID id) {
        User user = getCurrentUser();
        service.delete(id, user);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<ItemDto> getMyItem(UUID id) {
        User user = getCurrentUser();
        return ResponseEntity.ok(service.getByUserAndId(id, user));
    }

    @Override
    public ResponseEntity<List<ItemPageResponse>> listMyItems(Integer page, Integer size) {
        return MyItemsApi.super.listMyItems(page, size);
    }

    @Override
    public ResponseEntity<ItemDto> updateMyItem(UUID id, ItemRequestDto item, String existingImages, List<MultipartFile> images) {
        User user = getCurrentUser();
        return ResponseEntity.ok(service.editOne(id, item, user,images, existingImages));
    }


    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
