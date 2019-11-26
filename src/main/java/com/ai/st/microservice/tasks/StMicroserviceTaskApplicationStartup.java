package com.ai.st.microservice.tasks;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.ai.st.microservice.tasks.business.TaskCategoryBusiness;
import com.ai.st.microservice.tasks.business.TaskStateBusiness;
import com.ai.st.microservice.tasks.entities.TaskCategoryEntity;
import com.ai.st.microservice.tasks.entities.TaskEntity;
import com.ai.st.microservice.tasks.entities.TaskMemberEntity;
import com.ai.st.microservice.tasks.entities.TaskMetadataEntity;
import com.ai.st.microservice.tasks.entities.TaskStateEntity;
import com.ai.st.microservice.tasks.services.ITaskCategoryService;
import com.ai.st.microservice.tasks.services.ITaskService;
import com.ai.st.microservice.tasks.services.ITaskStateService;

@Component
public class StMicroserviceTaskApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger log = LoggerFactory.getLogger(StMicroserviceTaskApplicationStartup.class);

	@Autowired
	private ITaskStateService taskStateService;

	@Autowired
	private ITaskCategoryService taskCategoryService;

	@Autowired
	private ITaskService taskService;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		log.info("ST - Loading Domains ... ");
		this.initTasksStates();
		this.initTasksCategories();
		this.initTasks();
	}

	public void initTasksStates() {
		Long countStates = taskStateService.getAllCount();
		if (countStates == 0) {

			try {

				TaskStateEntity taskStateAssiged = new TaskStateEntity();
				taskStateAssiged.setId(TaskStateBusiness.TASK_STATE_ASSIGNED);
				taskStateAssiged.setName("ASIGNADA");
				taskStateService.createTaskState(taskStateAssiged);

				TaskStateEntity taskStateClosed = new TaskStateEntity();
				taskStateClosed.setId(TaskStateBusiness.TASK_STATE_CLOSED);
				taskStateClosed.setName("CERRADA");
				taskStateService.createTaskState(taskStateClosed);

				TaskStateEntity taskStateCancelled = new TaskStateEntity();
				taskStateCancelled.setId(TaskStateBusiness.TASK_STATE_CANCELLED);
				taskStateCancelled.setName("CANCELADA");
				taskStateService.createTaskState(taskStateCancelled);

				log.info("The domains have been loaded!");

			} catch (Exception e) {
				log.error("Failed to load domains");
			}

		}
	}

	public void initTasksCategories() {
		Long countCategories = taskCategoryService.getCount();
		if (countCategories == 0) {

			try {

				TaskCategoryEntity categoryIntegration = new TaskCategoryEntity();
				categoryIntegration.setId(TaskCategoryBusiness.TASK_CATEGORY_INTEGRATION);
				categoryIntegration.setName("INTEGRACIÓN");

				taskCategoryService.createTaskCategory(categoryIntegration);

				log.info("The domains have been loaded!");
			} catch (Exception e) {
				log.error("Failed to load domains");
			}

		}
	}

	public void initTasks() {
		Long countTasks = taskService.getCount();
		if (countTasks == 0) {

			try {

				TaskEntity taskEntity = new TaskEntity();
				taskEntity.setName("Integración XTF");
				taskEntity.setDescription("Integración catastro-registro municipio ovejas.");
				taskEntity.setDeadline(new Date());
				taskEntity.setCreatedAt(new Date());

				TaskStateEntity taskStateEntity = taskStateService.getById(TaskStateBusiness.TASK_STATE_ASSIGNED);
				taskEntity.setTaskState(taskStateEntity);

				// set members
				List<TaskMemberEntity> members = new ArrayList<TaskMemberEntity>();
				TaskMemberEntity taskMemberEntity = new TaskMemberEntity();
				taskMemberEntity.setMemberCode((long) 2);
				taskMemberEntity.setCreatedAt(new Date());
				taskMemberEntity.setTask(taskEntity);
				members.add(taskMemberEntity);
				taskEntity.setMembers(members);

				// set categories
				List<TaskCategoryEntity> categoriesEntity = new ArrayList<TaskCategoryEntity>();
				TaskCategoryEntity category = taskCategoryService
						.getCategoryById(TaskCategoryBusiness.TASK_CATEGORY_INTEGRATION);
				categoriesEntity.add(category);
				taskEntity.setCategories(categoriesEntity);

				// set metadata

				List<TaskMetadataEntity> listMetadataEntity = new ArrayList<TaskMetadataEntity>();

				TaskMetadataEntity metadataHost = new TaskMetadataEntity();
				metadataHost.setKey("hostname");
				metadataHost.setValue("192.168.98.61");
				metadataHost.setTask(taskEntity);
				listMetadataEntity.add(metadataHost);

				TaskMetadataEntity metadataPort = new TaskMetadataEntity();
				metadataPort.setKey("port");
				metadataPort.setValue("5432");
				metadataPort.setTask(taskEntity);
				listMetadataEntity.add(metadataPort);

				TaskMetadataEntity metadataDatabase = new TaskMetadataEntity();
				metadataDatabase.setKey("database");
				metadataDatabase.setValue("integracion");
				metadataDatabase.setTask(taskEntity);
				listMetadataEntity.add(metadataDatabase);

				TaskMetadataEntity metadataUsername = new TaskMetadataEntity();
				metadataUsername.setKey("username");
				metadataUsername.setValue("postgres");
				metadataUsername.setTask(taskEntity);
				listMetadataEntity.add(metadataUsername);

				TaskMetadataEntity metadataPassword = new TaskMetadataEntity();
				metadataPassword.setKey("password");
				metadataPassword.setValue("123456");
				metadataPassword.setTask(taskEntity);
				listMetadataEntity.add(metadataPassword);

				taskEntity.setMetadata(listMetadataEntity);

				taskService.createTask(taskEntity);

				log.info("The domains have been loaded!");
			} catch (Exception e) {
				log.error("Failed to load domains");
			}

		}
	}

}
