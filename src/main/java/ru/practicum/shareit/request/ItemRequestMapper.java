package ru.practicum.shareit.request;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemRequestMapper {

    public ItemRequestDto itemRequestDto(ItemRequest itemRequest) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requestorId(itemRequest.getRequestorId())
                .createdTime(itemRequest.getCreatedTime())
                .build();
    }

    public ItemRequest dtoToUser(ItemRequestDto itemRequestDto) {
        return ItemRequest.builder()
                .id(itemRequestDto.getId())
                .description(itemRequestDto.getDescription())
                .requestorId(itemRequestDto.getRequestorId())
                .createdTime(itemRequestDto.getCreatedTime())
                .build();
    }

    public List<ItemRequestDto> itemRequestsToDto(List<ItemRequest> itemRequests) {
        return itemRequests.stream().map(this::itemRequestDto).toList();
    }
}