package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRepositoryImpl implements ItemRepository {

    private Integer itemId = 0;
    private final Map<Integer, Item> items = new HashMap<>();

    @Override
    public Item addItem(Item item) {
        int id = getId();
        item.setId(id);
        items.put(id, item);
        return item;
    }

    @Override
    public Item updateItem(int itemId, Item item) {
        item.setId(itemId);
        items.put(itemId, item);
        return item;
    }

    @Override
    public Item getItemById(int itemId) {
        return items.get(itemId);
    }

    @Override
    public List<Item> getItems(int userId) {
        return items.values().stream().filter(item -> item.getOwnerId() == userId).toList();
    }

    @Override
    public List<Item> searchItems(int userId, String text) {
        return items.values()
                .stream()
                .filter(Item::isAvailable)
                .filter(item -> item.getName().toLowerCase().contains(text)
                                || item.getDescription().toLowerCase().contains(text))
                .toList();
    }

    private Integer getId() {
        itemId++;
        return itemId;
    }
}