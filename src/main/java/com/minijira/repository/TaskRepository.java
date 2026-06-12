package com.minijira.repository;

import com.minijira.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    @Query(value = "SELECT * FROM tasks t " +
            "WHERE (?1 IS NULL OR t.assigned_to = ?1) " +
            "AND (?2 IS NULL OR t.created_by = ?2) " +
            "AND (?3 = '' OR t.swimlane = ?3) " +
            "AND (?4 = '' OR t.created_at >= CAST(?4 AS TIMESTAMPTZ)) " +
            "AND (?5 = '' OR t.created_at <= CAST(?5 AS TIMESTAMPTZ)) " +
            "AND (t.created_by = ?6 OR t.assigned_to = ?6) " +
            "ORDER BY t.created_at DESC", nativeQuery = true)
    List<Task> findTasksWithFilters( Integer assignedTo, Integer createdBy,
                                    String swimlane, String fromTime, String toTime, Integer userId);


//    @Query(value = "SELECT * FROM tasks t " +
//            "WHERE t.created_by = ?1 OR t.assigned_to = ?1 " +
//            "ORDER BY t.created_at DESC", nativeQuery = true)
//    List<Task> getVisibleTasksForUser(Integer userId);

    @Query(value = "SELECT * FROM tasks t " +
            "WHERE t.id = ?1 " +
            "AND (t.created_by = ?2 OR t.assigned_to = ?2)", nativeQuery = true)
    Task getVisibleTaskById(Integer taskId, Integer userId);

    @Query(value = "SELECT * FROM tasks t " +
            "WHERE t.created_by = ?1 OR t.assigned_to = ?1 " +
            "ORDER BY t.created_at DESC", nativeQuery = true)
    List<Task> getAllVisibleTasksOfUser(Integer userId);
}
