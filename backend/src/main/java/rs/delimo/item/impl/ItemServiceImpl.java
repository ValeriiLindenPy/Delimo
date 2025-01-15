package rs.delimo.item.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rs.delimo.error.exception.NotFoundException;
import rs.delimo.error.exception.OwnerException;
import rs.delimo.item.ItemMapper;
import rs.delimo.item.ItemRepository;
import rs.delimo.item.ItemService;
import rs.delimo.item.dto.ItemDto;
import rs.delimo.item.Item;
import rs.delimo.user.User;
import rs.delimo.user.UserRepository;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public List<ItemDto> getAll(Long userId) {
        isUserExist(userId);
        return itemRepository.findByUserId(userId).stream()
                .map(ItemMapper::toItemDto).toList();
    }

    @Override
    public ItemDto getById(Long itemId) {
        return itemRepository.findById(itemId)
                .map(ItemMapper::toItemDto)
                .orElseThrow(
                        () -> new NotFoundException("Item with id - %d not found"
                                .formatted(itemId))
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

        if (item.getName() != null) {
            oldItem.setName(item.getName());
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
                .filter(item -> item.getName().toLowerCase().contains(lowerText)
                        || item.getDescription().toLowerCase().contains(lowerText))
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public ItemDto create(ItemDto item, Long userId) {
        Item newItem = ItemMapper.toItem(item);
        return ItemMapper.toItemDto(itemRepository.save(newItem));
    }



    private void isUserExist(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id - %d not found"
                        .formatted(userId))
                );
    }
}
