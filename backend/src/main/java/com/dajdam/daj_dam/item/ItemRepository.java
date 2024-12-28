package com.dajdam.daj_dam.item;

import com.dajdam.daj_dam.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    List<Item> findAllByUser(Long userId);

    Optional<Item> findOne(Long itemId);

    Item save(Item newItem);

    List<Item> findAll();

    void clear();

    Item update(Item newItem);
}
