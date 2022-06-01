package com.ai.st.microservice.tasks.models.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.st.microservice.tasks.entities.TaskEntity;
import com.ai.st.microservice.tasks.models.repositories.TaskRepository;

@Service
public class TaskService implements ITaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Override
    @Transactional
    public List<TaskEntity> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    @Transactional
    public TaskEntity createTask(TaskEntity taskEntity) {
        return taskRepository.save(taskEntity);
    }

    @Override
    @Transactional
    public TaskEntity getById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    @Override
    public TaskEntity updateTask(TaskEntity task) {
        return taskRepository.save(task);
    }

    @Override
    public List<TaskEntity> getTasksByMember(Long memberCode) {
        return taskRepository.getTasksByMember(memberCode);
    }

    @Override
    public Long getCount() {
        return taskRepository.count();
    }

    @Override
    public List<TaskEntity> getTasksByStates(List<Long> taskStates) {
        return taskRepository.getTasksByStates(taskStates);
    }

    @Override
    public List<TaskEntity> getTasksByStatesAndMember(List<Long> taskStates, Long memberCode) {
        return taskRepository.getTasksByMemberAndStates(memberCode, taskStates);
    }

    @Override
    public List<TaskEntity> getTasksByStatesAndMemberAndCategories(List<Long> taskStates, List<Long> taskCategories,
            Long memberCode) {
        return taskRepository.getTasksByMemberAndStatesAndCategories(memberCode, taskStates, taskCategories);
    }

    @Override
    public List<TaskEntity> getTasksByMemberAndCategories(List<Long> taskCategories, Long memberCode) {
        return taskRepository.getTasksByMemberAndCategories(memberCode, taskCategories);
    }

    @Override
    public List<TaskEntity> getTasksByStatesAndCategories(List<Long> taskCategories, List<Long> taskStates) {
        return taskRepository.getTasksByStatesAndCategories(taskStates, taskCategories);
    }

    @Override
    public List<TaskEntity> getTasksByCategories(List<Long> taskCategories) {
        return taskRepository.getTasksByCategories(taskCategories);
    }

}
