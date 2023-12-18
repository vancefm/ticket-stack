package com.vancefm.ticketstack.services;

import com.vancefm.ticketstack.entities.Contact;
import com.vancefm.ticketstack.repositories.ContactRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ContactServiceTest {

    @Mock
    ContactRepository contactRepository;

    @InjectMocks
    ContactService contactService;

    @Test
    public void shouldGetAllContacts_andReturnContactList(){
        Contact contactOne = new Contact(1, "user1@localhost.com", "UserOne", "Person");
        Contact contactTwo = new Contact(2, "user2@localhost.com", "UserTwo", "Person");
        Contact contactThree = new Contact(3, "user3@localhost.com", "UserTwo", "Person");

        when(contactRepository.findAll()).thenReturn(Arrays.asList(contactOne,contactTwo,contactThree));

        Optional<List<Contact>> resultList = contactService.getAll();
        resultList.ifPresentOrElse(list -> {
            assertFalse(list.isEmpty());
            assertEquals(3, list.size());
            list.forEach(contact -> System.out.println(contact.getEmailAddress()));
        },
                Assertions::fail
        );
    }
    
    @Test
    public void shouldGetAContactById_andReturnAContact(){
        Contact contactOne = new Contact(1, "user1@localhost.com", "UserOne", "Person");

        when(contactRepository.findById(contactOne.getId())).thenReturn(Optional.of(contactOne));

        Optional<Contact> resultContact = contactService.getByID(contactOne.getId());
        resultContact.ifPresentOrElse(contact ->
                System.out.println(contact.getEmailAddress()),
                Assertions::fail
        );
    }

    @Test
    public void shouldCreateAContact_andReturnTheSameContactWithAnID(){
        Contact contactOne = new Contact(null, "user@localhost.com", "Integration", "Testing");
        Contact contactTwo = new Contact(1, "user@localhost.com", "Integration", "Testing");

        when(contactRepository.save(contactOne)).thenReturn(contactTwo);

        Optional<Contact> resultContact = contactService.create(contactOne);

        resultContact.ifPresentOrElse(contact ->
                {
                    if(contact.getEmailAddress().equals(contactTwo.getEmailAddress())
                            && contact.getId() != null) {
                        System.out.println(contact.getEmailAddress());
                    } else {
                        Assertions.fail();
                    }
                },
                Assertions::fail
        );
    }

    @Test
    public void shouldDeleteAContact_andReturnNothing(){
        Contact contactOne = new Contact(1, "user@localhost.com", "Integration", "Testing");

        assertDoesNotThrow(() -> contactRepository.deleteById(contactOne.getId()));
    }
}
