package com.bdiplus.task.repository;

import com.bdiplus.task.entity.Task;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long>{
    @Query(value = "SELECT * FROM `task` WHERE id=:id", nativeQuery = true)
    Task findByUserId(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM `task` WHERE id=:id", nativeQuery = true)
    void deleteByTaskId(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE `task` SET `description`=:description,`title`=:title WHERE id=:id",nativeQuery = true)
    void updateTaskById(@Param("id") Long id,@Param("title") String title, @Param("description") String description);
}
