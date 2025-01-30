package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.Item;

import java.util.List;

@Component
public class ItemMapper {

    public static Item dtoToItem(int userId, ItemRequestDto itemRequestDto) {
        return Item.builder()
                .name(itemRequestDto.getName())
                .description(itemRequestDto.getDescription())
                .available(itemRequestDto.getAvailable())
                .ownerId(userId)
                .build();
    }

    public static ItemResponseDto itemToResponseDto(Item item) {
        return ItemResponseDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .build();
    }

    public static List<ItemResponseDto> itemsToResponseDto(List<Item> items) {
        return items.stream().map(ItemMapper::itemToResponseDto).toList();
    }
}