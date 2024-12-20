package com.bootcamp.todo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    public static final String TODO_NOT_EXIST = "找不到要更新的todo";

    public NotFoundException() {
        super(TODO_NOT_EXIST);
    }
}

