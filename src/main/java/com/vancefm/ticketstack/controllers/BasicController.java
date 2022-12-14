package com.vancefm.ticketstack.controllers;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BasicController<T> {
    ResponseEntity<List<T>> getAll();
    ResponseEntity<T> getByID(Integer id);
    ResponseEntity<T> create(T t);
    ResponseEntity<T> update(Integer id, T t);
    ResponseEntity<T> delete(Integer id);
}
