package com.bdiplus.task;

import com.bdiplus.task.controller.TaskController;
import com.bdiplus.task.dto.TaskRequest;
import com.bdiplus.task.dto.TaskResponse;
import com.bdiplus.task.entity.Task;
import com.bdiplus.task.exception.TasksNotFoundException;
import com.bdiplus.task.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {
    private MockMvc mockMvc;


    @Mock
    private TaskService taskService;
    @InjectMocks
    private TaskController taskController;

    TaskResponse RECORD_1 = new TaskResponse(1L, "Run", "Tomorrow morning at 7:30");
    TaskResponse RECORD_2 = new TaskResponse(2L, "Gym", "Go to gym");
    TaskResponse RECORD_3 = new TaskResponse(3L, "Assignment", "Complete assignment");

    @BeforeEach
    public void setUp() {

        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
    }

    @Test
    public void getAllTasks_Success() throws Exception {
       List<TaskResponse> records = new ArrayList<>(Arrays.asList(RECORD_1, RECORD_2, RECORD_3));

        Mockito.when(taskService.getAllTasks()).thenReturn(records);

//        ResponseEntity<List<TaskResponse>> responseEntity=taskController.getAllTasks();
//        assertEquals(3, responseEntity.getBody().size());
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        verify(taskService).getAllTasks();

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/task/all-tasks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testCreateTask_success(){
        TaskRequest taskRequest=new TaskRequest();
        taskRequest.setTitle("Do assignment");
        taskRequest.setDescription("by 48 horus");
        Task RECORD_4 = new Task(1L, "Do assignment", "by 48 horus");

        Mockito.when(taskService.createTask(taskRequest)).thenReturn(RECORD_4);

        ResponseEntity<Task> responseEntity = taskController.createTask(taskRequest);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(RECORD_4.getTitle(), taskRequest.getTitle());

    }

    @Test
    public void testGetTaskById_success() throws TasksNotFoundException {
        Long taskId = 5L;
        TaskResponse RECORD_5 = new TaskResponse(5L, "Do assignment", "by 48 horus");

        Mockito.when(taskService.getTaskById(taskId)).thenReturn(RECORD_5);

        TaskResponse responseEntity=taskService.getTaskById(taskId);

        assertNotNull(responseEntity);
        assertEquals(RECORD_5.getId(), responseEntity.getId());
        assertEquals(RECORD_5.getTitle(), responseEntity.getTitle());
        assertEquals(RECORD_5.getDescription(), responseEntity.getDescription());

    }

    @Test
    public void testDeleteById_success() throws TasksNotFoundException {

        Long taskId = 5L;
        doNothing().when(taskService).deleteById(taskId);

        ResponseEntity<String> responseEntity = taskController.deleteById(taskId);


        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Task deleted successfully", responseEntity.getBody());

        verify(taskService).deleteById(taskId);
    }

    @Test
    public void testUpdateById_success() throws TasksNotFoundException {
        Long taskId = 5L;
        TaskRequest taskRequest = new TaskRequest("Updated task title", "Updated tsk description");
        Task updatedTask = new Task(taskId, "Updated task title", "Updated tsk description");

        when(taskService.updateById(taskId, taskRequest)).thenReturn(updatedTask);

        ResponseEntity<Task> responseEntity = taskController.updateById(taskId, taskRequest);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedTask, responseEntity.getBody());

        // Verify that the updateById method was called on the taskService with the correct taskId and taskRequest
        verify(taskService).updateById(taskId, taskRequest);
    }


}
