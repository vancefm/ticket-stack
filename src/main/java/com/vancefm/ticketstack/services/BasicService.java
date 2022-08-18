package com.vancefm.ticketstack.services;

import java.util.List;

public interface BasicService<T> {
    List<T> getAll();
    T getByID(Integer id);
    void createOrUpdate(T t);
    void delete(Integer id);
}
