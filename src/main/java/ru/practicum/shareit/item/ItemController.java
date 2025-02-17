package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public CreatedItemDtoResponse createItem(@RequestHeader("X-Sharer-User-Id") int userId,
                                             @Valid @RequestBody ItemDtoRequest itemRequestDto) {
        log.info("Добавление предмета: {}", itemRequestDto);
        CreatedItemDtoResponse createdItemDtoResponse = itemService.createItem(userId, itemRequestDto);
        log.info("Добавлен предмет: {}", createdItemDtoResponse);
        return createdItemDtoResponse;
    }

    @PatchMapping("/{itemId}")
    public ItemDtoResponse updateItem(@RequestHeader("X-Sharer-User-Id") int userId,
                                      @PathVariable int itemId,
                                      @RequestBody ItemDtoRequest itemRequestDto) {
        log.info("Обновление предмета: {}", itemRequestDto);
        ItemDtoResponse itemResponseDto = itemService.updateItem(userId, itemId, itemRequestDto);
        log.info("Обновлен предмет: {}", itemResponseDto);
        return itemResponseDto;
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@RequestHeader("X-Sharer-User-Id") int userId,
                               @PathVariable int itemId) {
        log.info("Получение предмета по id: {}", itemId);
        ItemDto itemDto = itemService.getItemById(userId, itemId);
        log.info("Получен предмет id: {}", itemDto);
        return itemDto;
    }

    @GetMapping
    public List<ItemDto> getAllItems(@RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("Получение списка всех предметов по id: {}", userId);
        List<ItemDto> itemsResponseDto = itemService.getItems(userId);
        log.info("Получен список предметов: {}", itemsResponseDto);
        return itemsResponseDto;
    }

    @GetMapping("/search")
    public List<ItemDtoResponse> searchItems(@RequestHeader("X-Sharer-User-Id") int userId,
                                             @RequestParam String text) {
        log.info("Поиск предметов по запросу: {}", text);
        List<ItemDtoResponse> searchItems = itemService.searchItems(userId, text);
        log.info("Получен список предметов по запросу: {}", searchItems);
        return searchItems;
    }

    @PostMapping("/{itemId}/comment")
    public CommentDtoResponse addComment(@RequestHeader("X-Sharer-User-Id") int userId,
                                         @PathVariable int itemId,
                                         @Valid @RequestBody CommentDtoRequest commentDtoRequest) {
        log.info("Добавление комментария к предмету с id: {}", itemId);
        CommentDtoResponse comment = itemService.addComment(userId, itemId, commentDtoRequest);
        log.info("Добавлен комментарий {}", comment);
        return comment;
    }
}