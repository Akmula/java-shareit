package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.BookingDtoForItem;
import ru.practicum.shareit.user.User;

import java.util.List;

@Component
public class ItemMapper {

    public static Item dtoToItem(ItemDtoRequest itemDtoRequest, User user, Integer itemId) {
        Item item = new Item();
        if (itemId != null) {
            item.setId(itemId);
        }
        item.setName(itemDtoRequest.getName());
        item.setDescription(itemDtoRequest.getDescription());
        item.setAvailable(itemDtoRequest.getAvailable());
        item.setUser(user);
        return item;
    }

    public static ItemDtoResponse itemToDtoResponse(Item item) {
        return ItemDtoResponse.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .owner(item.getUser())
                .available(item.isAvailable())
                .build();
    }

    public static CreatedItemDtoResponse createdItemDtoResponse(Item item) {
        return CreatedItemDtoResponse.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .build();
    }

    public static ItemDto toItemDto(int userId, ItemDtoResponse item, List<CommentDtoResponse> comments,
                                    BookingDtoForItem nextBooking, BookingDtoForItem lastBooking) {
        ItemDto itemDto = ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .owner(item.getOwner())
                .available(item.getAvailable())
                .build();

        if (comments != null) {
            itemDto.setComments(comments);
        }

        if (item.getOwner().getId() == userId) {
            if (nextBooking != null) {
                itemDto.setNextBooking(nextBooking);
            }
            if (lastBooking != null) {
                itemDto.setLastBooking(lastBooking);
            }
        }
        return itemDto;
    }
}