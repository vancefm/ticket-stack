package com.vancefm.ticketstack.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vancefm.ticketstack.kafka.KafkaProducer;
import com.vancefm.ticketstack.entities.TicketStatus;
import com.vancefm.ticketstack.services.TicketStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * This method is used to perform CRUD operations for Ticket Status objects
 */
@Slf4j
@RestController
@RequestMapping(value = "/ticket-status")
public class TicketStatusController implements BasicControllerFactory<TicketStatus> {

    private final TicketStatusService ticketStatusService;

    private final KafkaProducer kafkaProducer;

    private final ObjectMapper objectMapper;

    public TicketStatusController(TicketStatusService ticketStatusService, KafkaProducer kafkaProducer){
        this.ticketStatusService = ticketStatusService;
        this.kafkaProducer = kafkaProducer;
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    /**
     * Get a list of all available Ticket Statuses
     * @return List of Ticket Status objects
     */
    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<TicketStatus>> getAll() {
        try {
            Optional<List<TicketStatus>> resultList = ticketStatusService.getAll();
            if (resultList.isPresent()) {
                log.info("Result: " + objectMapper.writeValueAsString(resultList));
                return new ResponseEntity<>(resultList.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get a ticket status by ID number
     * @param pathId The integer id of a ticket status
     * @return A single TicketStatus object
     */
    @Override
    @GetMapping(value = "/{pathId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<TicketStatus> getByID(@PathVariable Integer pathId) {
        try {
            Optional<TicketStatus> resultStatus = ticketStatusService.getByID(pathId);
            if(resultStatus.isPresent()){
                log.info("Result: " + objectMapper.writeValueAsString(resultStatus));
                return new ResponseEntity<>(resultStatus.get(), HttpStatus.OK);
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
     * Create a new ticket status
     * @param ticketStatus A TicketStatus object
     * @return The created ticket status value
     */
    @Override
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<TicketStatus> create(@Valid @RequestBody TicketStatus ticketStatus) {
        try {
            log.info("Creating ticket status from: " + objectMapper.writeValueAsString(ticketStatus));
            Optional<TicketStatus> resultStatus = ticketStatusService.create(ticketStatus);
            if (resultStatus.isPresent()) {
                log.info("Result: " + objectMapper.writeValueAsString(resultStatus));
                kafkaProducer.sendMessage("Ticket status was created -> " + objectMapper.writeValueAsString(ticketStatus));
                return new ResponseEntity<>(resultStatus.get(), HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Update a TicketStatus
     * @param pathId Integer Value of a ticket status
     * @param ticketStatus The updated ticket status
     * @return A ticket status object
     */
    @Override
    @PutMapping(value = "/{pathId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<TicketStatus> update(@PathVariable Integer pathId, @Valid @RequestBody TicketStatus ticketStatus) {
        try {
            if(pathId != ticketStatus.getId()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            log.info("Updating ticket status object value to:" + objectMapper.writeValueAsString(ticketStatus));
            TicketStatus resultStatus = ticketStatusService.update(ticketStatus);
            if (resultStatus != null) {
                log.info("Result: " + objectMapper.writeValueAsString(resultStatus));
                kafkaProducer.sendMessage("Ticket status object was updated -> " + objectMapper.writeValueAsString(resultStatus));
                return new ResponseEntity<>(resultStatus, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete a Ticket Status
     *
     * @param pathId The integer id of a ticket status
     * @return The deleted ticket status
     */
    @Override
    @DeleteMapping(value = "/{pathId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<TicketStatus> delete(@PathVariable Integer pathId) {
            ticketStatusService.delete(pathId);
            log.info("Ticket status deleted with id: " + pathId);
            kafkaProducer.sendMessage("Ticket status object was deleted -> " + pathId);
            return new ResponseEntity<>(HttpStatus.OK);
    }
}
