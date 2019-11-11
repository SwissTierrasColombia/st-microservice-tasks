package com.ai.st.microservice.tasks.services;

import java.util.List;

import com.ai.st.entities.schema.administration.UserEntity;
import com.ai.st.entities.schema.tasks.TaskEntity;
import com.ai.st.entities.schema.tasks.TaskStateEntity;

public interface ITaskService {

	public List<TaskEntity> getAllTasks();

	public TaskEntity createTask(TaskEntity task);

	public TaskEntity getById(Long id);

	public List<TaskEntity> getTasksByUser(UserEntity user);

	public List<TaskEntity> getTasksByUserAndState(UserEntity user, TaskStateEntity taskState);

	public TaskEntity updateTask(TaskEntity task);

}
