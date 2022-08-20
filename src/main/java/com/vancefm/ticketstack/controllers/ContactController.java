package com.vancefm.ticketstack.controllers;

import com.vancefm.ticketstack.pojos.Contact;
import com.vancefm.ticketstack.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @GetMapping
    @Override
    public ResponseEntity<List<Contact>> getAll() {
        return new ResponseEntity<>(contactService.getAll(), HttpStatus.OK);
    }

    /**
     * Get a contact by ID number
     * @param id The integer id of a contact
     * @return A single Contact object
     */
    @GetMapping("/{id}")
    @Override
    public ResponseEntity<Contact> getByID(@PathVariable Integer id) {
        return new ResponseEntity<>(contactService.getByID(id), HttpStatus.OK);
    }

    /**
     * Create a new contact
     * @param contact A Contact object
     * @return String "Done"
     */
    @PostMapping
    @Override
    public ResponseEntity<String> create(@Valid @RequestBody Contact contact) {
        contactService.createOrUpdate(contact);
        return new ResponseEntity<>("Done", HttpStatus.OK);
    }

    /**
     * Update a contact
     * @param contact A Contact object
     * @return String "Done"
     */
    @PutMapping
    @Override
    public ResponseEntity<String> update(@RequestBody Contact contact) {
        contactService.createOrUpdate(contact);
        return new ResponseEntity<>("Done", HttpStatus.OK);
    }

    /**
     * Delete a contact
     * @param id The integer id of a contact
     * @return String "Contact Deleted"
     */
    @DeleteMapping("/{id}")
    @Override
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        contactService.delete(id);
        return new ResponseEntity<>("Contact deleted", HttpStatus.OK);
    }
}
