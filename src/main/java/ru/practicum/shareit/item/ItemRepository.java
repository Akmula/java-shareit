package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.user.User;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    List<Item> findAllItemsByUserOrderByIdAsc(User user);

    @Query("""
            SELECT i FROM Item AS i
            WHERE i.name ILIKE (CONCAT('%', :text, '%'))
            OR i.description ILIKE (CONCAT('%', :text, '%'))
            AND i.available IS TRUE
            """)
    List<Item> search(String text);
}