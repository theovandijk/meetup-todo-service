package demo.todo.baeldung.integration;

import demo.todo.ServiceTodoApplication;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServiceTodoApplication.class)
@AutoConfigureMockMvc
public class TodoIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private TodoRepository todoRepository;

    @Before
    public void clean() {
        todoRepository.deleteAll();
    }

    @Test
    public void getAllTodos() throws Exception {
        // given
        TodoItem todoItem = new TodoItem();
        todoItem.setTitle("Dummy todo");
        todoRepository.save(todoItem);

        // when
        ResultActions response = mvc.perform(get("/todos")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$", IsCollectionWithSize.hasSize(1)))
                .andExpect(jsonPath("$[0].title", is(todoItem.getTitle())));
    }

    @Test
    public void getOneTodo() throws Exception {
        // given
        TodoItem preparedTodo = TodoItem.builder().title("Dummy todo").build();
        TodoItem savedTodo = todoRepository.save(preparedTodo);

        // when
        ResultActions response = mvc.perform(get("/todos/" + savedTodo.getId())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Dummy todo")));
    }


    @Test
    public void get404WhenNotExisting() throws Exception {

        // when
        ResultActions response = mvc.perform(get("/todos/1")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isNotFound());
    }

    @Test
    public void delete204WhenDelete() throws Exception {

        // given
        TodoItem preparedTodo = TodoItem.builder().title("Dummy todo").build();
        TodoItem savedTodo = todoRepository.save(preparedTodo);

        // when
        ResultActions response = mvc.perform(delete("/todos/" + savedTodo.getId())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isNoContent());
    }

    @Test
    public void delete404WhenDeleteNotExisting() throws Exception {

        // when
        ResultActions response = mvc.perform(delete("/todos/1")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isNotFound());
    }
}
