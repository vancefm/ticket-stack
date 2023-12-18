package com.vancefm.ticketstack.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vancefm.ticketstack.entities.Contact;
import com.vancefm.ticketstack.kafka.KafkaProducer;
import com.vancefm.ticketstack.services.ContactService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Contact controller
 * Performs basic CRUD operations for Contacts
 */
@Slf4j
@RestController
@RequestMapping(value = "/contact")
public class ContactController implements BasicControllerFactory<Contact> {

    private final ContactService contactService;

    private final KafkaProducer kafkaProducer;

    private final ObjectMapper objectMapper;

    public ContactController(ContactService contactService, KafkaProducer kafkaProducer) {
        this.contactService = contactService;
        this.kafkaProducer = kafkaProducer;
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    /**
     * Get a list of all contacts
     * @return List of Contact objects
     */

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<Contact>> getAll() {
        try {
            Optional<List<Contact>> resultList = contactService.getAll();
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
     * Get a contact by ID number
     * @param pathId The integer id of a contact
     * @return A single Contact object
     */

    @Override
    @GetMapping(value = "/{pathId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Contact> getByID(@PathVariable Integer pathId) {
        try {
            Optional<Contact> resultContact = contactService.getByID(pathId);
            if(resultContact.isPresent()){
                log.info("Result: " + objectMapper.writeValueAsString(resultContact));
                return new ResponseEntity<>(resultContact.get(), HttpStatus.OK);
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
     * Create a new contact
     * @param contact A Contact object
     * @return The created contact
     */

    @Override
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Contact> create(@Valid @RequestBody Contact contact) {
        try {
            log.info("Creating contact from: " + objectMapper.writeValueAsString(contact));
            Optional<Contact> resultContact = contactService.create(contact);
            if(resultContact.isPresent()){
                log.info("Result: " + objectMapper.writeValueAsString(resultContact));
                kafkaProducer.sendMessage("Contact was created -> " + objectMapper.writeValueAsString(resultContact));
                return new ResponseEntity<>(resultContact.get(), HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Update a contact
     * @param contact A Contact object
     * @return The updated contact
     */
    @Override
    @PutMapping(value = "/{pathId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Contact> update(@PathVariable Integer pathId, @Valid @RequestBody Contact contact) {
        try {
            if(!Objects.equals(pathId, contact.getId())) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            log.info("Updating ticket to: " + objectMapper.writeValueAsString(contact));
            Contact resultContact = contactService.update(contact);
            if (resultContact != null) {
                log.info("Result: " + objectMapper.writeValueAsString(resultContact));
                kafkaProducer.sendMessage("Contact was updated -> " + objectMapper.writeValueAsString(resultContact));
                return new ResponseEntity<>(resultContact, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete a contact
     *
     * @param pathId The integer id of a contact
     * @return ResponseEntity with status
     */
    @Override
    @DeleteMapping(value = "/{pathId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Contact> delete(@PathVariable Integer pathId) {
        contactService.delete(pathId);
        log.info("Deleted contact with id: " + pathId);
        kafkaProducer.sendMessage("Contact with id was deleted -> " + pathId);
        return new ResponseEntity<>(HttpStatus.OK);

    }
}
