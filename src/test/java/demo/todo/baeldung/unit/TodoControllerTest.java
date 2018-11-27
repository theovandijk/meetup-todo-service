package demo.todo.baeldung.unit;

import demo.todo.controller.TodoController;
import demo.todo.model.TodoItem;
import demo.todo.service.TodoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TodoController.class)
public class TodoControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private TodoService todoService;

    @Test
    public void givenTodosWhenGetAllTodos() throws Exception {

        // given
        TodoItem preparedTodo1 = new TodoItem(1L, "Dummy item 1", false);
        TodoItem preparedTodo2 = new TodoItem(2L, "Dummy item 2", false);
        Mockito.when(todoService.getAllTodos())
                .thenReturn(Arrays.asList(preparedTodo1, preparedTodo2));

        // when
        ResultActions response = mvc.perform(get("/todos")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("Dummy item 1")))
                .andExpect(jsonPath("$[1].title", is("Dummy item 2")));
    }

    @Test
    public void givenTodoWhenGetOne() throws Exception {

        // given
        TodoItem preparedTodo = new TodoItem(1L, "Dummy item", false);
        Mockito.when(todoService.getTodo(1L))
                .thenReturn(Optional.of(preparedTodo));

        // when
        ResultActions response = mvc.perform(get("/todos/1")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Dummy item")));
    }

    @Test
    public void given404WhenGetNotExisting() throws Exception {

        // given
        Mockito.when(todoService.getTodo(1L))
                .thenReturn(Optional.empty());

        // when
        ResultActions response = mvc.perform(get("/todos/1")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isNotFound());
    }

    @Test
    public void given204WhenDelete() throws Exception {

        // given
        Mockito.when(todoService.deleteTodo(1L))
                .thenReturn(true);

        // when
        ResultActions response = mvc.perform(delete("/todos/1")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isNoContent());
    }

    @Test
    public void given404WhenDeleteNotExisting() throws Exception {

        // given
        Mockito.when(todoService.deleteTodo(1L))
                .thenReturn(false);

        // when
        ResultActions response = mvc.perform(delete("/todos/1")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isNotFound());
    }
}
