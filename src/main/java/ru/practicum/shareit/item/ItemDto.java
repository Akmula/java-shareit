package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.BookingDtoForItem;
import ru.practicum.shareit.user.User;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {

    private Integer id;
    private String name;
    private String description;
    private Boolean available;
    private List<CommentDtoResponse> comments;
    private BookingDtoForItem nextBooking;
    private BookingDtoForItem lastBooking;
    private User owner;
}