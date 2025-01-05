package com.dajdam.daj_dam.booking;

import com.dajdam.daj_dam.booking.dto.BookingStatus;
import com.dajdam.daj_dam.item.model.Item;
import com.dajdam.daj_dam.user.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Booking {
    /**
     * уникальный идентификатор бронирования
     */
    private Long id;
    /**
     * дата и время начала бронирования
     */
    private LocalDateTime start;
    /**
     * дата и время конца бронирования
     */
    private LocalDateTime end;
    /**
     * вещь, которую пользователь бронирует
     */
    private Item item;
    /**
     * пользователь, который осуществляет бронирование
     */
    private User booker;
    /**
     * статус бронирования. Может принимать одно из следующих
     * значений:
     * {@link BookingStatus#WAITING} — новое бронирование,ожидает одобрения,
     * {@link BookingStatus#APPROVED} — бронирование подтверждено владельцем,
     * {@link BookingStatus#REJECTED} — бронирование отклонено владельцем,
     * {@link BookingStatus#CANCELED} — бронирование отменено создателем.
     */
    private BookingStatus status;
}
