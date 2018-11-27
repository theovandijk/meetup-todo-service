package demo.todo.fowler.integration;

import demo.todo.data.TodoRepository;
import demo.todo.model.TodoItem;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TodoRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TodoRepository todoRepository;

    @Before
    public void clean(){
        todoRepository.deleteAll();
    }

    @Test
    public void findAll() {

        // given
        entityManager.persist(TodoItem.builder().title("Dummy 1").build());
        entityManager.persist(TodoItem.builder().title("Dummy 2").build());
        entityManager.persist(TodoItem.builder().title("Dummy 3").build());
        entityManager.flush();

        // when
        List<TodoItem> foundItems = todoRepository.findAll();

        // then
        assertThat(foundItems).hasSize(3);
    }

    @Test
    public void findOne() {

        // given
        TodoItem todo = TodoItem.builder().title("Dummy item").build();
        TodoItem persistedItem = entityManager.persist(todo);
        entityManager.flush();

        // when
        Optional<TodoItem> result = todoRepository.findById(persistedItem.getId());

        // then
        assertThat(result).hasValue(persistedItem);
    }

    @Test
    public void findNone() {

        // when
        Optional<TodoItem> result = todoRepository.findById(1L);

        // then
        assertThat(result).isNotPresent();
    }

    @Test
    public void save() {

        // given
        TodoItem todo = TodoItem.builder().title("Dummy item").build();

        // when
        TodoItem savedItem = todoRepository.save(todo);

        // then
        TodoItem foundItem = entityManager.find(TodoItem.class, savedItem.getId());
        assertThat(foundItem).isEqualTo(savedItem);
    }

    @Test
    public void delete() {

        // given
        TodoItem todo = TodoItem.builder().title("Dummy item").build();
        TodoItem persistedItem = entityManager.persist(todo);
        entityManager.flush();

        // when
        todoRepository.deleteById(persistedItem.getId());

        // then
        TodoItem foundItem = entityManager.find(TodoItem.class, persistedItem.getId());
        assertThat(foundItem).isNull();
    }

}
