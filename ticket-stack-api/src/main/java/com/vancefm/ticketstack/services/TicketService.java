package com.vancefm.ticketstack.services;

import com.vancefm.ticketstack.entities.Ticket;
import com.vancefm.ticketstack.repositories.TicketRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TicketService implements BasicRestServiceFactory<Ticket> {

    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public Optional<List<Ticket>> getAll(){
        return Optional.ofNullable((List<Ticket>) ticketRepository.findAll());
    }

    @Override
    public Optional<Ticket> getByID(Integer id){
        return ticketRepository.findById(id);
    }

    @Override
    public Optional<Ticket> create(Ticket ticket){
        ticket.setCreatedTime(LocalDateTime.now());
        ticket.setUpdatedTime(LocalDateTime.now());
        try {
            return Optional.ofNullable(ticketRepository.save(ticket));
        } catch (DataIntegrityViolationException e) {
            log.error(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Ticket update(Ticket ticket){
        ticket.setUpdatedTime(LocalDateTime.now());
        return ticketRepository.save(ticket);
    }

    @Override
    public void delete(Integer id){
        ticketRepository.deleteById(id);
    }
}
