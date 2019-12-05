package com.ai.st.microservice.tasks.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.st.microservice.tasks.entities.TaskEntity;
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
	public List<TaskEntity> getTasksByStateAndMember(Long taskStateId, Long memberCode) {
		return taskRepository.getTasksByMemberAndState(memberCode, taskStateId);
	}

	@Override
	public TaskEntity updateTask(TaskEntity task) {
		return taskRepository.save(task);
	}

	@Override
	public List<TaskEntity> getTasksByState(Long taskStateId) {
		return taskRepository.getTasksByState(taskStateId);
	}

	@Override
	public List<TaskEntity> getTasksByMember(Long memberCode) {
		return taskRepository.getTasksByMember(memberCode);
	}

	@Override
	public Long getCount() {
		return taskRepository.count();
	}

}
