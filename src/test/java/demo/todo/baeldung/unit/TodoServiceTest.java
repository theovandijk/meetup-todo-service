package demo.todo.baeldung.unit;

import demo.todo.controller.ResourceNotFoundException;
import demo.todo.data.TodoRepository;
import demo.todo.model.TodoItem;
import demo.todo.service.TodoService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@RunWith(SpringRunner.class)
public class TodoServiceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private TodoService todoService;
    @MockBean
    private TodoRepository todoRepository;

    @Before
    public void setUp() {
        todoService = new TodoService(todoRepository);
    }

    @Test
    public void returnTodosWhenGetAllTodos() {
        // given
        TodoItem preparedTodo1 = new TodoItem(1L, "Dummy 1", false);
        TodoItem preparedTodo2 = new TodoItem(2L, "Dummy 2", false);

        Mockito.when(todoRepository.findAll())
                .thenReturn(Arrays.asList(preparedTodo1, preparedTodo2));

        // when
        List<TodoItem> todos = todoService.getAllTodos();

        // then
        assertThat(todos).hasSize(2);
        assertThat(todos.get(0).getTitle()).isEqualTo("Dummy 1");
        assertThat(todos.get(1).getTitle()).isEqualTo("Dummy 2");
    }

    @Test
    public void returnTodoWhenGetOne() {
        // given
        TodoItem preparedTodo = new TodoItem(1L, "Dummy item", false);

        Mockito.when(todoRepository.findById(1L))
                .thenReturn(Optional.of(preparedTodo));

        // when
        TodoItem todo = todoService.getTodo(1L);

        // then
        assertThat(todo.getTitle()).isEqualTo("Dummy item");
    }

    @Test
    public void throwExceptionWhenGetNotExisting() {
        // given
        Mockito.when(todoRepository.findById(1L))
                .thenReturn(Optional.empty());

        thrown.expect(ResourceNotFoundException.class);

        // when
        todoService.getTodo(1L);
    }

    @Test
    public void returnOnDelete() {
        // given
        TodoItem preparedTodo = new TodoItem(1L, "Dummy item", false);

        Mockito.when(todoRepository.findById(1L))
                .thenReturn(Optional.of(preparedTodo));

        // when
        todoService.deleteTodo(1L);

        // then
        Mockito.verify(todoRepository, times(1)).delete(preparedTodo);
    }

    @Test
    public void throwExceptionWhenDeleteNotExisting() {
        // given
        Mockito.when(todoRepository.findById(1L))
                .thenReturn(Optional.empty());

        thrown.expect(ResourceNotFoundException.class);

        // when
        todoService.deleteTodo(1L);

        // then
        Mockito.verify(todoRepository, never()).delete(Mockito.any(TodoItem.class));
    }
}
