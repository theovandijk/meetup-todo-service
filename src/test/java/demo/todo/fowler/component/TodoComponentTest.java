package demo.todo.fowler.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import demo.todo.data.TodoRepository;
import demo.todo.model.TodoItem;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest()
@AutoConfigureMockMvc
public class TodoComponentTest {
    @Autowired
    private MockMvc mvc;

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
    public void givenTodosWhenGetAllTodos() throws Exception {

        // given
        todoRepository.save(TodoItem.builder().title("test 1").build());
        todoRepository.save(TodoItem.builder().title("test 2").build());

        // when
        ResultActions response = mvc.perform(get("/todos").contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$", IsCollectionWithSize.hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("test 1")))
                .andExpect(jsonPath("$[1].title", is("test 2")));
    }

    /**
     * POST /todos
     */
    @Test
    public void givenTodoOnAdd() throws Exception {

        // given
        TodoItem todoItem = TodoItem.builder().title("test 1").build();

        // when
        ResultActions response = mvc.perform(
                post("/todos")
                        .content(asJsonString(todoItem))
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("test 1")));
    }

    /**
     * GET /todos/{id}
     */
    @Test
    public void givenTodoOnGetOne() throws Exception {

        // given
        TodoItem todoItem = todoRepository.save(TodoItem.builder().title("test 1").build());

        // when
        ResultActions response = mvc.perform(get("/todos/" + todoItem.getId()).contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("test 1")));
    }

    /**
     * GET /todos/{id}
     */
    @Test
    public void given404OnGetNotExisting() throws Exception {
        // when
        ResultActions response = mvc.perform(get("/todos/1").contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isNotFound());
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
        ResultActions response = mvc.perform(
                put("/todos/" + todoItem.getId())
                .content(asJsonString(todoItem))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("test 2")));
    }

    /**
     * DELETE /todos/{id}
     */
    @Test
    public void given204OnDelete() throws Exception {

        // given
        TodoItem todoItem = todoRepository.save(TodoItem.builder().title("test 1").build());

        // when
        ResultActions response = mvc.perform(delete("/todos/" + todoItem.getId()).contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isNoContent());
    }

    /**
     * DELETE /todos/{id}
     */
    @Test
    public void given404OnDeleteNotExisting() throws Exception {
        // when
        ResultActions response = mvc.perform(delete("/todos/1").contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isNotFound());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
