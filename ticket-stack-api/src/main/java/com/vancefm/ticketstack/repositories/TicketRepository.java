package com.vancefm.ticketstack.repositories;

import com.vancefm.ticketstack.entities.Ticket;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Component
public interface TicketRepository extends CrudRepository<Ticket, Integer> {

    @Override
    Iterable<Ticket> findAll();

    @Override
    <S extends Ticket> S save(S entity);

    @Override
    Optional<Ticket> findById(Integer integer);

    @Override
    void deleteById(Integer integer);

}
