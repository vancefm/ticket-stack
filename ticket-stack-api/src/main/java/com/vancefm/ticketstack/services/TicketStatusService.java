package com.vancefm.ticketstack.services;

import com.vancefm.ticketstack.entities.TicketStatus;
import com.vancefm.ticketstack.repositories.TicketStatusRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TicketStatusService implements BasicRestServiceFactory<TicketStatus> {

    @Autowired
    private TicketStatusRepository ticketStatusRepository;

    @Override
    public Optional<List<TicketStatus>> getAll() {
        return Optional.ofNullable((List<TicketStatus>) ticketStatusRepository.findAll());
    }

    @Override
    public Optional<TicketStatus> getByID(Integer id) {
        return ticketStatusRepository.findById(id);
    }

    @Override
    public Optional<TicketStatus> create(TicketStatus ticketStatus) {
        try {
            return Optional.ofNullable(ticketStatusRepository.save(ticketStatus));
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            log.error(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public TicketStatus update(TicketStatus ticketStatus) {
        return ticketStatusRepository.save(ticketStatus);
    }

    @Override
    public void delete(Integer id) {
        ticketStatusRepository.deleteById(id);
    }
}
