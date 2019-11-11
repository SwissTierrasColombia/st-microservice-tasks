package com.ai.st.microservice.tasks.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ai.st.microservice.tasks.business.TaskBusiness;
import com.ai.st.microservice.tasks.clients.UserFeignClient;
import com.ai.st.microservice.tasks.dto.TaskDto;
import com.ai.st.microservice.tasks.dto.UserDto;
import com.ai.st.microservice.tasks.exceptions.BusinessException;
import com.ai.st.microservice.tasks.swagger.models.CreateTaskModel;

import feign.FeignException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE,
		RequestMethod.OPTIONS })
@Api(value = "Manage Tasks", description = "Manage Tasks", tags = { "Manage Tasks" })
@RestController
@RequestMapping("api/tasks")
public class TaskController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private TaskBusiness taskBusiness;

	@Autowired
	private UserFeignClient userClient;

	@GetMapping("")
	@ApiOperation(value = "Get all tasks")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Get tasks", response = TaskDto.class),
			@ApiResponse(code = 500, message = "Error Server") })
	public ResponseEntity<List<TaskDto>> getAllTasks() {

		HttpStatus httpStatus = null;
		List<TaskDto> listTasks = new ArrayList<TaskDto>();

		try {

			listTasks = taskBusiness.getAllTasks();
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
	public ResponseEntity<TaskDto> createTask(@RequestBody(required = true) CreateTaskModel taskRequest,
			@RequestHeader("authorization") String headerAuthorization) {

		HttpStatus httpStatus = null;
		TaskDto taskDtoResponse = null;

		try {

			// user session
			String token = headerAuthorization.replace("Bearer ", "").trim();
			Long userSessionId = null;
			try {
				UserDto userDtoSession = userClient.findByToken(token);
				userSessionId = userDtoSession.getId();
			} catch (FeignException e) {
				userSessionId = null;
			}

			// validation data required
			String taskName = taskRequest.getName();
			String taskDescription = taskRequest.getDescription();
			String taskDeadlineString = taskRequest.getDeadline();
			Long taskUserId = taskRequest.getUserId();

			if (taskName == null || taskName.isEmpty() || taskUserId == null) {
				httpStatus = HttpStatus.BAD_REQUEST;
			} else {

				Date taskDeadline = null;

				if (taskDeadlineString != null && !taskDeadlineString.isEmpty()) {
					try {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						taskDeadline = sdf.parse(taskDeadlineString);
					} catch (Exception e) {
						httpStatus = HttpStatus.BAD_REQUEST;
					}
				}

				taskDtoResponse = taskBusiness.createTask(taskName, taskDescription, taskUserId, userSessionId,
						taskDeadline);
				httpStatus = HttpStatus.CREATED;
			}

		} catch (BusinessException e) {
			log.error("Error TaskController@createTask1 ---> " + e.getMessage());
			httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
		} catch (Exception e) {
			log.error("Error TaskController@createTask2 ---> " + e.getMessage());
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		return new ResponseEntity<>(taskDtoResponse, httpStatus);
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

	@GetMapping("/users")
	@ApiOperation(value = "Get tasks by user and task state")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Get tasks", response = TaskDto.class),
			@ApiResponse(code = 500, message = "Error Server") })
	public ResponseEntity<List<TaskDto>> getTasksByUserAndState(
			@RequestParam(name = "state_id", required = true) Long taskStateId,
			@RequestHeader("authorization") String headerAuthorization) {

		HttpStatus httpStatus = null;
		List<TaskDto> listTasks = new ArrayList<TaskDto>();

		try {

			// user session
			String token = headerAuthorization.replace("Bearer ", "").trim();
			Long userSessionId = null;
			try {
				UserDto userDtoSession = userClient.findByToken(token);
				userSessionId = userDtoSession.getId();
			} catch (FeignException e) {
				userSessionId = null;
			}

			listTasks = taskBusiness.getTaskByUserAndTaskState(userSessionId, taskStateId);
			httpStatus = HttpStatus.OK;
		} catch (Exception e) {
			log.error("Error TaskController@getTasksByUserAndState ---> " + e.getMessage());
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		return new ResponseEntity<>(listTasks, httpStatus);
	}

	@PutMapping("/{id}/close")
	@ApiOperation(value = "Close task")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Task updated", response = TaskDto.class),
			@ApiResponse(code = 404, message = "Task not found"), @ApiResponse(code = 500, message = "Error Server") })
	public ResponseEntity<TaskDto> closeTask(@PathVariable(required = true) Long id,
			@RequestHeader("authorization") String headerAuthorization) {

		HttpStatus httpStatus = null;
		TaskDto taskDtoResponse = null;

		try {

			// user session
			String token = headerAuthorization.replace("Bearer ", "").trim();
			Long userSessionId = null;
			try {
				UserDto userDtoSession = userClient.findByToken(token);
				userSessionId = userDtoSession.getId();
			} catch (FeignException e) {
				userSessionId = null;
			}

			taskDtoResponse = taskBusiness.closeTask(id, userSessionId);
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

}
