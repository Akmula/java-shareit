package ru.practicum.shareit.item;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface ItemService {

    @Transactional
    ItemDtoResponse createItem(int userId, ItemDtoRequest itemRequestDto);

    @Transactional
    ItemDtoResponse updateItem(int userId, int itemId, ItemDtoRequest itemRequestDto);

    ItemDto getItemById(int userId, int itemId);

    List<ItemDto> getItems(int userId);

    List<ItemDtoResponse> searchItems(int userId, String text);

    @Transactional
    CommentDtoResponse addComment(int userId, int itemId, CommentDtoRequest commentDtoRequest);
}