package com.minijira.kafka;

import com.minijira.dto.KafkaTaskEventDto;
import com.minijira.enums.KafkaEventType;
import com.minijira.enums.LogMessage;
import com.minijira.enums.Swimlane;
import com.minijira.model.Task;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, KafkaTaskEventDto> taskEventKafkaTemplate;

    @Value("${mini-jira.kafka.task-events-topic}")
    private String taskEventsTopic;

    public KafkaProducerService(KafkaTemplate<String, KafkaTaskEventDto> taskEventKafkaTemplate) {
        this.taskEventKafkaTemplate = taskEventKafkaTemplate;
    }

    @Async
    public void publishTaskEvent(Task task, Integer userId, KafkaEventType eventType, Swimlane previousSwimlane) {

        KafkaTaskEventDto event = new KafkaTaskEventDto();

        event.setTaskId(task.getId());
        event.setUserId(userId);
        event.setAssignedTo(task.getAssignedTo());
        event.setEventType(eventType);
        event.setSwimlane(task.getSwimlane());
        event.setPreviousSwimlane(previousSwimlane);
        event.setTimestamp(Instant.now());

        log.info(LogMessage.KAFKA_PUBLISH.getTemplate(), task.getId(), eventType);

        taskEventKafkaTemplate.send(taskEventsTopic, task.getId().toString(), event);
    }
}
