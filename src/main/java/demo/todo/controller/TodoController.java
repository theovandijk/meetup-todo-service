package demo.todo.controller;

import demo.todo.model.TodoItem;
import demo.todo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TodoController {

    private TodoService todoService;

    @Autowired
    public TodoController(final TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("/todos")
    public List<TodoItem> getTodos() {
        return todoService.getAllTodos();
    }

    @PostMapping("/todos")
    public TodoItem addTodo(@RequestBody TodoItem todoItem) {
        return todoService.addTodo(todoItem);
    }

    @GetMapping(path = "/todos/{id}", produces = "application/json")
    public TodoItem getTodo(@PathVariable long id) {
        return todoService.getTodo(id);
    }

    @PutMapping(path = "/todos/{id}", produces = "application/json")
    public TodoItem updateTodo(@PathVariable long id, @RequestBody TodoItem todoItem) {
        return todoService.updateTodo(id, todoItem);
    }

    @DeleteMapping(path = "/todos/{id}")
    public ResponseEntity deleteTodo(@PathVariable long id) {
        todoService.deleteTodo(id);
        return ResponseEntity.noContent().build();
    }
}
