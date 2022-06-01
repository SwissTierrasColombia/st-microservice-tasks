package com.ai.st.microservice.tasks.models.services;

import com.ai.st.microservice.tasks.entities.TaskTypeStepEntity;

public interface ITaskTypeStepService {

    Long getCount();

    TaskTypeStepEntity createTypeStep(TaskTypeStepEntity typeStepEntity);

    TaskTypeStepEntity getTypeStepById(Long id);

}
