package com.bootcamp.todo.controller;

import com.bootcamp.todo.model.Todo;
import com.bootcamp.todo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {

    @Autowired
    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping(path = "/hello")
    public String Hello() {
        return "Hello World";
    }

    @GetMapping
    public List<Todo> getAllTodos() {
        return todoService.findAll();
    }
}
