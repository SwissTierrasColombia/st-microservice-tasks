package com.ai.st.microservice.tasks.models.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ai.st.microservice.tasks.entities.TaskEntity;

public interface TaskRepository extends CrudRepository<TaskEntity, Long> {

    @Override
    List<TaskEntity> findAll();

    @Query("SELECT t FROM TaskEntity t JOIN TaskMemberEntity tm ON t.id = tm.task.id AND tm.memberCode = :memberCode WHERE t.taskState.id IN :taskStates")
    List<TaskEntity> getTasksByMemberAndStates(@Param("memberCode") Long memberCode,
            @Param("taskStates") List<Long> taskStates);

    @Query("SELECT t FROM TaskEntity t JOIN TaskMemberEntity tm ON t.id = tm.task.id AND tm.memberCode = :memberCode")
    List<TaskEntity> getTasksByMember(@Param("memberCode") Long memberCode);

    @Query("SELECT t FROM TaskEntity t WHERE t.taskState.id IN :taskStates")
    List<TaskEntity> getTasksByStates(@Param("taskStates") List<Long> taskStates);

    @Query(nativeQuery = true, value = "SELECT t.* FROM tasks.tasks t JOIN tasks.tasks_members tm ON t.id = tm.task_id AND tm.member_code = :memberCode "
            + "JOIN tasks.tasks_x_categories tc ON t.id = tc.task_id AND tc.category_id IN :taskCategories "
            + "WHERE t.task_state_id IN :taskStates")
    List<TaskEntity> getTasksByMemberAndStatesAndCategories(@Param("memberCode") Long memberCode,
            @Param("taskStates") List<Long> taskStates, @Param("taskCategories") List<Long> taskCategories);

    @Query(nativeQuery = true, value = "SELECT t.* FROM tasks.tasks t JOIN tasks.tasks_members tm ON t.id = tm.task_id AND tm.member_code = :memberCode "
            + "JOIN tasks.tasks_x_categories tc ON t.id = tc.task_id AND tc.category_id IN :taskCategories")
    List<TaskEntity> getTasksByMemberAndCategories(@Param("memberCode") Long memberCode,
            @Param("taskCategories") List<Long> taskCategories);

    @Query(nativeQuery = true, value = "SELECT t.* FROM tasks.tasks t "
            + "JOIN tasks.tasks_x_categories tc ON t.id = tc.task_id AND tc.category_id IN :taskCategories "
            + "WHERE t.task_state_id IN :taskStates")
    List<TaskEntity> getTasksByStatesAndCategories(@Param("taskStates") List<Long> taskStates,
            @Param("taskCategories") List<Long> taskCategories);

    @Query(nativeQuery = true, value = "SELECT t.* FROM tasks.tasks t "
            + "JOIN tasks.tasks_x_categories tc ON t.id = tc.task_id AND tc.category_id IN :taskCategories")
    List<TaskEntity> getTasksByCategories(@Param("taskCategories") List<Long> taskCategories);

}
