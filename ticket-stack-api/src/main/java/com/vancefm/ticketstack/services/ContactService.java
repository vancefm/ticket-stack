package com.vancefm.ticketstack.services;

import com.vancefm.ticketstack.entities.Contact;
import com.vancefm.ticketstack.repositories.ContactRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ContactService implements BasicRestServiceFactory<Contact> {

    @Autowired
    private ContactRepository contactRepository;

    @Override
    public Optional<List<Contact>> getAll() {
        return Optional.ofNullable((List<Contact>) contactRepository.findAll());
    }

    @Override
    public Optional<Contact> getByID(Integer id) {
        return contactRepository.findById(id);
    }

    @Override
    public Optional<Contact> create(Contact contact) {
        try {
            return Optional.ofNullable(contactRepository.save(contact));
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            log.error(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Contact update(Contact contact) {
        return contactRepository.save(contact);
    }

    @Override
    public void delete(Integer id) {
        contactRepository.deleteById(id);
    }
}
