package ru.practicum.shareit.booking;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Booking {

    private Integer id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer itemId;
    private Integer bookerId;
    private BookingStatus status;
}