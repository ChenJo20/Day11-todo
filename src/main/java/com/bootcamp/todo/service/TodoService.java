package com.bootcamp.todo.service;

import com.bootcamp.todo.exception.NotFoundException;
import com.bootcamp.todo.model.Todo;
import com.bootcamp.todo.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {
    @Autowired
    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<Todo> findAll() {
        return todoRepository.findAll();
    }

    public Todo findById(Integer employeeId) {
        return todoRepository.findById(employeeId)
                .orElseThrow(NotFoundException::new);
    }

    public Todo save(Todo todo) {
        return todoRepository.save(todo);
    }

    public Todo update(Integer id, Todo todo) {
        Todo existedTodo = this.findById(id);
        existedTodo.updateTodo(todo);
        return todoRepository.save(existedTodo);
    }
}
