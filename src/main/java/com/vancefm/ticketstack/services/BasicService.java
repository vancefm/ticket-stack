package com.vancefm.ticketstack.services;

import java.util.List;

public interface BasicService<T> {
    List<T> getAll();
    T getByID(Integer id);
    T create(T t);
    T update(Integer id, T t);
    T delete(Integer id);
}
