package ru.practicum.shareit.request;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface RequestRepository extends JpaRepository<Request, Long> {

    Collection<Request> findAllByRequestorId(Long userId, Sort sort);

}
