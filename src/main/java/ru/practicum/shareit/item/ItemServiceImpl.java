package ru.practicum.shareit.item;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    @Override
    public CreatedItemDtoResponse createItem(int userId, ItemDtoRequest itemDtoRequest) {
        User user = getUser(userId);
        Item createdItem = itemRepository.save(ItemMapper.dtoToItem(itemDtoRequest, user, null));
        return ItemMapper.createdItemDtoResponse(createdItem);
    }

    @Override
    public ItemDtoResponse updateItem(int userId, int itemId, ItemDtoRequest itemDtoRequest) {
        User oldUser = getUser(userId);
        Item oldItem = getItem(itemId);

        if (oldItem.getUser().getId() != userId) {
            throw new ValidationException(oldUser + " - не является владельцем предмета " + oldItem);
        }
        if (itemDtoRequest.getName() == null || itemDtoRequest.getName().isEmpty()) {
            itemDtoRequest.setName(oldItem.getName());
        }
        if (itemDtoRequest.getDescription() == null || itemDtoRequest.getDescription().isEmpty()) {
            itemDtoRequest.setDescription(oldItem.getDescription());
        }
        if (itemDtoRequest.getAvailable() == null) {
            itemDtoRequest.setAvailable(oldItem.isAvailable());
        }

        Item updatedItem = itemRepository.save(ItemMapper.dtoToItem(itemDtoRequest, oldUser, itemId));

        return ItemMapper.itemToDtoResponse(updatedItem);
    }

    @Override
    public ItemDto getItemById(int userId, int itemId) {
        getUser(userId);
        Item item = getItem(itemId);
        ItemDtoResponse itemDtoResponse = ItemMapper.itemToDtoResponse(item);

        List<CommentDtoResponse> commentDtoResponses = commentRepository.getAllCommentsByItemId(itemId)
                .stream()
                .map(CommentMapper::commentToDtoResponse)
                .toList();

        Booking nextBooking = getNextBooking(userId);
        Booking lastBooking = getLastBooking(userId);

        return ItemMapper.toItemDto(userId, itemDtoResponse, commentDtoResponses,
                BookingMapper.toItemBookingDto(nextBooking),
                BookingMapper.toItemBookingDto(lastBooking));

    }

    @Override
    public List<ItemDto> getItems(int userId) {
        User user = getUser(userId);
        List<Item> items = itemRepository.findAllItemsByUserOrderByIdAsc(user);
        List<ItemDto> itemsDto = new ArrayList<>();

        for (Item item : items) {
            ItemDtoResponse itemDtoResponse = ItemMapper.itemToDtoResponse(item);
            List<CommentDtoResponse> commentDtoResponses = commentRepository.getAllCommentsByItemId(item.getId())
                    .stream()
                    .map(CommentMapper::commentToDtoResponse)
                    .toList();

            Booking nextBooking = getNextBooking(userId);
            Booking lastBooking = getLastBooking(userId);

            ItemDto itemDto = ItemMapper.toItemDto(userId, itemDtoResponse, commentDtoResponses,
                    BookingMapper.toItemBookingDto(nextBooking),
                    BookingMapper.toItemBookingDto(lastBooking));
            itemsDto.add(itemDto);
        }
        return itemsDto;
    }

    @Override
    public List<ItemDtoResponse> searchItems(int userId, String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        List<Item> items = itemRepository.search(text);

        return items.stream()
                .map(ItemMapper::itemToDtoResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDtoResponse addComment(int userId, int itemId, CommentDtoRequest commentDtoRequest) {

        User user = getUser(userId);
        Item item = getItem(itemId);
        Set<Integer> itemIds = new HashSet<>();
        itemIds.add(itemId);

        List<Booking> bookings = bookingRepository.findAllBookingsByItemIdInOrderByStartDesc(itemIds);

        if (bookings.isEmpty()) {
            throw new ValidationException("Для предмета с id: " + itemId + " бронирования не было.");
        }

        Booking booking = bookings.getFirst();

        if (booking.getBooker().getId() != userId || booking.getEnd().isAfter(LocalDateTime.now())) {
            throw new ValidationException("Пользователь с id: " + userId +
                                          " не бронировал предмет с id: " + itemId +
                                          " или срок бронирования не истек!");
        }

        Comment comment = CommentMapper.dtoToComment(commentDtoRequest, item, user);
        comment.setCreated(LocalDateTime.now());

        return CommentMapper.commentToDtoResponse(commentRepository.save(comment));
    }

    private User getUser(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id - " + userId + " не найден!"));
    }

    private Item getItem(int itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Предмет с id - " + itemId + " не найден!"));
    }

    private Booking getNextBooking(int userId) {
        List<Booking> bookings = bookingRepository
                .findAllBookingsByItemUserIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now());

        Booking booking = new Booking();

        if (bookings != null && !bookings.isEmpty()) {
            booking = bookings.getFirst();
        }
        return booking;
    }

    private Booking getLastBooking(int userId) {
        List<Booking> bookings = bookingRepository
                .findAllBookingsByItemUserIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now());

        Booking booking = new Booking();

        if (bookings != null && !bookings.isEmpty()) {
            booking = bookings.getFirst();
        }
        return booking;
    }
}