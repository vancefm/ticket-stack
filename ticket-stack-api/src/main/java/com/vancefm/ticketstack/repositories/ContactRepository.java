package com.vancefm.ticketstack.repositories;

import com.vancefm.ticketstack.entities.Contact;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Component
public interface ContactRepository extends CrudRepository<Contact, Integer> {

    @Override
    Iterable<Contact> findAll();

    @Override
    <S extends Contact> S save(S entity);

    @Override
    Optional<Contact> findById(Integer integer);

    @Override
    void deleteById(Integer integer);
}
