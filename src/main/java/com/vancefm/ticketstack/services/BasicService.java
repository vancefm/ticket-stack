package com.vancefm.ticketstack.services;

import com.vancefm.ticketstack.pojos.Ticket;

import java.util.List;

public interface BasicService<T> {
    List<T> getAll();
    T getByID(Integer id);
    T create(T t);
    T update(Integer id, T t);
    void delete(Integer id);
}
