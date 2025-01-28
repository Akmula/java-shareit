package ru.practicum.shareit.item;

import java.util.List;

public interface ItemRepository {
    Item addItem(Item item);

    Item updateItem(Item item);

    Item getItemById(int itemId);

    List<Item> getItems(int userId);

    List<Item> searchItems(int userId, String text);
}