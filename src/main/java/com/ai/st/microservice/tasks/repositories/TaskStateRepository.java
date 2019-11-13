package com.ai.st.microservice.tasks.repositories;

import org.springframework.data.repository.CrudRepository;

import com.ai.st.microservice.tasks.entities.TaskStateEntity;

public interface TaskStateRepository extends CrudRepository<TaskStateEntity, Long> {

}
