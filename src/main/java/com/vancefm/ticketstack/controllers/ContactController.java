package com.vancefm.ticketstack.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vancefm.ticketstack.pojos.Contact;
import com.vancefm.ticketstack.services.ContactService;
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
@RequestMapping(value = "/contact")
public class ContactController implements BasicController<Contact>{

    private final ContactService contactService;

    private final ObjectMapper objectMapper;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    /**
     * Get a list of all contacts
     *
     * @return ArrayList of Contact objects
     */

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<Contact>> getAll() {
        try {
            List<Contact> resultList = contactService.getAll();
            log.info("Result: " + objectMapper.writeValueAsString(resultList));
            if (resultList != null) {
                return new ResponseEntity<>(contactService.getAll(), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
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
            Contact resultContact = contactService.getByID(pathId);
            log.info("Result: " + objectMapper.writeValueAsString(resultContact));
            if(resultContact != null){
                return new ResponseEntity<>(contactService.getByID(pathId), HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Create a new contact
     * @param contact A Contact object
     * @return The created ticket
     */

    @Override
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Contact> create(@Valid @RequestBody Contact contact) {
        try {
            Contact resultContact = contactService.create(contact);
            log.info("Result: " + objectMapper.writeValueAsString(resultContact));
            return new ResponseEntity<>(resultContact, HttpStatus.CREATED);
        } catch (DuplicateKeyException e) {
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "Unable to create Contact. Contact already exists.", e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Update a contact
     * @param contact A Contact object
     * @return The updated ticket
     */
    @Override
    @PutMapping(value = "/{pathId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Contact> update(@PathVariable Integer pathId, @Valid @RequestBody Contact contact) {
        try {
            Contact resultContact = contactService.update(pathId, contact);
            log.info("Result: " + objectMapper.writeValueAsString(resultContact));
            return new ResponseEntity<>(resultContact, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Delete a contact
     * @param pathId The integer id of a contact
     * @return The deleted contact
     */
    @Override
    @DeleteMapping(value = "/{pathId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Contact> delete(@PathVariable Integer pathId) {
        try {
            Contact resultContact = contactService.delete(pathId);
            log.info("Result: " + objectMapper.writeValueAsString(resultContact));
            if(resultContact != null){
                return new ResponseEntity<>(resultContact, HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
