package com.ai.st.microservice.tasks.services;

import com.ai.st.entities.schema.tasks.TaskStateEntity;

public interface ITaskStateService {

	public TaskStateEntity getById(Long id);

	public Long getAllCount();

	public TaskStateEntity createTaskState(TaskStateEntity taskEntity);

}
