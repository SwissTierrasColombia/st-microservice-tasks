package com.ai.st.microservice.tasks.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ai.st.entities.schema.administration.UserEntity;
import com.ai.st.entities.schema.tasks.TaskEntity;
import com.ai.st.entities.schema.tasks.TaskStateEntity;

public interface TaskRepository extends CrudRepository<TaskEntity, Long> {

	@Override
	List<TaskEntity> findAll();

	List<TaskEntity> findByUser(UserEntity user);

	List<TaskEntity> findByUserAndTaskState(UserEntity user, TaskStateEntity taskState);

}
