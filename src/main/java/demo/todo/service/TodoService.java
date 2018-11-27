package demo.todo.service;

import demo.todo.data.TodoRepository;
import demo.todo.model.TodoItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
public class TodoService {

    private TodoRepository todoRepository;

    @Autowired
    public TodoService(TodoRepository todoRepository){
        this.todoRepository = todoRepository;
    }

    public List<TodoItem> getAllTodos() {
        return todoRepository.findAll();
    }

    public TodoItem addTodo(TodoItem todoItem) {
        return todoRepository.save(todoItem);
    }


    public Optional<TodoItem> getTodo(long id) {
        return todoRepository.findById(id);
    }

    public Optional<TodoItem> updateTodo(long id, TodoItem todoItem) {
        return todoRepository.findById(id)
                .map(foundTodoItem -> todoRepository.save(todoItem));
    }

    public Boolean deleteTodo(long id) {
        return todoRepository.findById(id)
                .map((foundTodoItem) -> {
                    todoRepository.deleteById(id);
                    return true;
                })
                .orElse(false);
    }
}
