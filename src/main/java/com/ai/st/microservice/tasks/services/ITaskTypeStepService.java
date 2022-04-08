package com.ai.st.microservice.tasks.services;

import com.ai.st.microservice.tasks.entities.TaskTypeStepEntity;

public interface ITaskTypeStepService {

    public Long getCount();

    public TaskTypeStepEntity createTypeStep(TaskTypeStepEntity typeStepEntity);

    public TaskTypeStepEntity getTypeStepById(Long id);

}
