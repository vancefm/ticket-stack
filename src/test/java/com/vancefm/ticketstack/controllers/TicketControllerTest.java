package com.vancefm.ticketstack.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vancefm.ticketstack.pojos.Ticket;
import com.vancefm.ticketstack.services.TicketService;
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
public class TicketControllerTest {

    private static final ObjectMapper mapper = JsonMapper.builder()
            .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
            .addModule(new JavaTimeModule())
            .build();

    @MockBean
    private TicketService ticketService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldGetAllTickets() throws Exception{

        Ticket ticketOne = new Ticket(1,"Test ticket 1",0,0,"",0,null,null,null);
        Ticket ticketTwo = new Ticket(2,"Test ticket 1",0,0,"",0,null,null,null);
        Ticket ticketThree = new Ticket(3,"Test ticket 1",0,0,"",0,null,null,null);
        List<Ticket> ticketList = Stream.of(ticketOne, ticketTwo, ticketThree).collect(Collectors.toList());

        Mockito.when(ticketService.getAll()).thenReturn(ticketList);

        MvcResult result = mockMvc
                .perform(get("/ticket"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", Matchers.is(3)))
                .andReturn();

        List<Ticket> resultList = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<Ticket>>() {});

        assertTrue(resultList.size() == 3);

    }

    @Test
    public void shouldGetATicket() throws Exception{
        Ticket ticketOne = new Ticket(1,"Test ticket 1",0,0,"",0,null,null,null);

        Mockito.when(ticketService.getByID(1)).thenReturn(ticketOne);

        MvcResult result = mockMvc
                .perform(get("/ticket/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        Ticket ticket = mapper.readValue(result.getResponse().getContentAsString(), Ticket.class);

        assertEquals(1, ticket.getId());
    }

    @Test
    public void shouldCreateATicket() throws Exception{

        Ticket ticketOne = new Ticket(null,"Test ticket 1",0,0,"",0,null,null,null);
        Ticket ticketTwo = new Ticket(1,"Test ticket 1",0,0,"",0,null,null,null);

        Mockito.when(ticketService.create(ticketOne)).thenReturn(ticketTwo);

        MvcResult result = mockMvc
                .perform(
                        post("/ticket")
                                .content(mapper.writeValueAsString(ticketOne))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Test
    public void shouldUpdateATicket() throws Exception{

        Ticket ticketOne = new Ticket(1,"Test ticket 1",0,0,"",0,null,null,null);

        MvcResult result = mockMvc
                .perform(
                        put("/ticket")
                                .content(mapper.writeValueAsString(ticketOne))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().string(""))
                .andReturn();
    }

}
