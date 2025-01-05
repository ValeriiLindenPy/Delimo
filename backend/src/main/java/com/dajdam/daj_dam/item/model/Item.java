package com.dajdam.daj_dam.item.model;

import lombok.Builder;
import lombok.Data;
import com.dajdam.daj_dam.request.ItemRequest;
import com.dajdam.daj_dam.user.dto.UserDto;


@Data
@Builder
public class Item {
    /**
     * уникальный идентификатор вещи
     */
    private Long id;
    /**
     * краткое название;
     */
    private String name;
    /**
     * развёрнутое описание
     */
    private String description;
    /**
     * статус о том, доступна или нет вещь для аренды;
     */
    private Boolean available;
    /**
     * владелец вещи
     */
    private UserDto owner;
    /**
     * если вещь была создана по запросу другого пользователя, то в этом
     * поле будет храниться ссылка на соответствующий запрос
     */
    private ItemRequest request;
}
