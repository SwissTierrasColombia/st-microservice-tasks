package com.ai.st.microservice.tasks;

import com.ai.st.microservice.tasks.business.TaskCategoryBusiness;
import com.ai.st.microservice.tasks.business.TaskStateBusiness;
import com.ai.st.microservice.tasks.business.TaskTypeStepBusiness;
import com.ai.st.microservice.tasks.entities.TaskCategoryEntity;
import com.ai.st.microservice.tasks.entities.TaskStateEntity;
import com.ai.st.microservice.tasks.entities.TaskTypeStepEntity;
import com.ai.st.microservice.tasks.services.ITaskCategoryService;
import com.ai.st.microservice.tasks.services.ITaskStateService;
import com.ai.st.microservice.tasks.services.ITaskTypeStepService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class StMicroserviceTaskApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger log = LoggerFactory.getLogger(StMicroserviceTaskApplicationStartup.class);

	@Autowired
	private ITaskStateService taskStateService;

	@Autowired
	private ITaskCategoryService taskCategoryService;

	@Autowired
	private ITaskTypeStepService typeStepService;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		log.info("ST - Loading Domains ... ");
		this.initTasksStates();
		this.initTypeSteps();
		this.initTasksCategories();
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
				categoryIntegration.setName("INTEGRACIÓN DE INSUMOS");
				taskCategoryService.createTaskCategory(categoryIntegration);

				TaskCategoryEntity categoryGeneration = new TaskCategoryEntity();
				categoryGeneration.setId(TaskCategoryBusiness.TASK_CATEGORY_CADASTRAL_INPUT_GENERATION);
				categoryGeneration.setName("GENERACIÓN INSUMO CATASTRAL");
				taskCategoryService.createTaskCategory(categoryGeneration);

				TaskCategoryEntity categoryXTFQuality = new TaskCategoryEntity();
				categoryXTFQuality.setId(TaskCategoryBusiness.TASK_CATEGORY_XTF_QUALITY_RULES);
				categoryXTFQuality.setName("CONTROL DE CALIDAD XTF");
				taskCategoryService.createTaskCategory(categoryXTFQuality);

				log.info("The domains 'categories' have been loaded!");
			} catch (Exception e) {
				System.out.println("ERROR: " + e.getMessage());
				log.error("Failed to load 'categories' domains");
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
