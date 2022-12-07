package com.vancefm.ticketstack.controllers;

import com.vancefm.ticketstack.pojos.Ticket;
import com.vancefm.ticketstack.services.TicketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/ticket")
public class TicketController implements BasicController<Ticket>{

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    /**
     * Get a list of all tickets
     *
     * @return ArrayList of Ticket objects
     */
    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<Ticket>> getAll(){
        List<Ticket> resultList = ticketService.getAll();
        if(resultList != null){
            return new ResponseEntity<>(ticketService.getAll(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    /**
     * Get a ticket by ID number
     *
     * @param pathId The integer id of a ticket
     * @return A single Ticket object
     */
    @Override
    @GetMapping(value = "/{pathId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Ticket> getByID(@PathVariable Integer pathId) {

        Ticket result = ticketService.getByID(pathId);
        if(result != null){
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Create a new ticket
     *
     * @param ticket A Ticket object
     * @return The created ticket
     */
    @Override
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Ticket> create(@Valid @RequestBody Ticket ticket){
        try {
            Ticket resultTicket = ticketService.create(ticket);
            return new ResponseEntity<>(resultTicket, HttpStatus.CREATED);
        } catch (DuplicateKeyException e) {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "Unable to create Ticket. Ticket already exists.", e);
        }
    }

    /**
     * Update a ticket
     *
     * @param ticket A Ticket object
     * @return The updated ticket
     */
    @Override
    @PutMapping(value = "/{pathId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Ticket> update(@PathVariable Integer pathId, @Valid @RequestBody Ticket ticket){
        Ticket resultTicket = ticketService.update(pathId, ticket);
        return new ResponseEntity<>(resultTicket, HttpStatus.OK);
    }

    /**
     * Delete a ticket
     *
     * @param pathId The integer id of a ticket
     * @return The deleted ticket
     */
    @Override
    @DeleteMapping(value = "/{pathId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Ticket> delete(@PathVariable Integer pathId){
        Ticket resultTicket = ticketService.delete(pathId);
        if(resultTicket != null){
            return new ResponseEntity<>(resultTicket, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }

}
