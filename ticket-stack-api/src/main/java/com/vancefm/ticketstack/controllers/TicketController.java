package com.vancefm.ticketstack.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vancefm.ticketstack.kafka.KafkaProducer;
import com.vancefm.ticketstack.entities.Ticket;
import com.vancefm.ticketstack.services.TicketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Ticket controller
 * Performs basic CRUD operations for Tickets
 */
@Slf4j
@RestController
@RequestMapping(value = "/ticket")
public class TicketController implements BasicControllerFactory<Ticket> {

    private final TicketService ticketService;

    private final KafkaProducer kafkaProducer;

    private final ObjectMapper objectMapper;

    public TicketController(TicketService ticketService, KafkaProducer kafkaProducer) {
        this.ticketService = ticketService;
        this.kafkaProducer = kafkaProducer;
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    /**
     * Get a list of all tickets
     *
     * @return List of Ticket objects
     */
    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<Ticket>> getAll(){
        try {
            Optional<List<Ticket>> resultList = ticketService.getAll();
            if(resultList.isPresent()){
                log.info("Result: " + objectMapper.writeValueAsString(resultList));
                return new ResponseEntity<>(resultList.get(), HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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
            Optional<Ticket> resultTicket = ticketService.getByID(pathId);
            if(resultTicket.isPresent()){
                log.info("Result: " + objectMapper.writeValueAsString(resultTicket));
                return new ResponseEntity<>(resultTicket.get(), HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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
            log.info("Creating ticket from: " + objectMapper.writeValueAsString(ticket));
            Optional<Ticket> resultTicket = ticketService.create(ticket);
            if(resultTicket.isPresent()){
                log.info("Result ticket: " + objectMapper.writeValueAsString(resultTicket));
                kafkaProducer.sendMessage("Ticket was created -> " + objectMapper.writeValueAsString(resultTicket));
                return new ResponseEntity<>(resultTicket.get(), HttpStatus.CREATED);
            }
            else{
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     *  Update a Ticket
     * @param pathId Integer id of a ticket
     * @param ticket A Ticket object
     * @return The updated ticket
     */
    @Override
    @PutMapping(value = "/{pathId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Ticket> update(@PathVariable Integer pathId, @Valid @RequestBody Ticket ticket){
        try {
            if(pathId != ticket.getId()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            log.info("Updating ticket to: " + objectMapper.writeValueAsString(ticket));
            Ticket resultTicket = ticketService.update(ticket);
            if (resultTicket != null) {
                log.info("Result: " + objectMapper.writeValueAsString(resultTicket));
                kafkaProducer.sendMessage("Ticket was updated -> " + objectMapper.writeValueAsString(resultTicket));
                return new ResponseEntity<>(resultTicket, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete a ticket
     *
     * @param pathId The integer id of a ticket
     * @return ResponseEntity with status
     */
    @Override
    @DeleteMapping(value = "/{pathId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Ticket> delete(@PathVariable Integer pathId) {
        ticketService.delete(pathId);
        log.info("Deleted ticket with id: " + pathId);
        kafkaProducer.sendMessage("Ticket was deleted -> " + pathId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
