package com.ai.st.microservice.tasks.models.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.st.microservice.tasks.entities.TaskTypeStepEntity;
import com.ai.st.microservice.tasks.models.repositories.TaskTypeStepRepository;

@Service
public class TaskTypeStepService implements ITaskTypeStepService {

    @Autowired
    private TaskTypeStepRepository typeStepRepository;

    @Override
    public Long getCount() {
        return typeStepRepository.count();
    }

    @Override
    @Transactional
    public TaskTypeStepEntity createTypeStep(TaskTypeStepEntity typeStepEntity) {
        return typeStepRepository.save(typeStepEntity);
    }

    @Override
    public TaskTypeStepEntity getTypeStepById(Long id) {
        return typeStepRepository.findById(id).orElse(null);
    }

}
