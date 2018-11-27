package demo.todo.fowler.unit;

import demo.todo.data.TodoRepository;
import demo.todo.model.TodoItem;
import demo.todo.service.TodoService;
import org.junit.Before;
import org.junit.Test;
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
        Optional<TodoItem> result = todoService.getTodo(1L);

        // then
        assertThat(result).hasValue(preparedTodo);
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
        Optional<TodoItem> result = todoService.updateTodo(1L, preparedTodo);

        // then
        assertThat(result).hasValue(preparedTodo);
    }


    @Test
    public void returnEmptyOnGetNotExisting() {

        // given
        given(todoRepository.findById(1L))
                .willReturn(Optional.empty());

        // when
        Optional<TodoItem> result = todoService.getTodo(1L);

        // then
        assertThat(result).isNotPresent();
    }


    @Test
    public void returnTrueOnDelete() {

        // given
        TodoItem preparedTodo = new TodoItem(1L, "Dummy 1", false);
        given(todoRepository.findById(1L))
                .willReturn(Optional.of(preparedTodo));

        // when
        Boolean result = todoService.deleteTodo(1L);

        // then
        Mockito.verify(todoRepository, times(1)).deleteById(1L);
        assertThat(result).isTrue();
    }

    @Test
    public void returnFalseOnDeleteNotExisting() {

        // given
        given(todoRepository.findById(1L))
                .willReturn(Optional.empty());

        // when
        Boolean result = todoService.deleteTodo(1L);

        // then
        Mockito.verify(todoRepository, never()).deleteById(1L);
        assertThat(result).isFalse();
    }
}
