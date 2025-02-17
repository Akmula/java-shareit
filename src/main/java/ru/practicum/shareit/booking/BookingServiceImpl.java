package ru.practicum.shareit.booking;

import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnknownStateException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public BookingDtoResponse createBooking(int userId, BookingDtoRequest bookingDtoRequest) {
        final int itemId = bookingDtoRequest.getItemId();

        User booker = getUser(userId);
        Item item = getItem(itemId);

        if (booker.getId().equals(item.getUser().getId())) {
            throw new ValidationException("Владелец не может забронировать свой предмет!");
        }

        if (!item.isAvailable()) {
            throw new ValidationException("Предмет - " + item + ", не доступен для бронирования!");
        }

        if (bookingDtoRequest.getStart().equals(bookingDtoRequest.getEnd())) {
            throw new ValidationException("Дата начала бронирования равна дате окончания!");
        }

        if (bookingDtoRequest.getStart().isAfter(bookingDtoRequest.getEnd())) {
            throw new ValidationException("Дата окончания бронирования раньше даты начала бронирования!");
        }

        Booking booking = BookingMapper.toBooking(bookingDtoRequest, booker, item);
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDtoResponse approvedBooking(int userId, int bookingId, Boolean approved) {

        Booking booking = getBooking(bookingId);
        final int itemId = booking.getItem().getId();
        Item item = getItem(itemId);
        final int ownerId = item.getUser().getId();

        if (ownerId != userId) {
            throw new ValidationException(" У '" + item.getUser().getName() +
                                          "' - не найден предмет: '" + item.getName() + "'!");
        }

        if (booking.getStatus().equals(BookingStatus.APPROVED)) {
            throw new ValidationException("Предмет уже имеет статус: '" + booking.getStatus() + "'!");
        }

        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        Booking approvedBooking = bookingRepository.save(booking);

        return BookingMapper.toBookingDto(approvedBooking);
    }

    @Override
    public BookingDtoResponse getBookingById(int userId, int bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование с id - " + bookingId + " не найдено!"));

        User owner = booking.getItem().getUser();
        User booker = booking.getBooker();

        int ownerId = owner.getId();
        int bookerId = booker.getId();

        if (ownerId != userId && bookerId != userId) {
            throw new NotFoundException("У '" + owner.getName() + "' и '" + booker.getName() +
                                        "' не найден предмет: '" + booking.getItem().getName() + "'!");
        }

        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDtoResponse> getBookingsByBooker(int bookerId, String state) {

        getUser(bookerId);
        List<Booking> bookingDtoResponses;

        switch (state.toUpperCase()) {
            case "ALL" -> {
                log.info("Запрос на получение всех бронирований пользователя с id: {}", bookerId);
                bookingDtoResponses = bookingRepository.findAllBookingsByBookerIdOrderByStartDesc(bookerId);
            }
            case "CURRENT" -> {
                log.info("Запрос на получение текущих бронирований пользователя с id: {}", bookerId);
                bookingDtoResponses = bookingRepository.findAllCurrentBookingsByBookerId(bookerId, LocalDateTime.now());
            }
            case "PAST" -> {
                log.info("Запрос на получение завершенных бронирований пользователя с id: {}", bookerId);
                bookingDtoResponses = bookingRepository
                        .findAllBookingsByBookerIdAndEndBeforeOrderByStartDesc(bookerId, LocalDateTime.now());
            }
            case "FUTURE" -> {
                log.info("Запрос на получение будущих бронирований пользователя с id: {}", bookerId);
                bookingDtoResponses = bookingRepository
                        .findAllBookingsByBookerIdAndStartAfterOrderByStartDesc(bookerId, LocalDateTime.now());
            }
            case "WAITING" -> {
                log.info("Запрос на получение бронирований пользователя с id: {}, ожидающих подтверждения!", bookerId);
                bookingDtoResponses = bookingRepository
                        .findAllBookingsByBookerIdAndStatusOrderByStartDesc(bookerId, BookingStatus.WAITING);
            }
            case "REJECTED" -> {
                log.info("Запрос на получение отклоненных бронирований пользователя с id: {}", bookerId);
                bookingDtoResponses = bookingRepository
                        .findAllBookingsByBookerIdAndStatusOrderByStartDesc(bookerId, BookingStatus.REJECTED);
            }
            default -> {
                log.error("Unknown state: {}, from booker", state);
                throw new UnknownStateException("Unknown state: " + state);
            }
        }
        return bookingDtoResponses.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }

    @Override
    public List<BookingDtoResponse> getBookingsByOwner(int ownerId, String state) {

        User user = getUser(ownerId);
        Set<Integer> itemIds = itemRepository
                .findAllItemsByUserOrderByIdAsc(user).stream().map(Item::getId).collect(Collectors.toSet());
        List<Booking> bookingDtoResponses;

        switch (state.toUpperCase()) {
            case "ALL" -> {
                log.info("Запрос на получение всех бронирований владельцем с id: {}!", ownerId);
                bookingDtoResponses = bookingRepository.findAllBookingsByItemIdInOrderByStartDesc(itemIds);
            }
            case "CURRENT" -> {
                log.info("Запрос на получение текущих бронирований владельцем с id: {}!", ownerId);
                bookingDtoResponses = bookingRepository.findAllCurrentBookingsByOwnerId(ownerId, LocalDateTime.now());
            }
            case "PAST" -> {
                log.info("Запрос на получение завершенных бронирований владельцем с id: {}!", ownerId);
                bookingDtoResponses = bookingRepository
                        .findAllBookingsByItemUserIdAndEndBeforeOrderByStartDesc(ownerId, LocalDateTime.now());
            }
            case "FUTURE" -> {
                log.info("Запрос на получение будущих бронирований владельцем с id: {}!", ownerId);
                bookingDtoResponses = bookingRepository
                        .findAllBookingsByItemUserIdAndStartAfterOrderByStartDesc(ownerId, LocalDateTime.now());
            }
            case "WAITING" -> {
                log.info("Запрос на получение бронирований владельцем с id: {}, ожидающих подтверждение!", ownerId);
                bookingDtoResponses = bookingRepository
                        .findAllBookingsByItemUserIdAndStatusOrderByStartDesc(ownerId, BookingStatus.WAITING);

            }
            case "REJECTED" -> {
                log.info("Запрос на получение отклоненных бронирований владельцем с id: {}!", ownerId);
                bookingDtoResponses = bookingRepository
                        .findAllBookingsByItemUserIdAndStatusOrderByStartDesc(ownerId, BookingStatus.REJECTED);
            }
            default -> {
                log.error("Unknown state: {}, from owner", state);
                throw new UnknownStateException("Unknown state: " + state);
            }
        }
        return bookingDtoResponses.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }

    private User getUser(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id - " + userId + " не найден!"));
    }

    private Item getItem(int itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Предмет с id - " + itemId + " не найден!"));
    }

    private Booking getBooking(int bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование с id - " + bookingId + " не найдено!"));
    }
}