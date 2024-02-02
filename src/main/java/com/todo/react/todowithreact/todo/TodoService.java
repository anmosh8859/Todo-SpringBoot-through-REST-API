package com.todo.react.todowithreact.todo;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Service
public class TodoService {
    private static List<Todo> todos = new ArrayList<>();
    private static int todosCount = 0;

    static{
        todos.add(new Todo(++todosCount,"anand", "Learn AWS", LocalDate.now().plusYears(1), false));
        todos.add(new Todo(++todosCount,"anand", "Learn Azure", LocalDate.now().plusYears(1), false));
        todos.add(new Todo(++todosCount,"anand", "Learn JavaFX", LocalDate.now().plusYears(3), false));
        todos.add(new Todo(++todosCount,"anand", "Learn ...", LocalDate.now().plusYears(2), false));
    }

    public List<Todo> findByUsername(String username){
        Predicate<? super Todo> predicate = todo -> todo.getUsername().equalsIgnoreCase(username);
        return todos.stream().filter(predicate).toList();
    }

    public void addTodo(String username, String description, LocalDate targetDate, boolean done){
        Todo todo = new Todo(++todosCount,username,description,targetDate,done);
        todos.add(todo);
    }

    public void deleteById(int id){
        todos.removeIf(todo -> todo.getId()==id);
    }

    public Todo findById(int id) {
        return todos.stream().filter(todo -> todo.getId()==id).findFirst().get();
    }

    public void updateTodo(Todo todo) {
        deleteById(todo.getId());
        todos.add(todo);
    }
}