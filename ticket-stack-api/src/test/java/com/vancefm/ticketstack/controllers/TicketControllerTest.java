package com.vancefm.ticketstack.controllers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vancefm.ticketstack.entities.Ticket;
import com.vancefm.ticketstack.services.TicketService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
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
    public void get_shouldGetAllTickets_andReturnStatusOk() throws Exception{

        Ticket ticketOne = new Ticket(1,"Test ticket 1",1,1,"",1,null,null,null);
        Ticket ticketTwo = new Ticket(2,"Test ticket 1",1,1,"",1,null,null,null);
        Ticket ticketThree = new Ticket(3,"Test ticket 1",1,1,"",1,null,null,null);
        List<Ticket> ticketList = Stream.of(ticketOne, ticketTwo, ticketThree).collect(Collectors.toList());

        when(ticketService.getAll()).thenReturn(Optional.of(ticketList));

        mockMvc
                .perform(get("/ticket"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", Matchers.is(3)))
                .andReturn();
    }

    @Test
    public void get_shouldGetAllTickets_andReturnStatusNotFound() throws Exception{

        when(ticketService.getAll()).thenReturn(Optional.empty());

        mockMvc
                .perform(get("/ticket"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void get_shouldGetATicketByID_andReturnStatusOk() throws Exception{

        Optional<Ticket> ticketOne = Optional.of(new Ticket(1, "Test ticket 1", 1, 1, "", 1, null, null, null));

        when(ticketService.getByID(1)).thenReturn(ticketOne);

        mockMvc
                .perform(get("/ticket/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Test
    public void get_shouldGetATicketByID_andReturnStatusNotFound() throws Exception{
        when(ticketService.getByID(1)).thenReturn(Optional.empty());

        mockMvc
                .perform(get("/ticket/1"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void post_shouldCreateATicket_andReturnStatusIsCreated() throws Exception{

        Ticket ticketOne = new Ticket(null,"Test ticket 1",1,1,"",1,null,null,null);
        Optional<Ticket> ticketTwo = Optional.of(new Ticket(1, "Test ticket 1", 1, 1, "", 1, null, null, null));

        when(ticketService.create(ticketOne)).thenReturn(ticketTwo);

        mockMvc
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
    public void post_shouldCreateATicket_andReturnStatusBadRequest() throws Exception{

        Ticket ticketOne = new Ticket(null,"Test ticket 1",1,1,"",1,null,null,null);

        when(ticketService.create(ticketOne)).thenReturn(Optional.empty());

        mockMvc
                .perform(
                        post("/ticket")
                                .content(mapper.writeValueAsString(ticketOne))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void put_shouldUpdateATicket_andReturnStatusIsOk() throws Exception{

        Ticket ticketOne = new Ticket(1,"Test ticket 1",1,1,"",1,null,null,null);

        when(ticketService.update(ticketOne)).thenReturn(ticketOne);

        mockMvc
                .perform(
                        put("/ticket/1")
                                .content(mapper.writeValueAsString(ticketOne))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

    }

    @Test
    public void put_shouldUpdateATicket_andReturnStatusBadRequest() throws Exception{
        Ticket ticketOne = new Ticket(1,"Test ticket 1",1,1,"",1,null,null,null);

        when(ticketService.update(ticketOne)).thenReturn(null);

        mockMvc
                .perform(
                        put("/ticket/1")
                                .content(mapper.writeValueAsString(ticketOne))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void delete_shouldDeleteATicket_andReturnStatusIsOk() throws Exception{

        mockMvc
                .perform(
                        delete("/ticket/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andReturn();
    }

}
