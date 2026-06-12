package com.minijira.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationEventDto {

    private Integer taskId;
    private String userId;
    private String eventType;
    private boolean acknowledged;
}
