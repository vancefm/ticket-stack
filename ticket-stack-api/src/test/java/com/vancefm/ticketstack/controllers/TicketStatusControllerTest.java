package com.vancefm.ticketstack.controllers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vancefm.ticketstack.entities.TicketStatus;
import com.vancefm.ticketstack.services.TicketStatusService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
public class TicketStatusControllerTest {

    private static final ObjectMapper mapper = JsonMapper.builder()
            .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
            .addModule(new JavaTimeModule())
            .build();

    @MockBean
    private TicketStatusService ticketStatusService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void get_shouldGetAllTicketStatuses_andReturnStatusOk() throws Exception{

        TicketStatus statusOne = new TicketStatus(1, "Status 1");
        TicketStatus statusTwo = new TicketStatus(2, "Status 2");
        TicketStatus statusThree = new TicketStatus(3, "Status 3");
        List<TicketStatus> statusList = Stream.of(statusOne, statusTwo, statusThree).collect(Collectors.toList());

        Mockito.when(ticketStatusService.getAll()).thenReturn(Optional.of(statusList));

        mockMvc
                .perform(get("/ticket-status"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", Matchers.is(3)))
                .andReturn();
    }

    @Test
    public void get_shouldGetAllTicketStatuses_andReturnStatusNotFound() throws Exception{
        Mockito.when(ticketStatusService.getAll()).thenReturn(Optional.empty());

        mockMvc
                .perform(get("/ticket-status"))
                .andExpect(status().isNotFound())

                .andReturn();
    }

    @Test
    public void get_shouldGetATicketStatusById_andReturnStatusOk() throws Exception{
        Optional<TicketStatus> statusOne = Optional.of(new TicketStatus(1, "Status 1"));

        Mockito.when(ticketStatusService.getByID(1)).thenReturn(statusOne);

        mockMvc
                .perform(get("/ticket-status/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Test
    public void get_shouldGetATicketStatusById_andReturnStatusNotFound() throws Exception{
        Mockito.when(ticketStatusService.getByID(1)).thenReturn(Optional.empty());

        mockMvc
                .perform(get("/ticket-status/1"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void post_shouldCreateATicketStatus_andReturnStatusIsCreated() throws Exception{

        TicketStatus statusOne = new TicketStatus(null, "Status 1");
        Optional<TicketStatus> statusTwo = Optional.of(new TicketStatus(1, "Status 1"));

        Mockito.when(ticketStatusService.create(statusOne)).thenReturn(statusTwo);

        mockMvc
                .perform(
                        post("/ticket-status")
                                .content(mapper.writeValueAsString(statusOne))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Test
    public void post_shouldCreateATicketStatus_andReturnStatusBadRequest() throws Exception{
        TicketStatus statusOne = new TicketStatus(null, "Status 1");

        Mockito.when(ticketStatusService.create(statusOne)).thenReturn(Optional.empty());

        mockMvc
                .perform(
                        post("/ticket-status")
                                .content(mapper.writeValueAsString(statusOne))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void put_shouldUpdateATicketStatus_andReturnStatusIsOk() throws Exception{

        TicketStatus statusOne = new TicketStatus(1, "Status 1");

        when(ticketStatusService.update(statusOne)).thenReturn(statusOne);

        mockMvc
                .perform(
                        put("/ticket-status/1")
                                .content(mapper.writeValueAsString(statusOne))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

    }

    @Test
    public void put_shouldUpdateATicketStatus_andReturnStatusBadRequest() throws Exception{
        TicketStatus statusOne = new TicketStatus(1, "Status 1");

        when(ticketStatusService.update(statusOne)).thenReturn(null);

        mockMvc
                .perform(
                        put("/ticket-status/1")
                                .content(mapper.writeValueAsString(statusOne))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void delete_shouldDeleteATicketStatus_andReturnStatusIsOk() throws Exception{

        mockMvc
                .perform(
                        delete("/ticket-status/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andReturn();
    }

}
