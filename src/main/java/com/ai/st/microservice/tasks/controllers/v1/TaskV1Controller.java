package com.ai.st.microservice.tasks.controllers.v1;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ai.st.microservice.tasks.business.TaskBusiness;
import com.ai.st.microservice.tasks.dto.BasicResponseDto;
import com.ai.st.microservice.tasks.dto.CancelTaskDto;
import com.ai.st.microservice.tasks.dto.CreateTaskDto;
import com.ai.st.microservice.tasks.dto.ErrorDto;
import com.ai.st.microservice.tasks.dto.TaskDto;
import com.ai.st.microservice.tasks.exceptions.BusinessException;
import com.ai.st.microservice.tasks.exceptions.InputValidationException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "Manage Tasks", description = "Manage Tasks", tags = { "Tasks" })
@RestController
@RequestMapping("api/tasks/v1/tasks")
public class TaskV1Controller {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TaskBusiness taskBusiness;

	@GetMapping("")
	@ApiOperation(value = "Get all tasks")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Get tasks", response = TaskDto.class),
			@ApiResponse(code = 500, message = "Error Server") })
	public ResponseEntity<List<TaskDto>> getAllTasks(@RequestParam(required = false, name = "member") Long memberCode,
			@RequestParam(required = false, name = "states") List<Long> taskStates,
			@RequestParam(required = false, name = "categories") List<Long> categories) {

		HttpStatus httpStatus = null;
		List<TaskDto> listTasks = new ArrayList<TaskDto>();

		try {
			listTasks = taskBusiness.getTasksByFilters(memberCode, taskStates, categories);

			httpStatus = HttpStatus.OK;
		} catch (Exception e) {
			log.error("Error TaskController@getAllTasks ---> " + e.getMessage());
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		return new ResponseEntity<>(listTasks, httpStatus);
	}

	@PostMapping("")
	@ApiOperation(value = "Create task")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Task created", response = TaskDto.class),
			@ApiResponse(code = 500, message = "Error Server") })
	public ResponseEntity<Object> createTask(@RequestBody(required = true) CreateTaskDto taskRequest) {

		HttpStatus httpStatus = null;
		Object responseDto = null;

		try {

			// validation name
			String taskName = taskRequest.getName();
			if (taskName.isEmpty() || taskName == null) {
				throw new InputValidationException("El nombre de la tarea es requerido");
			}

			// validation deadline
			Date taskDeadline = null;
			String taskDeadlineString = taskRequest.getDeadline();
			if (taskDeadlineString != null && !taskDeadlineString.isEmpty()) {
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					taskDeadline = sdf.parse(taskDeadlineString);
				} catch (Exception e) {
					throw new InputValidationException("La fecha límite es inválida.");
				}
			}

			// validation users
			List<Long> taskUsers = taskRequest.getUsers();
			if (taskUsers.size() == 0) {
				throw new InputValidationException("La tarea debe tener al menos un usuario.");
			}

			// validation categories
			List<Long> taskCategories = taskRequest.getCategories();
			if (taskCategories.size() == 0) {
				throw new InputValidationException("La tarea debe tener al menos una categoria.");
			}

			responseDto = taskBusiness.createTask(taskName, taskRequest.getDescription(), taskUsers, taskDeadline,
					taskCategories, taskRequest.getMetadata(), taskRequest.getSteps());
			httpStatus = HttpStatus.CREATED;

		} catch (InputValidationException e) {
			log.error("Error TaskController@createTask#Validation ---> " + e.getMessage());
			httpStatus = HttpStatus.BAD_REQUEST;
			responseDto = new ErrorDto(e.getMessage(), 1);
		} catch (BusinessException e) {
			log.error("Error TaskController@createTask#Business ---> " + e.getMessage());
			httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
			responseDto = new ErrorDto(e.getMessage(), 2);
		} catch (Exception e) {
			log.error("Error TaskController@createTask#General ---> " + e.getMessage());
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			responseDto = new ErrorDto(e.getMessage(), 3);
		}

		return new ResponseEntity<>(responseDto, httpStatus);
	}

	@GetMapping("/{id}")
	@ApiOperation(value = "Get task by id")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Get tasks", response = TaskDto.class),
			@ApiResponse(code = 404, message = "Task not found"), @ApiResponse(code = 500, message = "Error Server") })
	public ResponseEntity<TaskDto> getTaskById(@PathVariable(required = true, name = "id") Long id) {

		HttpStatus httpStatus = null;
		TaskDto taskDto = null;

		try {
			taskDto = taskBusiness.getTaskById(id);
			httpStatus = (taskDto instanceof TaskDto) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
		} catch (Exception e) {
			log.error("Error TaskController@getTaskById ---> " + e.getMessage());
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		return new ResponseEntity<>(taskDto, httpStatus);
	}

	@PutMapping("/{id}/close")
	@ApiOperation(value = "Close task")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Task updated", response = TaskDto.class),
			@ApiResponse(code = 404, message = "Task not found"), @ApiResponse(code = 500, message = "Error Server") })
	public ResponseEntity<TaskDto> closeTask(@PathVariable(required = true) Long id) {

		HttpStatus httpStatus = null;
		TaskDto taskDtoResponse = null;

		try {

			taskDtoResponse = taskBusiness.closeTask(id);
			httpStatus = HttpStatus.OK;

		} catch (BusinessException e) {
			log.error("Error TaskController@closeTask1 ---> " + e.getMessage());
			httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
		} catch (Exception e) {
			log.error("Error TaskController@closeTask2 ---> " + e.getMessage());
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		return new ResponseEntity<>(taskDtoResponse, httpStatus);
	}

	@PutMapping("/{id}/start")
	@ApiOperation(value = "Start task")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Task updated", response = TaskDto.class),
			@ApiResponse(code = 404, message = "Task not found"), @ApiResponse(code = 500, message = "Error Server") })
	public ResponseEntity<TaskDto> startTask(@PathVariable(required = true) Long id) {

		HttpStatus httpStatus = null;
		TaskDto taskDtoResponse = null;

		try {

			taskDtoResponse = taskBusiness.startTask(id);
			httpStatus = HttpStatus.OK;

		} catch (BusinessException e) {
			log.error("Error TaskController@closeTask#Business ---> " + e.getMessage());
			httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
		} catch (Exception e) {
			log.error("Error TaskController@closeTask#General ---> " + e.getMessage());
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		return new ResponseEntity<>(taskDtoResponse, httpStatus);
	}

	@PutMapping("/{id}/cancel")
	@ApiOperation(value = "Cancel task")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Task updated", response = TaskDto.class),
			@ApiResponse(code = 404, message = "Task not found"), @ApiResponse(code = 500, message = "Error Server") })
	public ResponseEntity<TaskDto> cancelTask(@PathVariable(required = true) Long id,
			@RequestBody(required = true) CancelTaskDto cancelTaskRequest) {

		HttpStatus httpStatus = null;
		TaskDto taskDtoResponse = null;

		try {

			taskDtoResponse = taskBusiness.cancelTask(id, cancelTaskRequest.getReason());
			httpStatus = HttpStatus.OK;

		} catch (BusinessException e) {
			log.error("Error TaskController@closeTask#Business ---> " + e.getMessage());
			httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
		} catch (Exception e) {
			log.error("Error TaskController@closeTask#General ---> " + e.getMessage());
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		return new ResponseEntity<>(taskDtoResponse, httpStatus);
	}

	@DeleteMapping("/{taskId}/members/{memberId}/")
	@ApiOperation(value = "Remove member from task")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Task updated", response = TaskDto.class),
			@ApiResponse(code = 404, message = "Member removed"), @ApiResponse(code = 500, message = "Error Server") })
	public ResponseEntity<?> removeMemberFromTask(@PathVariable(required = true) Long taskId,
			@PathVariable(required = true) Long memberId) {

		HttpStatus httpStatus = null;
		Object responseDto = null;

		try {

			responseDto = taskBusiness.removeUserFromTask(taskId, memberId);
			httpStatus = HttpStatus.OK;

		} catch (BusinessException e) {
			log.error("Error TaskController@removeMemberFromTask#Business ---> " + e.getMessage());
			httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
			responseDto = new BasicResponseDto(e.getMessage(), 2);
		} catch (Exception e) {
			log.error("Error TaskController@removeMemberFromTask#General ---> " + e.getMessage());
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
			responseDto = new BasicResponseDto(e.getMessage(), 1);
		}

		return new ResponseEntity<>(responseDto, httpStatus);
	}

}
