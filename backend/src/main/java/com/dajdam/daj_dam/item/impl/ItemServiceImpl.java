package com.dajdam.daj_dam.item.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.dajdam.daj_dam.error.exception.NotFoundException;
import com.dajdam.daj_dam.error.exception.OwnerException;
import com.dajdam.daj_dam.item.ItemMapper;
import com.dajdam.daj_dam.item.ItemRepository;
import com.dajdam.daj_dam.item.ItemService;
import com.dajdam.daj_dam.item.dto.ItemDto;
import com.dajdam.daj_dam.item.model.Item;
import com.dajdam.daj_dam.user.User;
import com.dajdam.daj_dam.user.UserMapper;
import com.dajdam.daj_dam.user.UserRepository;

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
        return itemRepository.findAllByUser(userId).stream()
                .map(ItemMapper::toItemDto).toList();
    }

    @Override
    public ItemDto getById(Long itemId) {
        return itemRepository.findOne(itemId)
                .map(ItemMapper::toItemDto)
                .orElseThrow(
                        () -> new NotFoundException("Item with id - %d not found"
                                .formatted(itemId))
                );
    }


    @Override
    public ItemDto editOne(Long id, ItemDto item, Long userId) {
        User user = userRepository.findOne(userId).orElseThrow(
                () -> new NotFoundException("User with id - %d not found"
                        .formatted(userId))
        );

        Item oldItem = itemRepository.findOne(id).orElseThrow(
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

        return ItemMapper.toItemDto(itemRepository.update(oldItem));
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
        newItem.setOwner(userRepository.findOne(userId).map(UserMapper::toUserDto)
                .orElseThrow(() -> new NotFoundException("User with id - %d not found"
                        .formatted(userId))
                )
        );
        return ItemMapper.toItemDto(itemRepository.save(newItem));
    }

    @Override
    public void clear() {
        itemRepository.clear();
    }

    private void isUserExist(Long userId) {
        userRepository.findOne(userId)
                .orElseThrow(() -> new NotFoundException("User with id - %d not found"
                        .formatted(userId))
                );
    }
}
