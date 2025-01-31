package ru.practicum.shareit.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ItemRequest {
    private Integer id;
    private String description;
    private Integer requestorId;
    private LocalDateTime createdTime;
}