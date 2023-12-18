package com.vancefm.ticketstack.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vancefm.ticketstack.entities.RequestCategory;
import com.vancefm.ticketstack.kafka.KafkaProducer;
import com.vancefm.ticketstack.services.RequestCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * This method is used to perform CRUD operations for Request Category objects
 */
@Slf4j
@RestController
@RequestMapping(value = "/ticket-request-category")
public class RequestCategoryController implements BasicControllerFactory<RequestCategory> {

    private final RequestCategoryService requestCategoryService;

    private final KafkaProducer kafkaProducer;

    private final ObjectMapper objectMapper;

    public RequestCategoryController(RequestCategoryService requestCategoryService, KafkaProducer kafkaProducer){
        this.requestCategoryService = requestCategoryService;
        this.kafkaProducer = kafkaProducer;
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    /**
     * Get a list of all request categories
     * @return List of Request Categories
     */
    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<RequestCategory>> getAll() {
        try{
            Optional<List<RequestCategory>> resultList = requestCategoryService.getAll();
            if(resultList.isPresent()){
                log.info("Result: " + objectMapper.writeValueAsString(resultList));
                return new ResponseEntity<>(resultList.get(), HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get a request category by ID number
     * @param pathId The integer id of a request category
     * @return A single Request Category object
     */
    @Override
    @GetMapping(value = "/{pathId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<RequestCategory> getByID(@PathVariable Integer pathId) {
        try {
            Optional<RequestCategory> requestCategory = requestCategoryService.getByID(pathId);
            if(requestCategory.isPresent()){
                log.info("Result: " + objectMapper.writeValueAsString(requestCategory));
                return new ResponseEntity<>(requestCategory.get(), HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Create a new request category
     * @param requestCategory A RequestCategory object
     * @return The created request category
     */
    @Override
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<RequestCategory> create(@Valid @RequestBody RequestCategory requestCategory) {
        try {
            log.info("Creating request category from: " + objectMapper.writeValueAsString(requestCategory));
            Optional<RequestCategory> resultRequestCategory = requestCategoryService.create(requestCategory);
            if(resultRequestCategory.isPresent()){
                log.info("Result: " + objectMapper.writeValueAsString(resultRequestCategory));
                kafkaProducer.sendMessage("Request Category was created -> " + objectMapper.writeValueAsString(requestCategory));
                return new ResponseEntity<>(resultRequestCategory.get(), HttpStatus.CREATED);
            }
            else{
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Update a request category
     *
     * @param pathId Integer id of a RequestCategory
     * @param requestCategory A RequestCategory object
     * @return The updated RequestCategory
     */
    @Override
    @PutMapping(value = "/{pathId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<RequestCategory> update(@PathVariable Integer pathId, @Valid @RequestBody RequestCategory requestCategory) {
        try {
            if(pathId != requestCategory.getId()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            log.info("Updating request category to: " + objectMapper.writeValueAsString(requestCategory));
            RequestCategory resultRequestCategory = requestCategoryService.update(requestCategory);
            if (resultRequestCategory != null) {
                log.info("Result: " + objectMapper.writeValueAsString(resultRequestCategory));
                kafkaProducer.sendMessage("Request Category was updated -> " + objectMapper.writeValueAsString(requestCategory));
                return new ResponseEntity<>(resultRequestCategory, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(requestCategory, HttpStatus.BAD_REQUEST);
            }
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete a RequestCategory
     *
     * @param pathId The integer of a request category
     * @return ResponseEntity with status
     */
    @Override
    @DeleteMapping(value = "/{pathId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<RequestCategory> delete(@PathVariable Integer pathId) {
        requestCategoryService.delete(pathId);
        log.info("Deleted request category with id: " + pathId);
        kafkaProducer.sendMessage("Request category was deleted -> " + pathId);
        return new ResponseEntity<>(HttpStatus.OK);

    }
}
