package com.ai.st.microservice.tasks.services;

import com.ai.st.microservice.tasks.entities.TaskCategoryEntity;

public interface ITaskCategoryService {

	public TaskCategoryEntity getCategoryById(Long id);

	public Long getCount();

	public TaskCategoryEntity createTaskCategory(TaskCategoryEntity categoryEntity);

}
