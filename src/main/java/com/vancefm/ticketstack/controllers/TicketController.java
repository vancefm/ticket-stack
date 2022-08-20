package com.vancefm.ticketstack.controllers;

import com.vancefm.ticketstack.pojos.Ticket;
import com.vancefm.ticketstack.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping
    public ResponseEntity<List<Ticket>> getAll(){
        return new ResponseEntity<>(ticketService.getAll(), HttpStatus.OK);
    }

    /**
     * Get a ticket by ID number
     *
     * @param id The integer id of a ticket
     * @return A single Ticket object
     */
    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getByID(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(ticketService.getByID(id), HttpStatus.OK);
    }

    /**
     * Create a new ticket
     *
     * @param ticket A Ticket object
     * @return String "Done"
     */
    @Override
    @PostMapping
    public ResponseEntity<String> create(@RequestBody Ticket ticket){
        ticketService.createOrUpdate(ticket);
        return new ResponseEntity<>("Done", HttpStatus.OK);
    }

    /**
     * Update a ticket
     *
     * @param ticket A Ticket object
     * @return String "Done"
     */
    @Override
    @PutMapping
    public ResponseEntity<String> update(@RequestBody Ticket ticket){
        ticketService.createOrUpdate(ticket);
        return new ResponseEntity<>("Done", HttpStatus.OK);
    }

    /**
     * Delete a ticket
     *
     * @param id The integer id of a ticket
     * @return String "Ticket Deleted"
     */
    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Integer id){
        ticketService.delete(id);
        return new ResponseEntity<>("Ticket Deleted", HttpStatus.OK);
    }

}
