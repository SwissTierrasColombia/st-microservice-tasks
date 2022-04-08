package com.ai.st.microservice.tasks.models.services;

import com.ai.st.microservice.tasks.entities.TaskCategoryEntity;

public interface ITaskCategoryService {

    TaskCategoryEntity getCategoryById(Long id);

    Long getCount();

    TaskCategoryEntity createTaskCategory(TaskCategoryEntity categoryEntity);

}
