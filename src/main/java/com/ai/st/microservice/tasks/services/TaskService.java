package com.ai.st.microservice.tasks.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.st.entities.schema.administration.UserEntity;
import com.ai.st.entities.schema.tasks.TaskEntity;
import com.ai.st.entities.schema.tasks.TaskStateEntity;
import com.ai.st.microservice.tasks.repositories.TaskRepository;

@Service
public class TaskService implements ITaskService {

	@Autowired
	private TaskRepository taskRepository;

	@Override
	@Transactional
	public List<TaskEntity> getAllTasks() {
		return taskRepository.findAll();
	}

	@Override
	@Transactional
	public TaskEntity createTask(TaskEntity taskEntity) {
		return taskRepository.save(taskEntity);
	}

	@Override
	@Transactional
	public TaskEntity getById(Long id) {
		return taskRepository.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public List<TaskEntity> getTasksByUser(UserEntity user) {
		return taskRepository.findByUser(user);
	}

	@Override
	@Transactional
	public List<TaskEntity> getTasksByUserAndState(UserEntity user, TaskStateEntity taskState) {
		return taskRepository.findByUserAndTaskState(user, taskState);
	}

	@Override
	public TaskEntity updateTask(TaskEntity task) {
		return taskRepository.save(task);
	}

}
