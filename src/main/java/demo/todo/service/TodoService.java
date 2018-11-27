package demo.todo.service;

import demo.todo.controller.ResourceNotFoundException;
import demo.todo.data.TodoRepository;
import demo.todo.model.TodoItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {

    private TodoRepository todoRepository;

    @Autowired
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<TodoItem> getAllTodos() {
        return todoRepository.findAll();
    }

    public TodoItem addTodo(TodoItem todoItem) {
        return todoRepository.save(todoItem);
    }


    public TodoItem getTodo(long id) {
        return todoRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
    }

    public TodoItem updateTodo(long id, TodoItem todoItem) {
        return todoRepository.findById(id)
                .map(foundTodoItem -> todoRepository.save(todoItem))
                .orElseThrow(ResourceNotFoundException::new);
    }

    public void deleteTodo(long id) {
        todoRepository.delete(
                todoRepository.findById(id)
                        .orElseThrow(ResourceNotFoundException::new));
    }
}
