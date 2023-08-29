package com.bdiplus.task.controller;

import com.bdiplus.task.dto.TaskRequest;
import com.bdiplus.task.dto.TaskResponse;
import com.bdiplus.task.entity.Task;
import com.bdiplus.task.exception.TasksNotFoundException;
import com.bdiplus.task.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/task")
public class TaskController {

    @Autowired
    TaskService taskService;
    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@RequestBody @Valid TaskRequest taskRequest){
        return new ResponseEntity<>(taskService.createTask(taskRequest), HttpStatus.CREATED);
    }

    @GetMapping("/all-tasks")
    public ResponseEntity<List<TaskResponse>> getAllTasks() throws TasksNotFoundException {
        return new ResponseEntity<>(taskService.getAllTasks(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) throws TasksNotFoundException {
        return new ResponseEntity<>(taskService.getTaskById(id), HttpStatus.OK);
    }
}

