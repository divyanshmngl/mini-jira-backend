package com.minijira.dto;

import com.minijira.enums.Swimlane;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class TaskDto {

    private Integer id;

    @NotNull(message = "title is required")
    private String title;

    private String description;
    private Integer createdBy; // basically userId >> ki kisne assign kia h
    private Integer assignedTo; // best practice is to store the id not names
    private Swimlane swimlane;
    private Instant createdAt;  // creation date taken in Instant >>
    private Long version;
}
