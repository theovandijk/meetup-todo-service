package demo.todo.fowler.unit;

import demo.todo.controller.ResourceNotFoundException;
import demo.todo.data.TodoRepository;
import demo.todo.model.TodoItem;
import demo.todo.service.TodoService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.MockitoAnnotations.initMocks;

public class TodoServiceTest {

    private TodoService todoService;
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Mock
    private TodoRepository todoRepository;

    @Before
    public void setUp() {
        initMocks(this);
        todoService = new TodoService(todoRepository);
    }

    @Test
    public void returnTodosWhenGetAllTodos() {

        // given
        TodoItem preparedTodo1 = new TodoItem(1L, "Dummy 1", false);
        TodoItem preparedTodo2 = new TodoItem(2L, "Dummy 2", false);

        given(todoRepository.findAll())
                .willReturn(Arrays.asList(preparedTodo1, preparedTodo2));

        // when
        List<TodoItem> todos = todoService.getAllTodos();

        // then
        assertThat(todos).contains(preparedTodo1, preparedTodo2);
    }

    @Test
    public void returnTodoOnGetOne() {

        // given
        TodoItem preparedTodo = new TodoItem(1L, "Dummy 1", false);
        given(todoRepository.findById(1L))
                .willReturn(Optional.of(preparedTodo));

        // when
        TodoItem todo = todoService.getTodo(1L);

        // then
        assertThat(todo).isEqualTo(preparedTodo);
    }


    @Test
    public void returnTodoOnAdded() {

        // given
        TodoItem preparedTodo = new TodoItem(1L, "Dummy 1", false);
        given(todoRepository.save(preparedTodo))
                .willReturn(preparedTodo);

        // when
        TodoItem addedTodo = todoService.addTodo(preparedTodo);

        // then
        assertThat(addedTodo).isEqualTo(preparedTodo);
    }


    @Test
    public void returnTodoOnUpdated() {

        // given
        TodoItem preparedTodo = new TodoItem(1L, "Dummy 1 updated", false);
        given(todoRepository.findById(1L))
                .willReturn(Optional.of(preparedTodo));
        given(todoRepository.save(preparedTodo))
                .willReturn(preparedTodo);

        // when
        TodoItem todo = todoService.updateTodo(1L, preparedTodo);

        // then
        assertThat(todo).isEqualTo(preparedTodo);
    }

    @Test
    public void throwExceptionOnGetNotExisting() {

        // given
        given(todoRepository.findById(1L))
                .willReturn(Optional.empty());

        thrown.expect(ResourceNotFoundException.class);

        // when
        todoService.getTodo(1L);
    }

    @Test
    public void returnOnDelete() {

        // given
        TodoItem todoItem = new TodoItem(1L, "Dummy 1", false);
        given(todoRepository.findById(1L))
                .willReturn(Optional.of(todoItem));

        // when
        todoService.deleteTodo(1L);

        // then
        Mockito.verify(todoRepository, times(1)).delete(todoItem);
    }

    @Test
    public void throwExceptionOnDeleteNotExisting() {

        // given
        given(todoRepository.findById(1L))
                .willReturn(Optional.empty());

        thrown.expect(ResourceNotFoundException.class);

        // when
        todoService.deleteTodo(1L);

        // then
        Mockito.verify(todoRepository, never()).delete(Mockito.any(TodoItem.class));
    }
}
