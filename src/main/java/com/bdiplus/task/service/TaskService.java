package com.bdiplus.task.service;

import com.bdiplus.task.dto.TaskRequest;
import com.bdiplus.task.entity.Task;
import com.bdiplus.task.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;
    public Task createTask(TaskRequest taskRequest){
       Task task = new Task();
       task.setTitle(taskRequest.getTitle());
       task.setDescription(taskRequest.getDescription());

       return taskRepository.save(task);
    }
}
