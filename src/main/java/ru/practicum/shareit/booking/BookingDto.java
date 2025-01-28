package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {

    private Integer id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer itemId;
    private Integer bookerId;
    private BookingStatus status;
}