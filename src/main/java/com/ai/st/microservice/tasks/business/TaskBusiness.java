package com.ai.st.microservice.tasks.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ai.st.microservice.tasks.dto.TaskDto;
import com.ai.st.microservice.tasks.dto.TaskMemberDto;
import com.ai.st.microservice.tasks.dto.TaskStateDto;
import com.ai.st.microservice.tasks.entities.TaskEntity;
import com.ai.st.microservice.tasks.entities.TaskMemberEntity;
import com.ai.st.microservice.tasks.entities.TaskStateEntity;
import com.ai.st.microservice.tasks.exceptions.BusinessException;
import com.ai.st.microservice.tasks.services.ITaskService;

@Component
public class TaskBusiness {

	@Autowired
	private ITaskService taskService;

	@Autowired
	private TaskStateBusiness taskStateBusiness;

	public List<TaskDto> getAllTasks() {

		List<TaskDto> listTasksDto = new ArrayList<TaskDto>();

		List<TaskEntity> listTasksEntity = taskService.getAllTasks();
		for (TaskEntity taskEntity : listTasksEntity) {
			TaskDto taskDto = entityParseDto(taskEntity);
			listTasksDto.add(taskDto);
		}

		return listTasksDto;
	}

	public TaskDto createTask(String name, String description, Long memberCode, Date deadline)
			throws BusinessException {

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

		try {
			TaskEntity taskEntity = new TaskEntity();
			taskEntity.setName(name);
			taskEntity.setDescription(description);
			taskEntity.setDeadline(deadline);
			taskEntity.setCreatedAt(currentDate);

			TaskStateEntity taskStateEntity = new TaskStateEntity();
			taskStateEntity.setId(taskStateDto.getId());
			taskEntity.setTaskState(taskStateEntity);

			TaskMemberEntity taskMemberEntity = new TaskMemberEntity();
			taskMemberEntity.setMemberCode(memberCode);
			taskMemberEntity.setCreatedAt(currentDate);
			taskMemberEntity.setTask(taskEntity);

			List<TaskMemberEntity> members = new ArrayList<TaskMemberEntity>();
			members.add(taskMemberEntity);

			taskEntity.setMembers(members);

			TaskEntity newTaskEntity = taskService.createTask(taskEntity);
			taskDto = entityParseDto(newTaskEntity);

		} catch (Exception e) {
			System.out.println("error creadno_ " + e.getMessage());
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

		}

		return taskDto;
	}

}
