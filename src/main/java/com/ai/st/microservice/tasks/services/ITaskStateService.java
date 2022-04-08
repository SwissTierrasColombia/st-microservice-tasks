package com.ai.st.microservice.tasks.services;

import com.ai.st.microservice.tasks.entities.TaskStateEntity;

public interface ITaskStateService {

    public TaskStateEntity getById(Long id);

    public Long getAllCount();

    public TaskStateEntity createTaskState(TaskStateEntity taskEntity);

}
