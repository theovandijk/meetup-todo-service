package demo.todo.spotify.integration;

import demo.todo.data.TodoRepository;
import demo.todo.model.TodoItem;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TodoIntegrationTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TodoRepository todoRepository;

    @Before
    public void setup() {
        todoRepository.deleteAll();
    }

    /**
     * GET /todos
     */
    @Test
    public void givenTodosWhenGetAllTodos() throws URISyntaxException {

        // given
        TodoItem todoItem1 = todoRepository.save(TodoItem.builder().title("test 1").build());
        TodoItem todoItem2 = todoRepository.save(TodoItem.builder().title("test 2").build());

        // when
        URI uri = new URI(format("http://localhost:%1s/todos", port));
        ResponseEntity<List<TodoItem>> response = this.restTemplate.exchange(RequestEntity.get(uri).build(),
                new ParameterizedTypeReference<List<TodoItem>>(){});

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains(todoItem1, todoItem2);
    }

    /**
     * POST /todos
     */
    @Test
    public void givenTodoOnAdd() throws Exception {

        // given
        TodoItem todoItem = TodoItem.builder().title("test 1").build();

        // when
        String url = format("http://localhost:%1s/todos", port);
        ResponseEntity<TodoItem> response = this.restTemplate.postForEntity(url, todoItem, TodoItem.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getTitle()).isEqualTo("test 1");
    }

    /**
     * GET /todos/{id}
     */
    @Test
    public void givenTodoOnGetOne() throws Exception {

        // given
        TodoItem todoItem = todoRepository.save(TodoItem.builder().title("test 1").build());


        // when
        String url = format("http://localhost:%1s/todos/%2s", port, todoItem.getId());
        ResponseEntity<TodoItem> response = this.restTemplate.getForEntity(url, TodoItem.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getTitle()).isEqualTo("test 1");
    }

    /**
     * GET /todos/{id}
     * ERROR
     */
    @Test
    public void given404OnGetNotExisting() throws Exception {
        // when
        String url = format("http://localhost:%1s/todos/1", port);
        ResponseEntity<TodoItem> response = this.restTemplate.getForEntity(url, TodoItem.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }

    /**
     * PUT /todos/{id}
     */
    @Test
    public void givenTodoOnUpdate() throws Exception {

        // given
        TodoItem todoItem = todoRepository.save(TodoItem.builder().title("test 1").build());

        // when
        todoItem.setTitle("test 2");

        String url = format("http://localhost:%1s/todos/%2s", port, todoItem.getId());
        ResponseEntity<TodoItem> response = this.restTemplate.exchange(url, HttpMethod.PUT, new HttpEntity<>(todoItem), TodoItem.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getTitle()).isEqualTo("test 2");
    }

    /**
     * DELETE /todos/{id}
     */
    @Test
    public void given204OnDelete() throws Exception {

// given
        TodoItem todoItem = todoRepository.save(TodoItem.builder().title("test 1").build());

        // when
        String url = format("http://localhost:%1s/todos/%2s", port, todoItem.getId());
        ResponseEntity response = this.restTemplate.exchange(RequestEntity.delete(new URI(url)).build(), ParameterizedTypeReference.forType(null));

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    /**
     * DELETE /todos/{id}
     * ERROR
     */
    @Test
    public void given404OnDeleteNotExisting() throws Exception {

        // when
        String url = format("http://localhost:%1s/todos/1", port);
        ResponseEntity response = this.restTemplate.exchange(RequestEntity.delete(new URI(url)).build(), ParameterizedTypeReference.forType(null));

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
