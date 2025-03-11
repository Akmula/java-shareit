package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    //Получение списка всех бронирований пользователя:
    // Параметр state равен ALL.
    List<Booking> findAllBookingsByBookerIdOrderByStartDesc(int bookerId);

    //Параметр state равен CURRENT.
    @Query("""
            SELECT b from Booking AS b
            WHERE b.booker.id = :bookerId
            AND b.start <= :time
            AND b.end >= :time
            """)
    List<Booking> findAllCurrentBookingsByBookerId(int bookerId, LocalDateTime time);

    //Параметр state равен PAST.
    List<Booking> findAllBookingsByBookerIdAndEndBeforeOrderByStartDesc(int bookerId, LocalDateTime time);

    //Параметр state равен FUTURE.
    List<Booking> findAllBookingsByBookerIdAndStartAfterOrderByStartDesc(int bookerId, LocalDateTime time);

    //Параметр state равен WAITING.
    //Параметр state равен REJECTED.
    List<Booking> findAllBookingsByBookerIdAndStatusOrderByStartDesc(int bookerId, BookingStatus status);

    //Получение списка всех бронирований владельца:
    // Параметр state равен ALL.
    List<Booking> findAllBookingsByItemIdInOrderByStartDesc(Set<Integer> itemIds);

    //Параметр state равен CURRENT.
    @Query("""
            SELECT b from Booking AS b
            WHERE b.item.user.id = :ownerId
            AND b.start <= :time
            AND b.end >= :time
            """)
    List<Booking> findAllCurrentBookingsByOwnerId(int ownerId, LocalDateTime time);

    //Параметр state равен PAST.
    List<Booking> findAllBookingsByItemUserIdAndEndBeforeOrderByStartDesc(int ownerId, LocalDateTime time);

    //Параметр state равен FUTURE.
    List<Booking> findAllBookingsByItemUserIdAndStartAfterOrderByStartDesc(int ownerId, LocalDateTime time);

    //Параметр state равен WAITING.
    //Параметр state равен REJECTED.
    List<Booking> findAllBookingsByItemUserIdAndStatusOrderByStartDesc(int ownerId, BookingStatus status);
}