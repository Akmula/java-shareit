package ru.practicum.shareit.booking;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface BookingService {

    @Transactional
    BookingDtoResponse createBooking(int userId, BookingDtoRequest bookingDtoRequest);

    @Transactional
    BookingDtoResponse approvedBooking(int userId, int bookingId, boolean approved);

    BookingDtoResponse getBookingById(int userId, int bookingId);

    List<BookingDtoResponse> getBookingsByBooker(int userId, State state);

    List<BookingDtoResponse> getBookingsByOwner(int userId, State state);
}