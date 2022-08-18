package com.vancefm.ticketstack.controllers;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BasicController<T> {
    ResponseEntity<List<T>> getAll();
    ResponseEntity<T> getByID(Integer id);
    ResponseEntity<String> create(T t);
    ResponseEntity<String> update(T t);
    ResponseEntity<String> delete(Integer id);
}
