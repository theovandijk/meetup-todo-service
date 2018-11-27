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
    public ResponseEntity<List<TodoItem>> getTodos() {
        return ResponseEntity.ok(todoService.getAllTodos());
    }

    @PostMapping("/todos")
    public ResponseEntity<TodoItem> addTodo(@RequestBody TodoItem todoItem) {
        return ResponseEntity.ok(todoService.addTodo(todoItem));
    }

    @GetMapping(path = "/todos/{id}", produces = "application/json")
    public ResponseEntity<TodoItem> getTodo(@PathVariable long id) {
        return todoService.getTodo(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping(path = "/todos/{id}", produces = "application/json")
    public ResponseEntity<TodoItem> updateTodo(@PathVariable long id, @RequestBody TodoItem todoItem) {
        return todoService.updateTodo(id, todoItem)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping(path = "/todos/{id}")
    public ResponseEntity deleteTodo(@PathVariable long id) {
        if (todoService.deleteTodo(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
