package ru.practicum.shareit.item;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemResponseDto createItem(int userId, ItemRequestDto itemRequestDto) {

        if (userRepository.getUserById(userId) == null) {
            throw new NotFoundException("User not found");
        }

        Item createdItem = ItemMapper.dtoToItem(userId, itemRequestDto);

        createdItem = itemRepository.addItem(createdItem);

        return ItemMapper.itemToResponseDto(createdItem);
    }

    @Override
    public ItemResponseDto updateItem(int userId, int itemId, ItemRequestDto itemRequestDto) {

        if (userRepository.getUserById(userId) == null) {
            throw new NotFoundException("User not found");
        }
        if (itemRepository.getItemById(itemId) == null) {
            throw new NotFoundException("Item not found");
        }

        Item oldItem = itemRepository.getItemById(itemId);

        if (oldItem.getOwnerId() != userId) {
            throw new ValidationException("Item is not owned by user");
        }
        if (itemRequestDto.getName() == null || itemRequestDto.getName().isEmpty()) {
            itemRequestDto.setName(oldItem.getName());
        }
        if (itemRequestDto.getDescription() == null || itemRequestDto.getDescription().isEmpty()) {
            itemRequestDto.setDescription(oldItem.getDescription());
        }
        if (itemRequestDto.getAvailable() == null) {
            itemRequestDto.setAvailable(false);
        }

        Item updatedItem = itemRepository.updateItem(itemId, ItemMapper.dtoToItem(userId, itemRequestDto));

        return ItemMapper.itemToResponseDto(updatedItem);
    }

    @Override
    public ItemResponseDto getItemById(int userId, int itemId) {
        return ItemMapper.itemToResponseDto(itemRepository.getItemById(itemId));
    }

    @Override
    public List<ItemResponseDto> getItems(int userId) {
        return ItemMapper.itemsToResponseDto(itemRepository.getItems(userId));
    }

    @Override
    public List<ItemResponseDto> searchItems(int userId, String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return ItemMapper.itemsToResponseDto(itemRepository.searchItems(userId, text.toLowerCase()));
    }
}