package com.ai.st.microservice.tasks.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ai.st.entities.schema.administration.UserEntity;
import com.ai.st.entities.schema.tasks.TaskEntity;
import com.ai.st.entities.schema.tasks.TaskStateEntity;
import com.ai.st.microservice.tasks.clients.UserFeignClient;
import com.ai.st.microservice.tasks.dto.TaskDto;
import com.ai.st.microservice.tasks.dto.TaskStateDto;
import com.ai.st.microservice.tasks.dto.UserDto;
import com.ai.st.microservice.tasks.exceptions.BusinessException;
import com.ai.st.microservice.tasks.services.ITaskService;

import feign.FeignException;

@Component
public class TaskBusiness {

	@Autowired
	private ITaskService taskService;

	@Autowired
	private TaskStateBusiness taskStateBusiness;

	@Autowired
	private UserFeignClient userClient;

	public List<TaskDto> getAllTasks() {

		List<TaskDto> listTasksDto = new ArrayList<TaskDto>();

		List<TaskEntity> listTasksEntity = taskService.getAllTasks();
		for (TaskEntity taskEntity : listTasksEntity) {
			TaskDto taskDto = entityParseDto(taskEntity);
			listTasksDto.add(taskDto);
		}

		return listTasksDto;
	}

	public TaskDto createTask(String name, String description, Long userId, Long createdBy, Date deadline)
			throws BusinessException {

		TaskDto taskDto = null;

		Date currentDate = new Date();

		// validate if the deadline is greater than the current date
		if (deadline != null) {
			if (!deadline.after(currentDate)) {
				throw new BusinessException("The deadline must be greater than the current date.");
			}
		}

		// validate if the user exists
		try {
			userClient.findById(userId);
		} catch (FeignException e) {
			throw new BusinessException("User not found.");
		}

		// validate if the user who registers the task exists
		try {
			userClient.findById(createdBy);
		} catch (FeignException e) {
			throw new BusinessException("User not found.");
		}

		// set task state
		TaskStateDto taskStateDto = taskStateBusiness.getById(TaskStateBusiness.TASK_STATE_ASSIGNED);
		if (taskStateDto == null) {
			throw new BusinessException("Task state not found.");
		}

		try {
			TaskEntity taskEntity = new TaskEntity();
			taskEntity.setName(name);
			taskEntity.setDescription(description);
			taskEntity.setDeadline(deadline);
			taskEntity.setCreatedAt(currentDate);

			UserEntity userEntity = new UserEntity();
			userEntity.setId(userId);
			taskEntity.setUser(userEntity);

			UserEntity createdByEntity = new UserEntity();
			createdByEntity.setId(createdBy);
			taskEntity.setCreatedBy(createdByEntity);

			TaskStateEntity taskStateEntity = new TaskStateEntity();
			taskStateEntity.setId(taskStateDto.getId());
			taskEntity.setTaskState(taskStateEntity);

			TaskEntity newTaskEntity = taskService.createTask(taskEntity);
			taskDto = entityParseDto(newTaskEntity);

		} catch (Exception e) {
			throw new BusinessException("The task could not be created.");
		}

		return taskDto;
	}

	public TaskDto getTaskById(Long id) {

		TaskDto taskDto = null;

		TaskEntity taskEntity = taskService.getById(id);
		if (taskEntity instanceof TaskEntity) {
			taskDto = entityParseDto(taskEntity);
		}

		return taskDto;
	}

	public List<TaskDto> getTaskByUserAndTaskState(Long userId, Long taskStateId) {

		List<TaskDto> listTasksDto = new ArrayList<TaskDto>();

		UserEntity userEntity = new UserEntity();
		userEntity.setId(userId);

		TaskStateEntity taskStateEntity = new TaskStateEntity();
		taskStateEntity.setId(taskStateId);

		List<TaskEntity> listTasksEntity = taskService.getTasksByUserAndState(userEntity, taskStateEntity);
		for (TaskEntity taskEntity : listTasksEntity) {
			TaskDto taskDto = entityParseDto(taskEntity);
			listTasksDto.add(taskDto);
		}

		return listTasksDto;
	}

	public TaskDto entityParseDto(TaskEntity taskEntity) {

		TaskDto taskDto = null;

		if (taskEntity instanceof TaskEntity) {
			taskDto = new TaskDto();

			taskDto.setId(taskEntity.getId());
			taskDto.setName(taskEntity.getName());
			taskDto.setDescription(taskEntity.getDescription());
			taskDto.setCreatedAt(taskEntity.getCreatedAt());
			taskDto.setDeadline(taskEntity.getDeadline());
			taskDto.setClosingDate(taskEntity.getClosingDate());

			// set state
			TaskStateEntity taskStateEntity = taskEntity.getTaskState();
			taskDto.setTaskState(new TaskStateDto(taskStateEntity.getId(), taskStateEntity.getName()));

			// set user
			UserEntity userEntity = taskEntity.getUser();
			taskDto.setUser(new UserDto(userEntity.getId(), userEntity.getFirstName(), userEntity.getLastName(),
					userEntity.getEmail(), userEntity.getUsername()));

			// set createdBy
			UserEntity createdByEntity = taskEntity.getCreatedBy();
			taskDto.setCreatedBy(new UserDto(createdByEntity.getId(), createdByEntity.getFirstName(),
					createdByEntity.getLastName(), createdByEntity.getEmail(), createdByEntity.getUsername()));
		}

		return taskDto;
	}

}
