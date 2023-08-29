package com.bdiplus.task.service;

import com.bdiplus.task.dto.TaskRequest;
import com.bdiplus.task.dto.TaskResponse;
import com.bdiplus.task.entity.Task;
import com.bdiplus.task.exception.TasksNotFoundException;
import com.bdiplus.task.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    //Create Task
    public Task createTask(TaskRequest taskRequest){
       Task task = new Task();
       task.setTitle(taskRequest.getTitle());
       task.setDescription(taskRequest.getDescription());

       return taskRepository.save(task);
    }


    //Get all tasks using dto converter
    public List<TaskResponse> getAllTasks() throws TasksNotFoundException {
        List<Task> tasks = taskRepository.findAll();
        if(tasks.isEmpty()){
            throw new TasksNotFoundException("No Tasks found!");
        }
        return tasks
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    public TaskResponse convertToDto(Task task){
        TaskResponse taskResponse=new TaskResponse();
        taskResponse.setTitle(task.getTitle());
        taskResponse.setDescription(task.getDescription());

        return taskResponse;
    }
}
