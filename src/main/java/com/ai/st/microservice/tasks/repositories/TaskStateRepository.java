package com.ai.st.microservice.tasks.repositories;

import org.springframework.data.repository.CrudRepository;

import com.ai.st.entities.schema.tasks.TaskStateEntity;

public interface TaskStateRepository extends CrudRepository<TaskStateEntity, Long> {

}
