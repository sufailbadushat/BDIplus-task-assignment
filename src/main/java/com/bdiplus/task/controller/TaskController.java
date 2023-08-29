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

import javax.naming.ldap.LdapName;
import java.util.List;

@RestController
@RequestMapping("api/task")
public class TaskController {

    @Autowired
    TaskService taskService;
    @CrossOrigin(origins = "*")
    @PostMapping("/create")
    public ResponseEntity<Task> createTask(@RequestBody @Valid TaskRequest taskRequest){
        return new ResponseEntity<>(taskService.createTask(taskRequest), HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/all-tasks")
    public ResponseEntity<List<TaskResponse>> getAllTasks() throws TasksNotFoundException {
        return new ResponseEntity<>(taskService.getAllTasks(), HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) throws TasksNotFoundException {
        return new ResponseEntity<>(taskService.getTaskById(id), HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) throws TasksNotFoundException {
        taskService.deleteById(id);
        return ResponseEntity.ok("Task deleted successfully");
    }

    @CrossOrigin(origins = "*")
    @PutMapping("/update/{id}")
    public ResponseEntity<Task> updateById(@PathVariable Long id, @RequestBody @Valid TaskRequest taskRequest) throws TasksNotFoundException {
        return new ResponseEntity<>(taskService.updateById(id, taskRequest), HttpStatus.OK);
    }
}

