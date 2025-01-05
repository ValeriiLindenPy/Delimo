package com.dajdam.daj_dam.request;

import lombok.Builder;
import lombok.Data;
import com.dajdam.daj_dam.user.User;

import java.time.LocalDateTime;

@Data
@Builder
public class ItemRequest {
    /**
     * уникальный идентификатор запроса
     */
    private Long id;
    /**
     * текст запроса, содержащий описание требуемой вещи
     */
    private String description;
    /**
     * пользователь, создавший запрос
     */
    private User requestor;
    /**
     *  дата и время создания запроса.
     */
    @Builder.Default
    private LocalDateTime created = LocalDateTime.now();
}
