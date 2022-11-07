package com.vancefm.ticketstack.controllers;


import com.fasterxml.jackson.core.type.TypeReference;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    public void shouldGetAllContacts() throws Exception{

        Contact contactOne = new Contact(-1, "user1@localhost.com", "UserOne", "Person");
        Contact contactTwo = new Contact(-2, "user2@localhost.com", "UserTwo", "Person");
        Contact contactThree = new Contact(-3, "user3@localhost.com", "UserTwo", "Person");
        List<Contact> contactList = Stream.of(contactOne, contactTwo, contactThree).collect(Collectors.toList());

        Mockito.when(contactService.getAll()).thenReturn(contactList);

        MvcResult result = mockMvc
                .perform(get("/contact"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", Matchers.is(3)))
                .andReturn();

        List<Contact> resultList = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

        assertTrue(resultList.size() == 3);

    }

    @Test
    public void shouldGetAContact() throws Exception{
        Contact contactOne = new Contact(-1, "user1@localhost.com", "UserOne", "Person");

        Mockito.when(contactService.getByID(1)).thenReturn(contactOne);

        MvcResult result = mockMvc
                .perform(get("/contact/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Contact contact = mapper.readValue(result.getResponse().getContentAsString(), Contact.class);

        assertEquals(-1, contact.getId());
    }

    @Test
    public void shouldCreateAContact() throws Exception{

        Contact contactOne = new Contact(-1, "user1@localhost.com", "UserOne", "Person");

        Mockito.when(contactService.create(contactOne)).thenReturn(contactOne);

        MvcResult result = mockMvc
                .perform(post("/contact")
                        .content(mapper.writeValueAsString(contactOne))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Contact resultContact = mapper.readValue(result.getResponse().getContentAsString(), Contact.class);

        assertEquals(contactOne, resultContact);

    }

    @Test
    public void shouldUpdateAContact() throws Exception{

        Contact contactOne = new Contact(-1, "user1@localhost.com", "UserOne", "Person");

        Mockito.when(contactService.update(contactOne)).thenReturn(contactOne);

        MvcResult result = mockMvc
                .perform(put("/contact")
                        .content(mapper.writeValueAsString(contactOne))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Contact resultContact = mapper.readValue(result.getResponse().getContentAsString(), Contact.class);

        assertEquals(contactOne, resultContact);

    }


}
