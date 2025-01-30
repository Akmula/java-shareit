package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemResponseDto createItem(@RequestHeader("X-Sharer-User-Id") int userId,
                                      @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Creating new item: {}", itemRequestDto);
        ItemResponseDto itemResponseDto = itemService.createItem(userId, itemRequestDto);
        log.info("Created new item: {}", itemResponseDto);
        return itemResponseDto;
    }

    @PatchMapping("/{itemId}")
    public ItemResponseDto updateItem(@RequestHeader("X-Sharer-User-Id") int userId,
                                      @PathVariable int itemId,
                                      @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Updating existing item: {}", itemRequestDto);
        ItemResponseDto itemResponseDto = itemService.updateItem(userId, itemId, itemRequestDto);
        log.info("Updating existing item: {}", itemResponseDto);
        return itemResponseDto;
    }

    @GetMapping("/{itemId}")
    public ItemResponseDto getItemById(@RequestHeader("X-Sharer-User-Id") int userId,
                                       @PathVariable int itemId) {
        log.info("Getting item by id: {}", itemId);
        ItemResponseDto itemResponseDto = itemService.getItemById(userId, itemId);
        log.info("Getting item by id: {}", itemResponseDto);
        return itemResponseDto;
    }

    @GetMapping
    public List<ItemResponseDto> getAllItems(@RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("Getting all items");
        List<ItemResponseDto> itemsResponseDto = itemService.getItems(userId);
        log.info("Getting all items");
        return itemsResponseDto;
    }

    @GetMapping("/search")
    public List<ItemResponseDto> searchItems(@RequestHeader("X-Sharer-User-Id") int userId,
                                             @RequestParam String text) {
        log.info("Searching for items with text {}", text);
        List<ItemResponseDto> searchItems = itemService.searchItems(userId, text);
        log.info("Searching for items with text {}", searchItems);
        return searchItems;
    }
}