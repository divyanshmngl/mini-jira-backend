package com.minijira.model;

import com.minijira.enums.Swimlane;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.Instant;



@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Integer createdBy; // userID

    @Column(name="assigned_to")
    private Integer assignedTo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Swimlane swimlane = Swimlane.TODO;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();


}
