package demo.todo.fowler.unit;

import demo.todo.controller.TodoController;
import demo.todo.model.TodoItem;
import demo.todo.service.TodoService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;

public class TodoControllerTest {

    private TodoController todoController;
    @Mock
    private TodoService todoService;

    @Before
    public void setUp() {
        initMocks(this);
        todoController = new TodoController(todoService);
    }

    @Test
    public void givenTodosWhenGetAllTodos() {

        // given
        TodoItem preparedTodo1 = new TodoItem(1L, "Dummy 1", false);
        TodoItem preparedTodo2 = new TodoItem(2L, "Dummy 2", false);

        given(todoService.getAllTodos())
                .willReturn(Arrays.asList(preparedTodo1, preparedTodo2));

        // when
        ResponseEntity<List<TodoItem>> response = todoController.getTodos();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains(preparedTodo1, preparedTodo2);
    }

    @Test
    public void givenTodoWhenGetOne() {

        // given
        TodoItem preparedTodo = new TodoItem(1L, "Dummy 1", false);
        given(todoService.getTodo(1L))
                .willReturn(Optional.of(preparedTodo));

        // when
        ResponseEntity<TodoItem> response = todoController.getTodo(1L);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(preparedTodo);
    }

    @Test
    public void givenTodoWhenPostOne() {

        // given
        TodoItem preparedTodo = new TodoItem(1L, "Dummy 1", false);
        given(todoService.addTodo(preparedTodo))
                .willReturn(preparedTodo);

        // when
        ResponseEntity<TodoItem> response = todoController.addTodo(preparedTodo);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(preparedTodo);
    }


    @Test
    public void givenTodoWhenPutOne() {

        // given
        TodoItem preparedTodo = new TodoItem(1L, "Dummy 1 updated", false);
        given(todoService.updateTodo(1L, preparedTodo))
                .willReturn(Optional.of(preparedTodo));

        // when
        ResponseEntity<TodoItem> response = todoController.updateTodo(1L, preparedTodo);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(preparedTodo);
    }


    @Test
    public void given404WhenNotExisting() {

        // given
        given(todoService.getTodo(1L))
                .willReturn(Optional.empty());

        // when
        ResponseEntity<TodoItem> response = todoController.getTodo(1L);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }



    @Test
    public void given204WhenDelete() {

        // given
        given(todoService.deleteTodo(1L))
                .willReturn(true);

        // when
        ResponseEntity response = todoController.deleteTodo(1L);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void given404WhenDeleteNotExisting() {

        // given
        given(todoService.deleteTodo(1L))
                .willReturn(false);

        // when
        ResponseEntity response = todoController.deleteTodo(1L);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
