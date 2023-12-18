package com.vancefm.ticketstack.repositories;

import com.vancefm.ticketstack.entities.RequestCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Component
public interface RequestCategoryRepository extends CrudRepository<RequestCategory, Integer> {

    @Override
    Iterable<RequestCategory> findAll();

    @Override
    <S extends RequestCategory> S save(S entity);

    @Override
    Optional<RequestCategory> findById(Integer integer);

    @Override
    void deleteById(Integer integer);
}
