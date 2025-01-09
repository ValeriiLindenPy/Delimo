package com.dajdam.daj_dam.item;

import com.dajdam.daj_dam.item.model.Item;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByUserId(Long userId);
}
