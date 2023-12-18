package com.vancefm.ticketstack.services;

import com.vancefm.ticketstack.entities.RequestCategory;
import com.vancefm.ticketstack.repositories.RequestCategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class RequestCategoryService implements BasicRestServiceFactory<RequestCategory> {

    @Autowired
    private RequestCategoryRepository requestCategoryRepository;

    @Override
    public Optional<List<RequestCategory>> getAll() {
        return Optional.ofNullable((List<RequestCategory>) requestCategoryRepository.findAll());
    }

    @Override
    public Optional<RequestCategory> getByID(Integer id) {
        return requestCategoryRepository.findById(id);
    }

    @Override
    public Optional<RequestCategory> create(RequestCategory requestCategory) {
        try {
            return Optional.ofNullable(requestCategoryRepository.save(requestCategory));
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            log.error(e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public RequestCategory update(RequestCategory requestCategory) {
        return requestCategoryRepository.save(requestCategory);
    }

    @Override
    public void delete(Integer id) {
        requestCategoryRepository.deleteById(id);
    }
}
