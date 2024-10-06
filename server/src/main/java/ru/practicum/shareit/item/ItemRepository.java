package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByOwnerId(Long id);

    @Query(value = """
            select i from Item i
            where (lower(i.name) like lower(concat('%', ?1, '%'))
            or lower(i.description) like lower(concat('%', ?1, '%')))
            and i.available is true
            """)
    List<Item> searchTextInNameOrDescription(String text);

    List<Item> findAllByRequestId(Long requestId);
}
