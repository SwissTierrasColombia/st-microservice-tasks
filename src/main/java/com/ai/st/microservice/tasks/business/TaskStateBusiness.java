package com.ai.st.microservice.tasks.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ai.st.entities.schema.tasks.TaskStateEntity;
import com.ai.st.microservice.tasks.dto.TaskStateDto;
import com.ai.st.microservice.tasks.services.ITaskStateService;

@Component
public class TaskStateBusiness {

	static final Long TASK_STATE_ASSIGNED = (long) 1;
	static final Long TASK_STATE_CLOSED = (long) 2;
	static final Long TASK_STATE_CANCELLED = (long) 3;

	@Autowired
	private ITaskStateService taskStateService;

	public TaskStateDto getById(Long id) {

		TaskStateDto taskStateDto = null;

		TaskStateEntity taskStateEntity = taskStateService.getById(id);
		if (taskStateEntity instanceof TaskStateEntity) {
			taskStateDto = new TaskStateDto(taskStateEntity.getId(), taskStateEntity.getName());
		}

		return taskStateDto;
	}

}
