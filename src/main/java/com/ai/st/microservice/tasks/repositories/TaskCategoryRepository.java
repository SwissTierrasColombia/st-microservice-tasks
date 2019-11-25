package com.ai.st.microservice.tasks.repositories;

import org.springframework.data.repository.CrudRepository;

import com.ai.st.microservice.tasks.entities.TaskCategoryEntity;

public interface TaskCategoryRepository extends CrudRepository<TaskCategoryEntity, Long> {

}
