package com.ai.st.microservice.tasks.models.services;

import java.util.List;

import com.ai.st.microservice.tasks.entities.TaskEntity;

public interface ITaskService {

    List<TaskEntity> getAllTasks();

    TaskEntity createTask(TaskEntity task);

    TaskEntity getById(Long id);

    List<TaskEntity> getTasksByStatesAndMember(List<Long> taskStates, Long memberCode);

    List<TaskEntity> getTasksByStates(List<Long> taskStates);

    List<TaskEntity> getTasksByMember(Long memberCode);

    TaskEntity updateTask(TaskEntity task);

    Long getCount();

    List<TaskEntity> getTasksByStatesAndMemberAndCategories(List<Long> taskStates, List<Long> taskCategories,
            Long memberCode);

    List<TaskEntity> getTasksByMemberAndCategories(List<Long> taskCategories, Long memberCode);

    List<TaskEntity> getTasksByStatesAndCategories(List<Long> taskCategories, List<Long> taskStates);

    List<TaskEntity> getTasksByCategories(List<Long> taskCategories);

}
