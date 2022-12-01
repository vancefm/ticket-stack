package com.vancefm.ticketstack.controllers;

import com.vancefm.ticketstack.pojos.Contact;
import com.vancefm.ticketstack.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/contact")
public class ContactController implements BasicController<Contact>{

    @Autowired
    ContactService contactService;

    /**
     * Get a list of all contacts
     *
     * @return ArrayList of Contact objects
     */

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<Contact>> getAll() {
        return new ResponseEntity<>(contactService.getAll(), HttpStatus.OK);
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
        return new ResponseEntity<>(contactService.getByID(pathId), HttpStatus.OK);
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
        Contact resultContact = contactService.create(contact);
        return new ResponseEntity<>(resultContact, HttpStatus.CREATED);
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
        Contact resultContact = contactService.update(pathId, contact);
        return new ResponseEntity<>(resultContact, HttpStatus.OK);
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
        Contact resultContact = contactService.delete(pathId);
        if(resultContact != null){
            return new ResponseEntity<>(resultContact, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
