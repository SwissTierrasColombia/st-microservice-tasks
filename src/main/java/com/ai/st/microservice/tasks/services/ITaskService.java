package com.ai.st.microservice.tasks.services;

import java.util.List;

import com.ai.st.microservice.tasks.entities.TaskEntity;

public interface ITaskService {

	public List<TaskEntity> getAllTasks();

	public TaskEntity createTask(TaskEntity task);

	public TaskEntity getById(Long id);

	public List<TaskEntity> getTasksByStatesAndMember(List<Long> taskStates, Long memberCode);

	public List<TaskEntity> getTasksByStates(List<Long> taskStates);

	public List<TaskEntity> getTasksByMember(Long memberCode);

	public TaskEntity updateTask(TaskEntity task);

	public Long getCount();

	public List<TaskEntity> getTasksByStatesAndMemberAndCategories(List<Long> taskStates, List<Long> taskCategories,
			Long memberCode);

	public List<TaskEntity> getTasksByMemberAndCategories(List<Long> taskCategories, Long memberCode);

	public List<TaskEntity> getTasksByStatesAndCategories(List<Long> taskCategories, List<Long> taskStates);

	public List<TaskEntity> getTasksByCategories(List<Long> taskCategories);

}
