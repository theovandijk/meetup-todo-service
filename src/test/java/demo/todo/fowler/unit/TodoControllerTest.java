package demo.todo.fowler.unit;

import demo.todo.controller.TodoController;
import demo.todo.model.TodoItem;
import demo.todo.service.TodoService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
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
        List<TodoItem> todos = todoController.getTodos();

        // then
        assertThat(todos).contains(preparedTodo1, preparedTodo2);
    }

    @Test
    public void givenTodoWhenGetOne() {

        // given
        TodoItem preparedTodo = new TodoItem(1L, "Dummy 1", false);
        given(todoService.getTodo(1L))
                .willReturn(preparedTodo);

        // when
        TodoItem todo = todoController.getTodo(1L);

        // then
        assertThat(todo).isEqualTo(preparedTodo);
    }

    @Test
    public void givenTodoWhenPostOne() {

        // given
        TodoItem preparedTodo = new TodoItem(1L, "Dummy 1", false);
        given(todoService.addTodo(preparedTodo))
                .willReturn(preparedTodo);

        // when
        TodoItem todo = todoController.addTodo(preparedTodo);

        // then
        assertThat(todo).isEqualTo(preparedTodo);
    }


    @Test
    public void givenTodoWhenPutOne() {

        // given
        TodoItem preparedTodo = new TodoItem(1L, "Dummy 1 updated", false);
        given(todoService.updateTodo(1L, preparedTodo))
                .willReturn(preparedTodo);

        // when
        TodoItem todo = todoController.updateTodo(1L, preparedTodo);

        // then
        assertThat(todo).isEqualTo(preparedTodo);
    }

    @Test
    public void callDelete() {

        // when
        todoController.deleteTodo(1L);

        // then
        Mockito.verify(todoService, times(1)).deleteTodo(1L);
    }
}