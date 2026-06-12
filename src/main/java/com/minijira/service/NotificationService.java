package com.minijira.service;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    //temporary replacement of feign
    public Long getActiveNotificationCount(Integer userId, Integer taskId) {

        if (taskId == null) {
            return 0L;
        }

        // Even taskId : no  notification (all cleared)
        if (taskId % 2 == 0) {
            return 0L;
        }

        // Odd taskId : simulate pending notifications
        return 2L;
    }
}
