package com.ai.st.microservice.tasks.models.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.st.microservice.tasks.entities.TaskCategoryEntity;
import com.ai.st.microservice.tasks.models.repositories.TaskCategoryRepository;

@Service
public class TaskCategoryService implements ITaskCategoryService {

    @Autowired
    private TaskCategoryRepository taskCategoryRepository;

    @Override
    public TaskCategoryEntity getCategoryById(Long id) {
        return taskCategoryRepository.findById(id).orElse(null);
    }

    @Override
    public Long getCount() {
        return taskCategoryRepository.count();
    }

    @Override
    @Transactional
    public TaskCategoryEntity createTaskCategory(TaskCategoryEntity categoryEntity) {
        return taskCategoryRepository.save(categoryEntity);
    }

}
