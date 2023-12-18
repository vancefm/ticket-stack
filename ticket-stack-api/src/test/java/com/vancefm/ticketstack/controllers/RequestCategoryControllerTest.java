package com.vancefm.ticketstack.controllers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vancefm.ticketstack.entities.RequestCategory;
import com.vancefm.ticketstack.services.RequestCategoryService;
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
public class RequestCategoryControllerTest {

    private static final ObjectMapper mapper = JsonMapper.builder()
            .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
            .addModule(new JavaTimeModule())
            .build();

    @MockBean
    private RequestCategoryService requestCategoryService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void get_shouldGetAllRequestCategories_andReturnStatusOk() throws Exception{

        RequestCategory categoryOne = new RequestCategory(1, "Category 1");
        RequestCategory categoryTwo = new RequestCategory(2, "Category 2");
        RequestCategory categoryThree = new RequestCategory(3, "Category 3");
        List<RequestCategory> categoryList = Stream.of(categoryOne, categoryTwo, categoryThree).collect(Collectors.toList());

        Mockito.when(requestCategoryService.getAll()).thenReturn(Optional.of(categoryList));

        mockMvc
                .perform(get("/ticket-request-category"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", Matchers.is(3)))
                .andReturn();
    }

    @Test
    public void get_shouldGetAllRequestCategories_andReturnNotFound() throws Exception {

        Mockito.when(requestCategoryService.getAll()).thenReturn(Optional.empty());

        mockMvc
                .perform(get("/ticket-request-category"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void get_shouldGetARequestCategoryById_andReturnStatusOk() throws Exception{
        Optional<RequestCategory> categoryOne = Optional.of(new RequestCategory(1, "Category 1"));

        Mockito.when(requestCategoryService.getByID(1)).thenReturn(categoryOne);

        mockMvc
                .perform(get("/ticket-request-category/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Test
    public void get_shouldGetARequestCategoryById_andReturnNotFound() throws Exception {
        Mockito.when(requestCategoryService.getByID(1)).thenReturn(Optional.empty());

        mockMvc
                .perform(get("/ticket-request-category/1"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void post_shouldCreateARequestCategory_andReturnStatusIsCreated() throws Exception{

        RequestCategory categoryOne = new RequestCategory(null, "Category 1");
        Optional<RequestCategory> categoryTwo = Optional.of(new RequestCategory(1, "Category 1"));

        Mockito.when(requestCategoryService.create(categoryOne)).thenReturn(categoryTwo);

        mockMvc
                .perform(
                        post("/ticket-request-category")
                                .content(mapper.writeValueAsString(categoryOne))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Test
    public void post_shouldCreateARequestCategory_andReturnStatusBadRequest() throws Exception{

        RequestCategory categoryOne = new RequestCategory(null, "Category 1");

        Mockito.when(requestCategoryService.create(categoryOne)).thenReturn(Optional.empty());

        mockMvc
                .perform(
                        post("/ticket-request-category")
                                .content(mapper.writeValueAsString(categoryOne))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void put_shouldUpdateARequestCategory_andReturnStatusIsOk() throws Exception{

        RequestCategory categoryOne = new RequestCategory(1, "Category 1");

        when(requestCategoryService.update(categoryOne)).thenReturn(categoryOne);

        mockMvc
                .perform(
                        put("/ticket-request-category/1")
                                .content(mapper.writeValueAsString(categoryOne))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

    }

    @Test
    public void put_shouldUpdateARequestCategory_andReturnStatusBadRequest() throws Exception{
        RequestCategory categoryOne = new RequestCategory(1, "Category 1");

        when(requestCategoryService.update(categoryOne)).thenReturn(null);

        mockMvc
                .perform(
                        put("/ticket-request-category/1")
                                .content(mapper.writeValueAsString(categoryOne))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void delete_shouldDeleteARequestCategory_andReturnStatusIsOk() throws Exception{

        mockMvc
                .perform(
                        delete("/ticket-request-category/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andReturn();
    }

}
