package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    List<Item> findAllItemsByUserIdOrderByIdAsc(Integer userId);

    List<Item> findAllItemsByItemRequestIdOrderByIdAsc(Integer requestId);

    @Query("""
            SELECT i FROM Item AS i
            WHERE i.available IS TRUE
            AND i.name ILIKE (CONCAT('%', :text, '%'))
            OR i.description ILIKE (CONCAT('%', :text, '%'))
            """)
    List<Item> search(String text);
}