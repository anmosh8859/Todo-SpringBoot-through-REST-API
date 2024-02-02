package com.todo.react.todowithreact.todo;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@SessionAttributes("name")
public class TodoControllerJpa {

    private TodoRepository todoRepository;

    public TodoControllerJpa(TodoRepository todoRepository) {
        super();
        this.todoRepository = todoRepository;
    }

    @RequestMapping("list-todos")
    public String listAllTodos(ModelMap map){
        List<Todo> todos = todoRepository.findByUsername(getLoggedInUsername(map));

//        List<Todo> todos = todoService.findByUsername(getLoggedInUsername(map));
        map.addAttribute("todos",todos);
        return "listTodos";
    }

    private static String getLoggedInUsername(ModelMap map) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    @RequestMapping("delete-todo")
    public String deleteTodo(@RequestParam int id){
        todoRepository.deleteById(id);
        return "redirect:list-todos";
    }

    @RequestMapping(value = "update-todo", method=RequestMethod.GET)
    public String showUpdateTodoPage(@RequestParam int id, ModelMap map){
        Todo todo = todoRepository.findById(id).get();
        map.addAttribute(todo);
        return "todo";
    }

    @RequestMapping(value = "update-todo", method=RequestMethod.POST)
    public String updateTodo(@Valid Todo todo, BindingResult result, ModelMap map){
        if(result.hasErrors()) return "todo";

        todo.setUsername(getLoggedInUsername(map));
        todoRepository.save(todo);
        return "redirect:list-todos";
    }

    @RequestMapping(value = "add-todo", method = RequestMethod.GET)
    public String showNewTodo(ModelMap map){
        Todo todo = new Todo(0, getLoggedInUsername(map),"",LocalDate.now(),false);
        map.put("todo",todo);
        return "todo";
    }

    @RequestMapping(value = "add-todo", method = RequestMethod.POST)
    public String addNewTodo(@Valid Todo todo, BindingResult result, ModelMap map) {

        if (result.hasErrors()) {
            return "todo";
        }

        String username = getLoggedInUsername(map);
        todo.setUsername(username);
        todoRepository.save(todo);

//        todoService.addTodo(username, todo.getDescription(), todo.getTargetDate(), todo.isDone());
        return "redirect:list-todos";
    }

}