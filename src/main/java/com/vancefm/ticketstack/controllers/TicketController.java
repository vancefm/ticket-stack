package com.vancefm.ticketstack.controllers;

import com.vancefm.ticketstack.pojos.Ticket;
import com.vancefm.ticketstack.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @GetMapping
    @ResponseBody
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
    @ResponseBody
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
    @ResponseBody
    public ResponseEntity<Ticket> create(@Valid @RequestBody Ticket ticket){
        Ticket resultTicket = ticketService.create(ticket);
        return new ResponseEntity<>(resultTicket, HttpStatus.CREATED);
    }

    /**
     * Update a ticket
     *
     * @param ticket A Ticket object
     * @return String "Done"
     */
    @Override
    @PutMapping
    @ResponseBody
    public ResponseEntity<Ticket> update(@RequestBody Ticket ticket){
        Ticket resultTicket = ticketService.update(ticket);
        return new ResponseEntity<>(resultTicket, HttpStatus.CREATED);
    }

    /**
     * Delete a ticket
     *
     * @param id The integer id of a ticket
     * @return String "Ticket Deleted"
     */
    @Override
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<String> delete(@PathVariable("id") Integer id){
        ticketService.delete(id);
        return new ResponseEntity<>("Ticket Deleted", HttpStatus.OK);
    }

}
