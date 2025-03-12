package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDtoResponse createItem(@RequestHeader("X-Sharer-User-Id") int userId,
                                             @RequestBody ItemDtoRequest itemRequest) {
        log.info("Добавление предмета: {}", itemRequest);
        ItemDtoResponse createdItem = itemService.createItem(userId, itemRequest);
        log.info("Добавлен предмет: {}", createdItem);
        return createdItem;
    }

    @PatchMapping("/{itemId}")
    public ItemDtoResponse updateItem(@RequestHeader("X-Sharer-User-Id") int userId,
                                      @PathVariable int itemId,
                                      @RequestBody ItemDtoRequest itemRequest) {
        log.info("Обновление предмета: {}", itemRequest);
        ItemDtoResponse itemResponse = itemService.updateItem(userId, itemId, itemRequest);
        log.info("Обновлен предмет: {}", itemResponse);
        return itemResponse;
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@RequestHeader("X-Sharer-User-Id") int userId,
                               @PathVariable int itemId) {
        log.info("Получение предмета по id: {}", itemId);
        ItemDto item = itemService.getItemById(userId, itemId);
        log.info("Получен предмет id: {}", item);
        return item;
    }

    @GetMapping
    public List<ItemDto> getAllItems(@RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("Получение списка всех предметов пользователя с id: {}", userId);
        List<ItemDto> itemsResponse = itemService.getItems(userId);
        log.info("Получен список предметов: {}", itemsResponse);
        return itemsResponse;
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
                                         @RequestBody CommentDtoRequest commentRequest) {
        log.info("Добавление комментария к предмету с id: {}", itemId);
        CommentDtoResponse comment = itemService.addComment(userId, itemId, commentRequest);
        log.info("Добавлен комментарий {}", comment);
        return comment;
    }
}