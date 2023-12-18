package com.vancefm.ticketstack.services;

import java.util.List;
import java.util.Optional;

public interface BasicRestServiceFactory<T> {
    Optional<List<T>> getAll();
    Optional<T> getByID(Integer id);
    Optional<T> create(T t);
    T update(T t);
    void delete(Integer id);
}
