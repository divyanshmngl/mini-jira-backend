package com.minijira.controller;

import com.minijira.dto.JsonResponse;
import com.minijira.dto.TaskDto;
import com.minijira.enums.ApiResponseEnum;
import com.minijira.enums.Swimlane;
import com.minijira.model.Task;
import com.minijira.service.TaskService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@RestController
@Slf4j
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/createTask")
    public JsonResponse<Task> createTask(@RequestHeader("X-User-Id") Integer userId,
                                         @Valid @RequestBody TaskDto taskDto) {
        Task saved = taskService.createTask(taskDto, userId);
        return new JsonResponse<>(true, ApiResponseEnum.SAVED.getMessage(), saved);
    }

    @GetMapping("/getTask")
    public JsonResponse<List<TaskDto>> getTask(@RequestHeader("X-User-Id") Integer userId, @RequestParam(required=false) Integer taskId) {

        List<TaskDto> task = taskService.getTaskById(taskId, userId); // yha taskId kya hogi jo db m phle id save hui thi vhi
        return new JsonResponse<>(true, ApiResponseEnum.SUCCESS.name(), task);
    }


    //Visibility is not a separate feature — it is a cross-cutting rule applied inside all task-related APIs
    @GetMapping("/getTasksWithFilters")
    public JsonResponse<List<TaskDto>> getTasksWithFilters(
            @RequestHeader ("X-User-Id") Integer userId,
            @RequestParam(required = false) Integer assignedTo,
            @RequestParam(required = false) Integer createdBy,
            @RequestParam(required = false) Swimlane swimlane,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant fromTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant toTime) {
        List<TaskDto> tasks = taskService.getTasksWithFilters( assignedTo, createdBy, swimlane, fromTime, toTime , userId);
        if (tasks.isEmpty()) {
            return new JsonResponse<>(true, ApiResponseEnum.NO_DATA_AVAILABLE.getMessage(), null);
        }
        return new JsonResponse<>(true, ApiResponseEnum.SUCCESS.name(), tasks);
    }

    // Task Visibility api
//    @GetMapping("/getVisibleTasks")
//    public JsonResponse<List<TaskDto>> getVisibleTasks(   @RequestHeader ("X-User-Id") Integer userId) {
//
//        List<TaskDto> tasks = taskService.getVisibleTasksForUser(userId);
//        if (tasks.isEmpty()) {
//            return new JsonResponse<>(true, ApiResponseEnum.NO_DATA_AVAILABLE.getMessage(), null);
//        }
//        return new JsonResponse<>(true, ApiResponseEnum.SUCCESS.name(), tasks);
//    }

    @PostMapping("/updateTask")
    public JsonResponse<TaskDto> updateTask( @RequestHeader ("X-User-Id") Integer userId, @RequestBody TaskDto taskDto) {

        TaskDto updated = taskService.updateTask(taskDto, userId);
        return new JsonResponse<>(true, ApiResponseEnum.UPDATED.getMessage(), updated);
    }
}
