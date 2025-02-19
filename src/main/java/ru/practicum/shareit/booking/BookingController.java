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
        BookingDtoResponse bookingResponse = bookingService.createBooking(userId, bookingDtoRequest);
        log.info("Предмет: {} - забронирована", bookingResponse);
        return bookingResponse;
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoResponse approvedBooking(@RequestHeader("X-Sharer-User-Id") int userId,
                                              @PathVariable int bookingId,
                                              @RequestParam boolean approved) {
        if (approved) {
            log.info("Подтверждение бронирования предмета владельцем: {}", userId);
        } else {
            log.info("Отклонение бронирования предмета владельцем: {}", userId);
        }
        BookingDtoResponse bookingResponse = bookingService.approvedBooking(userId, bookingId, approved);
        if (approved) {
            log.info("Бронирование: {} - подтверждено", bookingResponse);
        } else {
            log.info("Бронирование: {} - отклонено", bookingResponse);
        }
        return bookingResponse;
    }

    @GetMapping("/{bookingId}")
    public BookingDtoResponse getBookingById(@RequestHeader("X-Sharer-User-Id") int userId,
                                             @PathVariable int bookingId) {
        log.info("Получение данных о бронировании с id: {}", bookingId);
        BookingDtoResponse bookingResponse = bookingService.getBookingById(userId, bookingId);
        log.info("Получены данные о бронировании: {}", bookingResponse);
        return bookingResponse;
    }

    @GetMapping
    public List<BookingDtoResponse> getBookingsByBooker(@RequestHeader("X-Sharer-User-Id") int userId,
                                                        @RequestParam(defaultValue = "ALL") State state) {
        log.info("Получение списка всех бронирований текущего пользователя c id: {}, со статусом - {}.", userId, state);
        List<BookingDtoResponse> bookingResponses = bookingService.getBookingsByBooker(userId, state);
        log.info("Получен список всех бронирований: {}", bookingResponses);
        return bookingResponses;
    }

    @GetMapping("/owner")
    public List<BookingDtoResponse> getBookingsByOwner(@RequestHeader("X-Sharer-User-Id") int userId,
                                                       @RequestParam(defaultValue = "ALL") State state) {
        log.info("Получение списка бронирований пользователя c id: {}, со статусом - {}.", userId, state);
        List<BookingDtoResponse> bookingResponses = bookingService.getBookingsByOwner(userId, state);
        log.info("Получен список бронирований: {}", bookingResponses);
        return bookingResponses;
    }
}