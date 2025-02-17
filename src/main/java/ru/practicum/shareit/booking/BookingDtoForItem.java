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
public class BookingDtoForItem {

    private Integer id;
    private LocalDateTime start;
    private LocalDateTime end;
    private BookingStatus status;
    private Integer bookerId;
}