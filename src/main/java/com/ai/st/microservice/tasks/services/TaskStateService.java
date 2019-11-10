package com.ai.st.microservice.tasks.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.st.entities.schema.tasks.TaskStateEntity;
import com.ai.st.microservice.tasks.repositories.TaskStateRepository;

@Service
public class TaskStateService implements ITaskStateService {

	@Autowired
	private TaskStateRepository taskStateRepository;

	@Override
	public TaskStateEntity getById(Long id) {
		return taskStateRepository.findById(id).orElse(null);
	}

}
