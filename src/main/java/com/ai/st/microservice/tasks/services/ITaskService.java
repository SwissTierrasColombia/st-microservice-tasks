package com.ai.st.microservice.tasks.services;

import java.util.List;

import com.ai.st.microservice.tasks.entities.TaskEntity;

public interface ITaskService {

	public List<TaskEntity> getAllTasks();

	public TaskEntity createTask(TaskEntity task);

	public TaskEntity getById(Long id);

	public List<TaskEntity> getTasksByStateAndMember(Long taskStateId, Long memberCode);

	public List<TaskEntity> getTasksByState(Long taskStateId);

	public List<TaskEntity> getTasksByMember(Long memberCode);

	public TaskEntity updateTask(TaskEntity task);

	public Long getCount();

}
