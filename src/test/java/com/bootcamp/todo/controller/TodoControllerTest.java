package com.bootcamp.todo.controller;

import com.bootcamp.todo.model.Todo;
import com.bootcamp.todo.repository.TodoRepository;
import org.assertj.core.api.AssertionsForClassTypes;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
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
        todoRepository.save(new Todo("text5"));
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

    @Test
    void should_save_todo_success() throws Exception {
        // Given
        todoRepository.deleteAll();
        String givenText = "New Todo";
        String givenTodo = String.format(
                "{\"text\": \"%s\"}",
                givenText
        );

        // When
        // Then
        client.perform(MockMvcRequestBuilders.post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(givenTodo)
                )
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.text").value(givenText));
        List<Todo> todos = todoRepository.findAll();
        assertThat(todos).hasSize(1);
        AssertionsForClassTypes.assertThat(todos.get(0).getId()).isNotNull();
        AssertionsForClassTypes.assertThat(todos.get(0).getText()).isEqualTo(givenText);
        AssertionsForClassTypes.assertThat(todos.get(0).getDone()).isEqualTo(false);
    }

    @Test
    void should_update_todo_text_success() throws Exception {
        // Given
        Todo existTodo = todoRepository.findAll().get(0);
        Integer givenId = existTodo.getId();
        String givenText = "updated text";
        Boolean givenDone = existTodo.getDone();
        String givenTodo = String.format(
                "{\"id\": %s, \"text\": \"%s\"}",
                givenId,
                givenText
        );
        // When
        // Then
        client.perform(MockMvcRequestBuilders.put("/todos/" + givenId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(givenTodo)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.text").value(givenText))
                .andExpect(MockMvcResultMatchers.jsonPath("$.done").value(givenDone));
        List<Todo> employees = todoRepository.findAll();
        assertThat(employees).hasSize(5);
        AssertionsForClassTypes.assertThat(employees.get(0).getId()).isEqualTo(givenId);
        AssertionsForClassTypes.assertThat(employees.get(0).getText()).isEqualTo(givenText);
        AssertionsForClassTypes.assertThat(employees.get(0).getDone()).isEqualTo(givenDone);
    }

    @Test
    void should_update_todo_done_status_success() throws Exception {
        // Given
        Todo existTodo = todoRepository.findAll().get(0);
        Integer givenId = existTodo.getId();
        String givenText = existTodo.getText();
        Boolean givenDone = existTodo.getDone();
        String givenTodo = String.format(
                "{\"id\": %s, \"text\": \"%s\", \"done\": \"%s\"}",
                givenId,
                givenText,
                !givenDone
        );
        // When
        // Then
        client.perform(MockMvcRequestBuilders.put("/todos/" + givenId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(givenTodo)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.text").value(givenText))
                .andExpect(MockMvcResultMatchers.jsonPath("$.done").value(!givenDone));
        List<Todo> employees = todoRepository.findAll();
        assertThat(employees).hasSize(5);
        AssertionsForClassTypes.assertThat(employees.get(0).getId()).isEqualTo(givenId);
        AssertionsForClassTypes.assertThat(employees.get(0).getText()).isEqualTo(givenText);
        AssertionsForClassTypes.assertThat(employees.get(0).getDone()).isEqualTo(!givenDone);
    }

    @Test
    void should_throw_todo_not_found_exception_when_get_by_id_given_not_exists() throws Exception {
        // Given
        String givenTodo = String.format(
                "{\"id\": %s}",
                1
        );
        // When
        // Then
        client.perform(MockMvcRequestBuilders.put("/todos/" + 12389)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(givenTodo))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}


