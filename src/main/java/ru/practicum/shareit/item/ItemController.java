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
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") int userId,
                              @Valid @RequestBody ItemDto itemDto) {
        log.info("Creating new item: {}", itemDto);
        ItemDto createdItemDto = itemService.createItem(userId, itemDto);
        log.info("Created new item: {}", createdItemDto);
        return createdItemDto;
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") int userId,
                              @PathVariable int itemId,
                              @RequestBody ItemDto itemDto) {
        log.info("Updating existing item: {}", itemDto);
        ItemDto updatingItemDto = itemService.updateItem(userId, itemId, itemDto);
        log.info("Updating existing item: {}", updatingItemDto);
        return updatingItemDto;
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@RequestHeader("X-Sharer-User-Id") int userId,
                               @PathVariable int itemId) {
        log.info("Getting item by id: {}", itemId);
        ItemDto gettingItemDto = itemService.getItemById(userId, itemId);
        log.info("Getting item by id: {}", gettingItemDto);
        return gettingItemDto;
    }

    @GetMapping
    public List<ItemDto> getAllItems(@RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("Getting all items");
        List<ItemDto> allItemsDto = itemService.getItems(userId);
        log.info("Getting all items");
        return allItemsDto;
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestHeader("X-Sharer-User-Id") int userId,
                                     @RequestParam String text) {
        log.info("Searching for items with text {}", text);
        List<ItemDto> searchItems = itemService.searchItems(userId, text);
        log.info("Searching for items with text {}", searchItems);
        return searchItems;
    }
}