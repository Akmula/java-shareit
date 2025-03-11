package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService service;

    @PostMapping
    public ItemRequestDto createRequest(@RequestHeader("X-Sharer-User-Id") int userId,
                                        @RequestBody ItemRequestDtoRequest request) {
        log.info("Добавление запроса: {}, пользователем с id: {}", request, userId);
        ItemRequestDto createdRequest = service.createRequest(userId, request);
        log.info("Добавлен запрос: {}", createdRequest);
        return createdRequest;
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getRequestById(@RequestHeader("X-Sharer-User-Id") int userId,
                                         @PathVariable int requestId) {
        log.info("Получение запроса по id: {}", requestId);
        ItemRequestDto request = service.getRequestById(userId, requestId);
        log.info("Получен запрос: {}", request);
        return request;
    }

    @GetMapping
    public List<ItemRequestDto> getAllRequestByUserId(@RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("Получение списка всех запросов по id: {}", userId);
        List<ItemRequestDto> requests = service.getAllRequestByUserId(userId);
        log.info("Получен список запросов пользователя: {}", requests);
        return requests;
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequest(@RequestHeader("X-Sharer-User-Id") int userId) {
        log.info("Получение всех запросов");
        List<ItemRequestDto> requests = service.getAllRequest();
        log.info("Получен список всех запросов: {}", requests);
        return requests;
    }
}