package com.vancefm.ticketstack.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vancefm.ticketstack.pojos.Ticket;
import com.vancefm.ticketstack.services.TicketService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
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

    private final ObjectMapper objectMapper;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
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
        try {
            List<Ticket> resultList = ticketService.getAll();
            log.info("Result: " + objectMapper.writeValueAsString(resultList));
            if(resultList != null){
                return new ResponseEntity<>(ticketService.getAll(), HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
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

        try {
            Ticket resultTicket = ticketService.getByID(pathId);
            log.info("Result: " + objectMapper.writeValueAsString(resultTicket));
            if(resultTicket != null){
                return new ResponseEntity<>(resultTicket, HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
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
            log.info("Result: " + objectMapper.writeValueAsString(resultTicket));
            return new ResponseEntity<>(resultTicket, HttpStatus.CREATED);
        } catch (DuplicateKeyException e) {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "Unable to create Ticket. Ticket already exists.", e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
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
        try {
            Ticket resultTicket = ticketService.update(pathId, ticket);
            log.info("Result: " + objectMapper.writeValueAsString(resultTicket));
            return new ResponseEntity<>(resultTicket, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
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
        try {
            Ticket resultTicket = ticketService.delete(pathId);
            log.info("Result: " + objectMapper.writeValueAsString(resultTicket));
            if(resultTicket != null){
                return new ResponseEntity<>(resultTicket, HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

}
