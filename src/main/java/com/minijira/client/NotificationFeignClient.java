package com.minijira.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "notification-service", url = "${mini-jira.notification-service.url}")
public interface NotificationFeignClient {


    @GetMapping("/api/v1/notifications/active-count")
    Long getActiveNotificationCount(@RequestParam("userId") Integer userId, @RequestParam("taskId") Integer taskId);
}
