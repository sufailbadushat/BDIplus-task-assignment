package com.bdiplus.task;

import static org.junit.jupiter.api.Assertions.*;

import com.bdiplus.task.dto.TaskRequest;
import com.bdiplus.task.dto.TaskResponse;
import com.bdiplus.task.entity.Task;
import com.bdiplus.task.exception.TasksNotFoundException;
import com.bdiplus.task.repository.TaskRepository;
import com.bdiplus.task.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    Task RECORD_1 = new Task(1L, "Run", "Tomorrow morning at 7:30");
    Task RECORD_2 = new Task(2L, "Gym", "Go to gym");
    Task RECORD_3 = new Task(3L, "Assignment", "Complete assignment");

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    @BeforeEach
    public void setUp() {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(taskService).build();
    }


//---------------------------------------TEST----GET BY ID----START---------------------------------------------------//
    @Test
    public void testGetTaskById_ExistingId() throws TasksNotFoundException {
        Task task = new Task(1L, "Walk", "Today");
        Mockito.when(taskRepository.findByUserId(1L)).thenReturn(task);

        TaskResponse taskResponse = taskService.getTaskById(1L);

        assertNotNull(taskResponse);
        assertEquals(task.getId(), taskResponse.getId());
        assertEquals(task.getTitle(), taskResponse.getTitle());
        assertEquals(task.getDescription(), taskResponse.getDescription());

        verify(taskRepository, times(1)).findByUserId(1L);
    }

    @Test
    public void testGetTaskById_NonExistingId() {
        // Set up the mock behavior to return null
        Mockito.when(taskRepository.findByUserId(eq(2L))).thenReturn(null);
        // Test the method with a non-existing ID
        assertThrows(TasksNotFoundException.class, () -> taskService.getTaskById(2L));
        // Verify that taskRepository.findByUserId was called with the correct argument
        verify(taskRepository, times(1)).findByUserId(eq(2L));
    }
//---------------------------------------TEST----GET BY ID----END-----------------------------------------------------//


//---------------------------------------TEST----UPDATE BY ID----START------------------------------------------------//
    @Test
    public void testUpdateById_ExistingId() throws TasksNotFoundException {

        Task existingTask = new Task(1L, "Original title", "Original description");
        TaskRequest updateTaskRequest = new TaskRequest("Update title", "Updated description");

        // Set up the mock behavior
        Mockito.when(taskRepository.findByUserId(1L)).thenReturn(existingTask);
        Mockito.when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Call the method you want to test
        Task updatedTask = taskService.updateById(1L, updateTaskRequest);

        // Verify the results
        assertNotNull(updatedTask);
        assertEquals(updateTaskRequest.getTitle(), updatedTask.getTitle());
        assertEquals(updateTaskRequest.getDescription(), updatedTask.getDescription());

        // Verify that taskRepository.findByUserId was called with the correct argument
        verify(taskRepository, times(1)).findByUserId(eq(1L));

        // Verify that taskRepository.save was called with the updated task
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    public void testUpdateById_NonExistingId() {
        // Set up the mock behavior to return null
        when(taskRepository.findByUserId(eq(2L))).thenReturn(null);

        // Create a task request with updated values
        TaskRequest updatedTaskRequest = new TaskRequest();
        updatedTaskRequest.setTitle("Updated Title");
        updatedTaskRequest.setDescription("Updated Description");

        // Test the method with a non-existing ID
        assertThrows(TasksNotFoundException.class, () -> taskService.updateById(2L, updatedTaskRequest));

        // Verify that taskRepository.findByUserId was called with the correct argument
        verify(taskRepository, times(1)).findByUserId(eq(2L));

        // Verify that taskRepository.save was never called since task doesn't exist
        verify(taskRepository, never()).save(any(Task.class));
    }
//---------------------------------------TEST----UPDATE BY ID----END--------------------------------------------------//


//---------------------------------------TEST----GET ALL TASK----START------------------------------------------------//
    @Test
    public void testGetAllTasks_Success() throws TasksNotFoundException {
        List<Task> taskList = new ArrayList<>(Arrays.asList(RECORD_1, RECORD_2, RECORD_3));

        Mockito.when(taskRepository.findAll()).thenReturn(taskList);

        List<TaskResponse> taskResponseList = taskService.getAllTasks();

        assertNotNull(taskResponseList);
        assertEquals(taskList.isEmpty(), taskResponseList.isEmpty());
        assertEquals(3, taskResponseList.size());

        TaskResponse taskResponse1 = taskResponseList.get(1);
        assertEquals(taskList.get(1).getTitle(), taskResponse1.getTitle());

        // Verify that the repository method was called
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllTasks_Empty() {
        Mockito.when(taskRepository.findAll()).thenReturn(new ArrayList<>());

        assertThrows(TasksNotFoundException.class, () -> taskService.getAllTasks());

        verify(taskRepository, times(1)).findAll();
    }
//---------------------------------------TEST----GET ALL TASK----END--------------------------------------------------//



//---------------------------------------TEST----CREATE TASK----START-------------------------------------------------//
    @Test
    public void testCreateTask_Success() {
        TaskRequest taskRequest=new TaskRequest("title", "description");
        Task task=new Task(1L, "title", "description");
        Mockito.when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task createdTask=taskService.createTask(taskRequest);

        assertNotNull(createdTask);
        assertEquals("title", createdTask.getTitle());

        verify(taskRepository, times(1)).save(any(Task.class));
    }


//---------------------------------------TEST----CREATE TASK----END---------------------------------------------------//


//---------------------------------------TEST----DELETE TASK----START-------------------------------------------------//
@Test
public void testDeleteTask_TaskFound() throws TasksNotFoundException {
    Long taskId = 1L;
    Task mockTask = new Task();
    mockTask.setId(taskId);
    Mockito.when(taskRepository.findByUserId(taskId)).thenReturn(mockTask);

    taskService.deleteById(1L);

    verify(taskRepository, times(1)).findByUserId(taskId);
    verify(taskRepository, times(1)).deleteByTaskId(taskId);

}
    @Test
    public void testDeleteTask_TaskNotFound(){
        Long taskId=1L;
        Mockito.when(taskRepository.findByUserId(taskId)).thenReturn(null);

        assertThrows(TasksNotFoundException.class, ()-> taskService.deleteById(taskId));

        verify(taskRepository, times(1)).findByUserId(taskId);
        verify(taskRepository, never()).deleteById(taskId);
    }
//---------------------------------------TEST----DELETE TASK----END---------------------------------------------------//



}
