package com.vancefm.ticketstack.controllers;

import com.vancefm.ticketstack.pojos.Ticket;
import com.vancefm.ticketstack.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/ticket")
public class TicketController implements BasicController<Ticket>{

    @Autowired
    private TicketService ticketService;

    /**
     * Get a list of all tickets
     *
     * @return ArrayList of Ticket objects
     */
    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<Ticket>> getAll(){
        return new ResponseEntity<>(ticketService.getAll(), HttpStatus.OK);
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
        return new ResponseEntity<>(ticketService.getByID(pathId), HttpStatus.OK);
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
        Ticket resultTicket = ticketService.create(ticket);
        return new ResponseEntity<>(resultTicket, HttpStatus.CREATED);
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
