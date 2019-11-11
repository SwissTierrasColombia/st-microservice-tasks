package com.ai.st.microservice.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.ai.st.entities.schema.tasks.TaskStateEntity;
import com.ai.st.microservice.tasks.business.TaskStateBusiness;
import com.ai.st.microservice.tasks.services.ITaskStateService;

@Component
public class StMicroserviceTaskApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger log = LoggerFactory.getLogger(StMicroserviceTaskApplicationStartup.class);

	@Autowired
	private ITaskStateService taskStateService;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		log.info("ST - Loading Domains ... ");
		this.initTasksStates();
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

}
