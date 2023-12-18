package com.vancefm.ticketstack.repositories;

import com.vancefm.ticketstack.entities.TicketStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Component
public interface TicketStatusRepository extends CrudRepository<TicketStatus, Integer> {

    @Override
    Iterable<TicketStatus> findAll();

    @Override
    <S extends TicketStatus> S save(S entity);

    @Override
    Optional<TicketStatus> findById(Integer integer);

    @Override
    void deleteById(Integer integer);
}
