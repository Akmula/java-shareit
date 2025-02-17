package ru.practicum.shareit.request;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemRequestMapper {

    public ItemRequestDto toDto(ItemRequest itemRequest) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requestorId(itemRequest.getRequester().getId())
                .createdTime(itemRequest.getCreated())
                .build();
    }

    public ItemRequest toItemRequest(ItemRequestDto itemRequestDto) {
        return new ItemRequest();
    }

    public List<ItemRequestDto> itemRequestsToDto(List<ItemRequest> itemRequests) {
        return itemRequests.stream().map(this::toDto).toList();
    }
}