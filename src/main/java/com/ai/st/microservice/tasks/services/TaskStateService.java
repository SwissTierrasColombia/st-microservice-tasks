package com.ai.st.microservice.tasks.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.st.microservice.tasks.entities.TaskStateEntity;
import com.ai.st.microservice.tasks.repositories.TaskStateRepository;

@Service
public class TaskStateService implements ITaskStateService {

    @Autowired
    private TaskStateRepository taskStateRepository;

    @Override
    public TaskStateEntity getById(Long id) {
        return taskStateRepository.findById(id).orElse(null);
    }

    @Override
    public Long getAllCount() {
        return taskStateRepository.count();
    }

    @Override
    @Transactional
    public TaskStateEntity createTaskState(TaskStateEntity taskEntity) {
        return taskStateRepository.save(taskEntity);
    }

}
