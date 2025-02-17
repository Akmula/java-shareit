package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDtoResponse createBooking(@RequestHeader("X-Sharer-User-Id") int userId,
                                            @Valid @RequestBody BookingDtoRequest bookingDtoRequest) {
        log.info("Бронирование предмета: {}", bookingDtoRequest);
        BookingDtoResponse bookingDtoResponse = bookingService.createBooking(userId, bookingDtoRequest);
        log.info("Предмет: {} - забронирована", bookingDtoResponse);
        return bookingDtoResponse;
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoResponse approvedBooking(@RequestHeader("X-Sharer-User-Id") int userId,
                                              @PathVariable int bookingId,
                                              @RequestParam Boolean approved) {
        if (approved) {
            log.info("Подтверждение бронирования предмета владельцем: {}", userId);
        } else {
            log.info("Отклонение бронирования предмета владельцем: {}", userId);
        }
        BookingDtoResponse bookingDtoResponse = bookingService.approvedBooking(userId, bookingId, approved);
        if (approved) {
            log.info("Бронирование: {} - подтверждено", bookingDtoResponse);
        } else {
            log.info("Бронирование: {} - отклонено", bookingDtoResponse);
        }
        return bookingDtoResponse;
    }

    @GetMapping("/{bookingId}")
    public BookingDtoResponse getBookingById(@RequestHeader("X-Sharer-User-Id") int userId,
                                             @PathVariable int bookingId) {
        log.info("Получение данных о бронировании с id: {}", bookingId);
        BookingDtoResponse bookingDtoResponse = bookingService.getBookingById(userId, bookingId);
        log.info("Получены данные о бронировании: {}", bookingDtoResponse);
        return bookingDtoResponse;
    }

    @GetMapping
    public List<BookingDtoResponse> getBookingsByBooker(@RequestHeader("X-Sharer-User-Id") int userId,
                                                        @RequestParam(defaultValue = "ALL") String state) {
        log.info("Получение списка всех бронирований текущего пользователя c id: {}, со статусом - {}.", userId, state);
        List<BookingDtoResponse> bookingDtoResponses = bookingService
                .getBookingsByBooker(userId, state);
        log.info("Получен список всех бронирований: {}", bookingDtoResponses);
        return bookingDtoResponses;
    }

    @GetMapping("/owner")
    public List<BookingDtoResponse> getBookingsByOwner(@RequestHeader("X-Sharer-User-Id") int userId,
                                                       @RequestParam(defaultValue = "ALL") String state) {
        log.info("Получение списка бронирований пользователя c id: {}, со статусом - {}.", userId, state);
        List<BookingDtoResponse> bookingDtoResponses = bookingService
                .getBookingsByOwner(userId, state);
        log.info("Получен список бронирований: {}", bookingDtoResponses);
        return bookingDtoResponses;
    }
}