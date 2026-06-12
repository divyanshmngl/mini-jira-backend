package com.minijira.dto;

import com.minijira.enums.KafkaEventType;
import com.minijira.enums.Swimlane;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KafkaTaskEventDto {

    private Integer taskId;
    private Integer userId;
    private Integer  assignedTo;
    private KafkaEventType eventType;
    private Swimlane swimlane;
    private Swimlane previousSwimlane;
    private Instant timestamp;
}
