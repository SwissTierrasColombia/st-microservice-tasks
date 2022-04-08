package com.ai.st.microservice.tasks.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ai.st.microservice.tasks.dto.TaskStateDto;
import com.ai.st.microservice.tasks.entities.TaskStateEntity;
import com.ai.st.microservice.tasks.services.ITaskStateService;

@Component
public class TaskStateBusiness {

    public static final Long TASK_STATE_ASSIGNED = (long) 1;
    public static final Long TASK_STATE_CLOSED = (long) 2;
    public static final Long TASK_STATE_CANCELLED = (long) 3;
    public static final Long TASK_STATE_STARTED = (long) 4;

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
