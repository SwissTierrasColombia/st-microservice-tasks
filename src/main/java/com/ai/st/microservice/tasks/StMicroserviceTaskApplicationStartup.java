package com.ai.st.microservice.tasks;

import java.text.SimpleDateFormat;
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
import com.ai.st.microservice.tasks.business.TaskTypeStepBusiness;
import com.ai.st.microservice.tasks.entities.TaskCategoryEntity;
import com.ai.st.microservice.tasks.entities.TaskEntity;
import com.ai.st.microservice.tasks.entities.TaskMemberEntity;
import com.ai.st.microservice.tasks.entities.TaskMetadataEntity;
import com.ai.st.microservice.tasks.entities.TaskStateEntity;
import com.ai.st.microservice.tasks.entities.TaskStepEntity;
import com.ai.st.microservice.tasks.entities.TaskTypeStepEntity;
import com.ai.st.microservice.tasks.exceptions.InputValidationException;
import com.ai.st.microservice.tasks.services.ITaskCategoryService;
import com.ai.st.microservice.tasks.services.ITaskService;
import com.ai.st.microservice.tasks.services.ITaskStateService;
import com.ai.st.microservice.tasks.services.ITaskTypeStepService;

@Component
public class StMicroserviceTaskApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger log = LoggerFactory.getLogger(StMicroserviceTaskApplicationStartup.class);

	@Autowired
	private ITaskStateService taskStateService;

	@Autowired
	private ITaskCategoryService taskCategoryService;

	@Autowired
	private ITaskService taskService;

	@Autowired
	private ITaskTypeStepService typeStepService;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		log.info("ST - Loading Domains ... ");
		this.initTasksStates();
		this.initTypeSteps();
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

				TaskStateEntity taskStateStarted = new TaskStateEntity();
				taskStateStarted.setId(TaskStateBusiness.TASK_STATE_STARTED);
				taskStateStarted.setName("INICIADA");
				taskStateService.createTaskState(taskStateStarted);

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

				log.info("The domains 'categories' have been loaded!");
			} catch (Exception e) {
				System.out.println("ERROR: " + e.getMessage());
				log.error("Failed to load 'categories' domains");
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

				// set steps

				TaskTypeStepEntity typeStepOnce = typeStepService
						.getTypeStepById(TaskTypeStepBusiness.TASK_TYPE_STEP_ONCE);

				List<TaskStepEntity> steps = new ArrayList<TaskStepEntity>();

				TaskStepEntity step1 = new TaskStepEntity();
				step1.setTask(taskEntity);
				step1.setCode("001");
				step1.setDescription("Crear esquema BD");
				step1.setStatus(false);
				step1.setTypeStep(typeStepOnce);
				steps.add(step1);

				TaskStepEntity step2 = new TaskStepEntity();
				step2.setTask(taskEntity);
				step2.setCode("002");
				step2.setDescription("Correr ETL insumos");
				step2.setStatus(false);
				step2.setTypeStep(typeStepOnce);
				steps.add(step2);

				TaskStepEntity step3 = new TaskStepEntity();
				step3.setTask(taskEntity);
				step3.setCode("003");
				step3.setDescription("Generar XTF");
				step3.setStatus(false);
				step3.setTypeStep(typeStepOnce);
				steps.add(step3);

				TaskStepEntity step4 = new TaskStepEntity();
				step4.setTask(taskEntity);
				step4.setCode("004");
				step4.setDescription("Subir XTF");
				step4.setStatus(false);
				step4.setTypeStep(typeStepOnce);
				steps.add(step4);

				taskEntity.setSteps(steps);

				taskService.createTask(taskEntity);

				// tasks 2

				Date taskDeadline = null;
				String taskDeadlineString = "2019-12-24 23:59:00";
				try {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					taskDeadline = sdf.parse(taskDeadlineString);
				} catch (Exception e) {
					throw new InputValidationException("La fecha límite es inválida.");
				}

				TaskEntity task2Entity = new TaskEntity();
				task2Entity.setName("Generar XTF Cobol");
				task2Entity.setDescription("Generar insumos en XTF para el municipio ovejas.");
				task2Entity.setDeadline(taskDeadline);
				task2Entity.setCreatedAt(new Date());
				task2Entity.setTaskState(taskStateEntity);

				List<TaskMemberEntity> membersTask2 = new ArrayList<TaskMemberEntity>();

				TaskMemberEntity taskMemberEntity1 = new TaskMemberEntity();
				taskMemberEntity1.setMemberCode((long) 2);
				taskMemberEntity1.setCreatedAt(new Date());
				taskMemberEntity1.setTask(task2Entity);
				membersTask2.add(taskMemberEntity1);

				TaskMemberEntity taskMemberEntity2 = new TaskMemberEntity();
				taskMemberEntity2.setMemberCode((long) 3);
				taskMemberEntity2.setCreatedAt(new Date());
				taskMemberEntity2.setTask(task2Entity);
				membersTask2.add(taskMemberEntity2);

				task2Entity.setMembers(membersTask2);
				task2Entity.setCategories(categoriesEntity);

				List<TaskMetadataEntity> listMetadataEntity2 = new ArrayList<TaskMetadataEntity>();

				TaskMetadataEntity metadataHost2 = new TaskMetadataEntity();
				metadataHost2.setKey("hostname");
				metadataHost2.setValue("192.168.98.61");
				metadataHost2.setTask(task2Entity);
				listMetadataEntity2.add(metadataHost2);

				TaskMetadataEntity metadataPort2 = new TaskMetadataEntity();
				metadataPort2.setKey("port");
				metadataPort2.setValue("5432");
				metadataPort2.setTask(task2Entity);
				listMetadataEntity2.add(metadataPort2);

				TaskMetadataEntity metadataDatabase2 = new TaskMetadataEntity();
				metadataDatabase2.setKey("database");
				metadataDatabase2.setValue("integracion");
				metadataDatabase2.setTask(task2Entity);
				listMetadataEntity2.add(metadataDatabase2);

				TaskMetadataEntity metadataUsername2 = new TaskMetadataEntity();
				metadataUsername2.setKey("username");
				metadataUsername2.setValue("postgres");
				metadataUsername2.setTask(task2Entity);
				listMetadataEntity2.add(metadataUsername2);

				TaskMetadataEntity metadataPassword2 = new TaskMetadataEntity();
				metadataPassword2.setKey("password");
				metadataPassword2.setValue("123456");
				metadataPassword2.setTask(task2Entity);
				listMetadataEntity2.add(metadataPassword2);

				task2Entity.setMetadata(listMetadataEntity2);
				
				List<TaskStepEntity> steps2 = new ArrayList<TaskStepEntity>();

				TaskStepEntity step5 = new TaskStepEntity();
				step5.setTask(task2Entity);
				step5.setCode("001");
				step5.setDescription("Crear esquema BD");
				step5.setStatus(false);
				step5.setTypeStep(typeStepOnce);
				steps2.add(step5);

				TaskStepEntity step6 = new TaskStepEntity();
				step6.setTask(task2Entity);
				step6.setCode("002");
				step6.setDescription("Correr ETL insumos");
				step6.setStatus(false);
				step6.setTypeStep(typeStepOnce);
				steps2.add(step6);

				TaskStepEntity step7 = new TaskStepEntity();
				step7.setTask(task2Entity);
				step7.setCode("003");
				step7.setDescription("Generar XTF");
				step7.setStatus(false);
				step7.setTypeStep(typeStepOnce);
				steps2.add(step7);

				TaskStepEntity step8 = new TaskStepEntity();
				step8.setTask(task2Entity);
				step8.setCode("004");
				step8.setDescription("Subir XTF");
				step8.setStatus(false);
				step8.setTypeStep(typeStepOnce);
				steps2.add(step8);

				task2Entity.setSteps(steps2);

				taskService.createTask(task2Entity);

				log.info("The domains have been loaded!");
			} catch (Exception e) {
				System.out.println("ERROR: " + e.getMessage());
				log.error("Failed to load domains " + e.getMessage());
			}

		}
	}

	public void initTypeSteps() {

		Long countTypes = typeStepService.getCount();
		if (countTypes == 0) {

			try {

				TaskTypeStepEntity step1 = new TaskTypeStepEntity();
				step1.setId(TaskTypeStepBusiness.TASK_TYPE_STEP_ONCE);
				step1.setName("ÚNICA VEZ");
				typeStepService.createTypeStep(step1);

				TaskTypeStepEntity step2 = new TaskTypeStepEntity();
				step2.setId(TaskTypeStepBusiness.TASK_TYPE_STEP_ALWAYS);
				step2.setName("SIEMPRE");
				typeStepService.createTypeStep(step2);

				log.info("The domains 'types steps' have been loaded!");
			} catch (Exception e) {
				log.error("Failed to load 'types steps' domains");
			}

		}

	}

}
