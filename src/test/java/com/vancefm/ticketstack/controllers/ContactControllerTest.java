package com.vancefm.ticketstack.controllers;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vancefm.ticketstack.pojos.Contact;
import com.vancefm.ticketstack.services.ContactService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ContactControllerTest {

    private static final ObjectMapper mapper = JsonMapper.builder()
            .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
            .addModule(new JavaTimeModule())
            .build();

    @MockBean
    private ContactService contactService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void get_shouldGetAllContacts_andReturnStatusOk() throws Exception{

        Contact contactOne = new Contact(1, "user1@localhost.com", "UserOne", "Person");
        Contact contactTwo = new Contact(2, "user2@localhost.com", "UserTwo", "Person");
        Contact contactThree = new Contact(3, "user3@localhost.com", "UserTwo", "Person");
        List<Contact> contactList = Stream.of(contactOne, contactTwo, contactThree).collect(Collectors.toList());

        Mockito.when(contactService.getAll()).thenReturn(contactList);

        MvcResult result = mockMvc
                .perform(get("/contact"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", Matchers.is(3)))
                .andReturn();
    }

    @Test
    public void get_shouldGetAContact_andReturnStatusOk() throws Exception{
        Contact contactOne = new Contact(1, "user1@localhost.com", "UserOne", "Person");

        Mockito.when(contactService.getByID(1)).thenReturn(contactOne);

        MvcResult result = mockMvc
                .perform(get("/contact/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Test
    public void post_shouldCreateAContact_andReturnStatusIsCreated() throws Exception{

        Contact contactOne = new Contact(null, "user1@localhost.com", "UserOne", "Person");
        Contact contactTwo = new Contact(1, "user1@localhost.com", "UserOne", "Person");

        Mockito.when(contactService.create(contactOne)).thenReturn(contactTwo);

        MvcResult result = mockMvc
                .perform(
                        post("/contact")
                                .content(mapper.writeValueAsString(contactOne))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Test
    public void put_shouldUpdateAContact_andReturnStatusIsOk() throws Exception{

        Contact contactOne = new Contact(1, "user1@localhost.com", "UserOne", "Person");

        when(contactService.update(contactOne.getId(), contactOne)).thenReturn(contactOne);

        MvcResult result = mockMvc
                .perform(
                        put("/contact/1")
                                .content(mapper.writeValueAsString(contactOne))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

    }

    @Test
    public void delete_shouldDeleteAContact_andReturnStatusIsOk() throws Exception{

        Contact contactOne = new Contact(1, "user1@localhost.com", "UserOne", "Person");

        when(contactService.delete(contactOne.getId())).thenReturn(contactOne);

        MvcResult result = mockMvc
                .perform(
                        delete("/contact/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();


    }
}
