package com.ai.st.microservice.tasks.models.repositories;

import org.springframework.data.repository.CrudRepository;

import com.ai.st.microservice.tasks.entities.TaskTypeStepEntity;

public interface TaskTypeStepRepository extends CrudRepository<TaskTypeStepEntity, Long> {

}
