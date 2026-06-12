package com.minijira.kafka;

import com.minijira.dto.NotificationEventDto;
import com.minijira.enums.LogMessage;
import com.minijira.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumerService {

    private final TaskService taskService;

    public KafkaConsumerService(TaskService taskService) {
        this.taskService = taskService;
    }

    @KafkaListener(id = "notificationEventsListener", topics = "${mini-jira.kafka.notification-events-topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeNotificationEvent(NotificationEventDto event) {
        log.info(LogMessage.KAFKA_CONSUME.getTemplate(), event);
        taskService.processNotificationFromKafka(event);
    }
}
