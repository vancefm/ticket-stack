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

    @Override
    @GetMapping
    public ResponseEntity<List<Contact>> getAll() {
        return new ResponseEntity<>(contactService.getAll(), HttpStatus.OK);
    }

    /**
     * Get a contact by ID number
     * @param id The integer id of a contact
     * @return A single Contact object
     */

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Contact> getByID(@PathVariable Integer id) {
        return new ResponseEntity<>(contactService.getByID(id), HttpStatus.OK);
    }

    /**
     * Create a new contact
     * @param contact A Contact object
     * @return String "Done"
     */

    @Override
    @PostMapping
    @ResponseBody
    public ResponseEntity<Contact> create(@Valid @RequestBody Contact contact) {
        Contact resultContact = contactService.create(contact);
        return new ResponseEntity<>(resultContact, HttpStatus.CREATED);
    }

    /**
     * Update a contact
     * @param contact A Contact object
     * @return String "Done"
     */
    @Override
    @PutMapping
    @ResponseBody
    public ResponseEntity<Contact> update(@RequestBody Contact contact) {
        Contact resultContact = contactService.update(contact);
        return new ResponseEntity<>(resultContact, HttpStatus.CREATED);
    }

    /**
     * Delete a contact
     * @param id The integer id of a contact
     * @return String "Contact Deleted"
     */
    @Override
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        contactService.delete(id);
        return new ResponseEntity<>("Contact deleted", HttpStatus.OK);
    }
}
