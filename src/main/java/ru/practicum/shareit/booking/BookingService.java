package ru.practicum.shareit.booking;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface BookingService {

    @Transactional
    BookingDtoResponse createBooking(int userId, BookingDtoRequest bookingDtoRequest);

    @Transactional
    BookingDtoResponse approvedBooking(int userId, int bookingId, Boolean approved);

    BookingDtoResponse getBookingById(int userId, int bookingId);

    List<BookingDtoResponse> getBookingsByBooker(int userId, String state);

    List<BookingDtoResponse> getBookingsByOwner(int userId, String state);
}