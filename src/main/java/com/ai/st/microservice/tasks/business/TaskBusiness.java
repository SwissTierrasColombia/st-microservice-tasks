package com.ai.st.microservice.tasks.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ai.st.microservice.tasks.dto.CreateTaskMetadataDto;
import com.ai.st.microservice.tasks.dto.TaskCategoryDto;
import com.ai.st.microservice.tasks.dto.TaskDto;
import com.ai.st.microservice.tasks.dto.TaskMemberDto;
import com.ai.st.microservice.tasks.dto.TaskMetadataDto;
import com.ai.st.microservice.tasks.dto.TaskStateDto;
import com.ai.st.microservice.tasks.dto.TaskStepDto;
import com.ai.st.microservice.tasks.dto.TaskTypeStepDto;
import com.ai.st.microservice.tasks.entities.TaskCategoryEntity;
import com.ai.st.microservice.tasks.entities.TaskEntity;
import com.ai.st.microservice.tasks.entities.TaskMemberEntity;
import com.ai.st.microservice.tasks.entities.TaskMetadataEntity;
import com.ai.st.microservice.tasks.entities.TaskStateEntity;
import com.ai.st.microservice.tasks.entities.TaskStepEntity;
import com.ai.st.microservice.tasks.exceptions.BusinessException;
import com.ai.st.microservice.tasks.services.ITaskService;
import com.ai.st.microservice.tasks.services.TaskCategoryService;

@Component
public class TaskBusiness {

	@Autowired
	private ITaskService taskService;

	@Autowired
	private TaskStateBusiness taskStateBusiness;

	@Autowired
	private TaskCategoryService taskCategoryService;

	public List<TaskDto> getAllTasks() {

		List<TaskDto> listTasksDto = new ArrayList<TaskDto>();

		List<TaskEntity> listTasksEntity = taskService.getAllTasks();
		for (TaskEntity taskEntity : listTasksEntity) {
			TaskDto taskDto = entityParseDto(taskEntity);
			listTasksDto.add(taskDto);
		}

		return listTasksDto;
	}

	public TaskDto createTask(String name, String description, List<Long> users, Date deadline, List<Long> categories,
			List<CreateTaskMetadataDto> metadata) throws BusinessException {

		TaskDto taskDto = null;

		Date currentDate = new Date();

		// validate if the deadline is greater than the current date
		if (deadline != null) {
			if (!deadline.after(currentDate)) {
				throw new BusinessException("The deadline must be greater than the current date.");
			}
		}

		// set task state
		TaskStateDto taskStateDto = taskStateBusiness.getById(TaskStateBusiness.TASK_STATE_ASSIGNED);
		if (taskStateDto == null) {
			throw new BusinessException("Task state not found.");
		}

		List<Long> listUsers = users.stream().distinct().collect(Collectors.toList());
		List<Long> listCategories = categories.stream().distinct().collect(Collectors.toList());

		// validate categories
		for (Long categoryId : listCategories) {
			TaskCategoryEntity categoryEntity = taskCategoryService.getCategoryById(categoryId);
			if (!(categoryEntity instanceof TaskCategoryEntity)) {
				throw new BusinessException("No se ha encontrado la cagegoría.");
			}
		}

		// validate metadata
		if (metadata.size() > 0) {
			for (CreateTaskMetadataDto meta : metadata) {
				if (meta.getKey().isEmpty() || meta.getKey() == null) {
					throw new BusinessException("Metadato inválido.");
				}
				if (meta.getValue().isEmpty() || meta.getValue() == null) {
					throw new BusinessException("Metadato inválido.");
				}
			}
		}

		try {
			TaskEntity taskEntity = new TaskEntity();
			taskEntity.setName(name);
			taskEntity.setDescription(description);
			taskEntity.setDeadline(deadline);
			taskEntity.setCreatedAt(currentDate);

			TaskStateEntity taskStateEntity = new TaskStateEntity();
			taskStateEntity.setId(taskStateDto.getId());
			taskEntity.setTaskState(taskStateEntity);

			// set members
			List<TaskMemberEntity> members = new ArrayList<TaskMemberEntity>();
			for (Long userCode : listUsers) {
				TaskMemberEntity taskMemberEntity = new TaskMemberEntity();
				taskMemberEntity.setMemberCode(userCode);
				taskMemberEntity.setCreatedAt(currentDate);
				taskMemberEntity.setTask(taskEntity);
				members.add(taskMemberEntity);
			}
			taskEntity.setMembers(members);

			// set categories
			List<TaskCategoryEntity> categoriesEntity = new ArrayList<TaskCategoryEntity>();
			for (Long categoryId : listCategories) {
				TaskCategoryEntity category = new TaskCategoryEntity();
				category.setId(categoryId);
				categoriesEntity.add(category);
			}
			taskEntity.setCategories(categoriesEntity);

			// set metadata
			if (metadata.size() > 0) {
				List<TaskMetadataEntity> listMetadataEntity = new ArrayList<TaskMetadataEntity>();
				for (CreateTaskMetadataDto meta : metadata) {
					TaskMetadataEntity metadataEntity = new TaskMetadataEntity();
					metadataEntity.setKey(meta.getKey());
					metadataEntity.setValue(meta.getValue());
					metadataEntity.setTask(taskEntity);
					listMetadataEntity.add(metadataEntity);
				}
				taskEntity.setMetadata(listMetadataEntity);
			}

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

	public List<TaskDto> getTasksByFilters(Long memberCode, Long taskStateId) {

		List<TaskDto> listTasksDto = new ArrayList<TaskDto>();

		List<TaskEntity> listTasksEntity = new ArrayList<TaskEntity>();

		if (memberCode != null && taskStateId != null) {
			listTasksEntity = taskService.getTasksByStateAndMember(taskStateId, memberCode);
		} else if (memberCode != null) {
			listTasksEntity = taskService.getTasksByMember(memberCode);
		} else if (taskStateId != null) {
			listTasksEntity = taskService.getTasksByState(taskStateId);
		} else {
			listTasksEntity = taskService.getAllTasks();
		}

		for (TaskEntity taskEntity : listTasksEntity) {
			TaskDto taskDto = entityParseDto(taskEntity);
			listTasksDto.add(taskDto);
		}

		return listTasksDto;
	}

	public TaskDto closeTask(Long taskId) throws BusinessException {

		TaskDto taskDto = null;

		// verify task exists
		TaskEntity taskEntity = taskService.getById(taskId);
		if (!(taskEntity instanceof TaskEntity)) {
			throw new BusinessException("Task not found.");
		}

		Date currentDate = new Date();
		taskEntity.setClosingDate(currentDate);

		// set task state
		TaskStateDto taskStateDto = taskStateBusiness.getById(TaskStateBusiness.TASK_STATE_CLOSED);
		if (taskStateDto == null) {
			throw new BusinessException("Task state not found.");
		}
		TaskStateEntity taskStateEntity = new TaskStateEntity();
		taskStateEntity.setId(taskStateDto.getId());
		taskEntity.setTaskState(taskStateEntity);

		try {

			TaskEntity taskUpdatedEntity = taskService.updateTask(taskEntity);
			taskDto = entityParseDto(taskUpdatedEntity);

		} catch (Exception e) {
			throw new BusinessException("The task could not be updated.");
		}

		return taskDto;
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

			// set members
			for (TaskMemberEntity taskMemberEntity : taskEntity.getMembers()) {
				TaskMemberDto member = new TaskMemberDto();
				member.setId(taskMemberEntity.getId());
				member.setMemberCode(taskMemberEntity.getMemberCode());
				member.setCreatedAt(taskMemberEntity.getCreatedAt());
				taskDto.getMembers().add(member);
			}

			// set categories
			for (TaskCategoryEntity categoryEntity : taskEntity.getCategories()) {
				TaskCategoryDto categoryDto = new TaskCategoryDto();
				categoryDto.setId(categoryEntity.getId());
				categoryDto.setName(categoryEntity.getName());
				taskDto.getCategories().add(categoryDto);
			}

			// set metadata
			for (TaskMetadataEntity metadata : taskEntity.getMetadata()) {
				TaskMetadataDto metadataDto = new TaskMetadataDto();
				metadataDto.setId(metadata.getId());
				metadataDto.setKey(metadata.getKey());
				metadataDto.setValue(metadata.getValue());
				taskDto.getMetadata().add(metadataDto);
			}

			// set steps
			for (TaskStepEntity stepEntity : taskEntity.getSteps()) {
				TaskStepDto stepDto = new TaskStepDto();
				stepDto.setCode(stepEntity.getCode());
				stepDto.setDescription(stepEntity.getDescription());
				stepDto.setId(stepEntity.getId());
				stepDto.setStatus(stepEntity.getStatus());
				stepDto.setTypeStep(
						new TaskTypeStepDto(stepEntity.getTypeStep().getId(), stepEntity.getTypeStep().getName()));
				taskDto.getSteps().add(stepDto);
			}

		}

		return taskDto;
	}

}
