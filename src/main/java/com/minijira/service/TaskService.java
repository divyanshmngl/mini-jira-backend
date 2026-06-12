package com.minijira.service;

import com.minijira.client.NotificationFeignClient;
import com.minijira.customException.TaskException;
import com.minijira.dto.NotificationEventDto;
import com.minijira.dto.TaskDto;
import com.minijira.enums.KafkaEventType;
import com.minijira.enums.LogMessage;
import com.minijira.enums.MessageEnum;
import com.minijira.enums.Swimlane;
import com.minijira.kafka.KafkaProducerService;
import com.minijira.model.Task;
import com.minijira.repository.TaskRepository;
import com.minijira.util.SwimlaneTransitionValidator;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TaskService {

    private final TaskRepository taskRepository;
    private final KafkaProducerService kafkaProducerService;
    private final NotificationFeignClient notificationFeignClient;
    private final NotificationService notificationService;

    public TaskService(TaskRepository taskRepository,
                       KafkaProducerService kafkaProducerService,
                       NotificationFeignClient notificationFeignClient,
                       NotificationService notificationService) {
        this.taskRepository = taskRepository;
        this.kafkaProducerService = kafkaProducerService;
        this.notificationFeignClient = notificationFeignClient;
        this.notificationService=notificationService;
    }

    public Task createTask(TaskDto taskDto, Integer userId) {
        log.info(LogMessage.TASK_CREATE_START.getTemplate(), userId);
        Task task =new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        task.setCreatedBy(userId);
        task.setAssignedTo(taskDto.getAssignedTo());
        task.setCreatedAt(Instant.now());

        Task savedTask = taskRepository.save(task);
        // now produce the kafka Event of reaction  of new event(Task).
        kafkaProducerService.publishTaskEvent(savedTask, userId, KafkaEventType.TASK_CREATED, null);
        log.info(LogMessage.TASK_CREATE_SUCCESS.getTemplate(), savedTask.getId(), userId);
        return savedTask;

    }

    public List<TaskDto> getTaskById(Integer taskId, Integer userId) {

        Task task =new Task();
        List<Task>tasks=new ArrayList<>();
        if(taskId!=null) {
            log.info(LogMessage.TASK_GET_START.getTemplate(), taskId, userId);
            task= taskRepository.getVisibleTaskById(taskId, userId);
            if (task == null) {
                log.info(LogMessage.TASK_NOT_FOUND.getTemplate(), taskId);
                throw new TaskException(MessageEnum.TASK_NOT_FOUND);
            }
          tasks.add(task);
        }else{
            log.info("Fetching all tasks for userId: {}", userId);
            tasks = taskRepository.getAllVisibleTasksOfUser(userId);
        }

        List<TaskDto>result=new ArrayList<>();
        for(Task t:tasks){
            TaskDto dto= new TaskDto();
            dto.setId(t.getId());
            dto.setTitle(t.getTitle());
            dto.setDescription(t.getDescription());
            dto.setAssignedTo(t.getAssignedTo());
            dto.setCreatedBy(t.getCreatedBy());
            dto.setSwimlane(t.getSwimlane());
            dto.setCreatedAt(t.getCreatedAt());

            result.add(dto);

        }
        return result;
    }

    public List<TaskDto> getTasksWithFilters( Integer assignedTo,Integer createdBy, Swimlane swimlane, Instant fromTime, Instant toTime, Integer userId) {
        log.info(LogMessage.TASK_FILTER_START.getTemplate(), userId);
        String swimlaneParam = swimlane != null ? swimlane.name() : "";
        String fromTimeParam = fromTime != null ? fromTime.toString() : "";
        String toTimeParam = toTime != null ? toTime.toString() : "";
        List<Task> tasks = taskRepository.findTasksWithFilters( assignedTo, createdBy, swimlaneParam, fromTimeParam, toTimeParam, userId);
        List<TaskDto> result = new ArrayList<>();
        for (Task task : tasks) {

                TaskDto taskDto = new TaskDto();

                taskDto.setId(task.getId());
                taskDto.setTitle(task.getTitle());
                taskDto.setDescription(task.getDescription());
                taskDto.setCreatedBy(task.getCreatedBy());
                taskDto.setAssignedTo(task.getAssignedTo());
                taskDto.setSwimlane(task.getSwimlane());
                taskDto.setCreatedAt(task.getCreatedAt());

                result.add(taskDto);

        }
        return result;
    }

// todo: Task Visibility Api >> for particular user id >> replaced by get All tasks > in get Task api logic if taskID is not in input > toh userId k behalf p jo jo visible h vo show krdo .

//    public List<TaskDto> getVisibleTasksForUser(Integer userId) {
//        log.info(LogMessage.FETCH_VISIBLE_TASK.getTemplate(), userId);
//
//        List<Task> tasks = taskRepository.getVisibleTasksForUser(userId);
//        List<TaskDto> taskDtoList = new ArrayList<>();
//        for (Task task : tasks) {
//
//            TaskDto taskDto = new TaskDto();
//
//            taskDto.setId(task.getId());
//            taskDto.setTitle(task.getTitle());
//            taskDto.setDescription(task.getDescription());
//            taskDto.setCreatedBy(task.getCreatedBy());
//            taskDto.setAssignedTo(task.getAssignedTo());
//            taskDto.setSwimlane(task.getSwimlane());
//            taskDto.setCreatedAt(task.getCreatedAt());
//
//            taskDtoList.add(taskDto);
//        }
//
//        return taskDtoList;
//    }


    public TaskDto updateTask( TaskDto taskDto, Integer userId) {
        log.info(LogMessage.TASK_UPDATE_START.getTemplate(), taskDto.getId(), userId);
        try {
            if (taskDto.getId() == null) {   // id can not be null >> apply this constraint instead of this current constraint
                throw new TaskException(MessageEnum.TASK_NOT_FOUND);
            }
            Task task = taskRepository.getVisibleTaskById(taskDto.getId(), userId);

            if (task == null) {
                throw new TaskException(MessageEnum.TASK_NOT_FOUND);
            }

            Swimlane previousSwimlane = task.getSwimlane();
            boolean swimlaneChanged = false;


            if (taskDto.getTitle() != null) {
                if (!StringUtils.hasText(taskDto.getTitle())) {
                    throw new TaskException(MessageEnum.INVALID_TITLE);
                }
                task.setTitle(taskDto.getTitle());
            }

            if (taskDto.getDescription() != null) {
                task.setDescription(taskDto.getDescription());
            }


            if (taskDto.getAssignedTo() != null) {
                task.setAssignedTo(taskDto.getAssignedTo());
            }

            if (taskDto.getSwimlane() != null && taskDto.getSwimlane() != task.getSwimlane()) {
                validateAndApplySwimlaneMove(task, taskDto.getSwimlane(), userId);
                swimlaneChanged = true;
            }

            Task saved = taskRepository.save(task);

            KafkaEventType eventType = swimlaneChanged ? KafkaEventType.TASK_MOVED : KafkaEventType.TASK_UPDATED;
            kafkaProducerService.publishTaskEvent(saved, userId, eventType, swimlaneChanged ? previousSwimlane : null);

            log.info(LogMessage.TASK_UPDATE_SUCCESS.getTemplate(), saved.getId(), saved.getSwimlane());

            TaskDto dto= new TaskDto();
            dto.setId(task.getId());
            dto.setTitle(task.getTitle());
            dto.setDescription(task.getDescription());
            dto.setCreatedBy(task.getCreatedBy());
            dto.setAssignedTo(task.getAssignedTo());
            dto.setSwimlane(task.getSwimlane());
            dto.setCreatedAt(task.getCreatedAt());

            return dto;
        } catch (Exception e) {
            log.error("Error while updating : {}", e.getMessage());
           throw e;
        }
    }

    public void processNotificationFromKafka(NotificationEventDto event) {
        if (event == null || event.getTaskId() == null) {
            log.info("Skipping null notification event");
            return;
        }
        log.info("Notification event processed for taskId={} userId={} acknowledged={}", event.getTaskId(), event.getUserId(), event.isAcknowledged());
    }

// swimlane Validation
    private void validateAndApplySwimlaneMove(Task task, Swimlane targetSwimlane, Integer userId) {
        Swimlane current = task.getSwimlane();

        // No-op
        if (current == targetSwimlane) {
            return;
        }

        if (current.isTerminal()) {
            log.info(LogMessage.TASK_TERMINAL_VIOLATION.getTemplate(), task.getId());
            throw new TaskException(MessageEnum.TERMINAL_STATE_VIOLATION);
        }
        if (!SwimlaneTransitionValidator.isAllowed(current, targetSwimlane)) {
            log.info(LogMessage.TASK_MOVE_INVALID.getTemplate(), current, targetSwimlane, task.getId());
            throw new TaskException(MessageEnum.INVALID_SWIMLANE_TRANSITION);
        }

        if (targetSwimlane == Swimlane.DONE) {
            validateNotificationsCleared(userId, task.getId());
        }
        task.setSwimlane(targetSwimlane);
    }

    private void validateNotificationsCleared(Integer userId, Integer taskId) {
        Long activeCount;

        try {
         //   activeCount = notificationFeignClient.getActiveNotificationCount(userId, taskId);
            activeCount= notificationService.getActiveNotificationCount(userId, taskId);
        } catch (FeignException ex) {
            throw new TaskException(MessageEnum.NOTIFICATION_SERVICE_UNAVAILABLE);
        }

        if (activeCount != null && activeCount > 0) {
            throw new TaskException(MessageEnum.NOTIFICATIONS_PENDING);
        }
    }

}
