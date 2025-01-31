package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import java.util.List;

public interface ItemService {

    ItemResponseDto createItem(int userId, ItemRequestDto itemRequestDto);

    ItemResponseDto updateItem(int userId, int itemId, ItemRequestDto itemRequestDto);

    ItemResponseDto getItemById(int userId, int itemId);

    List<ItemResponseDto> getItems(int userId);

    List<ItemResponseDto> searchItems(int userId, String text);

}