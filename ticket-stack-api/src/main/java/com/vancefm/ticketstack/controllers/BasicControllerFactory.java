package com.vancefm.ticketstack.controllers;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BasicControllerFactory<T> {
    ResponseEntity<List<T>> getAll();
    ResponseEntity<T> getByID(Integer pathId);
    ResponseEntity<T> create(T t);
    ResponseEntity<T> update(Integer pathId, T t);
    ResponseEntity<T> delete(Integer pathId);
}
