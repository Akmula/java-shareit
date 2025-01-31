package ru.practicum.shareit.booking;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookingMapper {

    public BookingDto bookingToDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .startTime(booking.getStartTime())
                .endTime(booking.getEndTime())
                .itemId(booking.getItemId())
                .bookerId(booking.getBookerId())
                .status(booking.getStatus())
                .build();
    }

    public Booking dtoToBooking(BookingDto bookingDto) {
        return Booking.builder()
                .id(bookingDto.getId())
                .startTime(bookingDto.getStartTime())
                .endTime(bookingDto.getEndTime())
                .itemId(bookingDto.getItemId())
                .bookerId(bookingDto.getBookerId())
                .status(bookingDto.getStatus())
                .build();
    }

    public List<BookingDto> usersToDto(List<Booking> bookings) {
        return bookings.stream().map(this::bookingToDto).toList();
    }
}