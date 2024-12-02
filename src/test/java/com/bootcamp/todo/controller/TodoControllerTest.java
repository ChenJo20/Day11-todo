package com.bootcamp.todo.controller;

import com.bootcamp.todo.model.Todo;
import com.bootcamp.todo.repository.TodoRepository;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
public class TodoControllerTest {

    @Autowired
    private MockMvc client;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private JacksonTester<List<Todo>> todosJacksonTester;

    @BeforeEach
    void setUp() {
        todoRepository.flush();
        todoRepository.save(new Todo("text1"));
        todoRepository.save(new Todo("text2"));
        todoRepository.save(new Todo("text3"));
        todoRepository.save(new Todo("text4"));
        todoRepository.save(new Todo("text4"));
    }

    @Test
    void should_return_todos_when_get_all_given_todo_exist() throws Exception {
        //given
        final List<Todo> givenTodos = todoRepository.findAll();

        //when
        //then
        final String jsonResponse = client.perform(MockMvcRequestBuilders.get("/todos"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        final List<Todo> todoResult = todosJacksonTester.parseObject(jsonResponse);
        assertThat(todoResult)
                .usingRecursiveFieldByFieldElementComparator(
                        RecursiveComparisonConfiguration.builder()
                                .withComparedFields("id", "done", "text")
                                .build()
                )
                .isEqualTo(givenTodos);
    }
}
