package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository <Item, Long> {
    List<Item> findAllByOwner(User itemOwner);

    @Query("SELECT i FROM Item i" +
            " WHERE UPPER(i.name) LIKE %:text% AND i.available = true" +
            " OR UPPER(i.description) LIKE %:text% AND i.available = true")
    List<Item> searchItemsByText(String text);
}