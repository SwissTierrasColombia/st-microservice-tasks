package com.ai.st.microservice.tasks.models.services;

import com.ai.st.microservice.tasks.entities.TaskStateEntity;

public interface ITaskStateService {

    TaskStateEntity getById(Long id);

    Long getAllCount();

    TaskStateEntity createTaskState(TaskStateEntity taskEntity);

}
