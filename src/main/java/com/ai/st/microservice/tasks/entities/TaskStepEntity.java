package com.ai.st.microservice.tasks.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tasks_steps", schema = "tasks")
public class TaskStepEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "description", nullable = false, length = 1000)
	private String description;

	@Column(name = "code", nullable = false, length = 20)
	private String code;

	@Column(name = "status", nullable = false)
	private Boolean status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "task_type_step_id", referencedColumnName = "id", nullable = false)
	private TaskTypeStepEntity typeStep;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "task_id", referencedColumnName = "id", nullable = false)
	private TaskEntity task;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "step_id", referencedColumnName = "id", nullable = true)
	private TaskStepEntity step;

	public TaskStepEntity() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public TaskTypeStepEntity getTypeStep() {
		return typeStep;
	}

	public void setTypeStep(TaskTypeStepEntity typeStep) {
		this.typeStep = typeStep;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TaskStepEntity getStep() {
		return step;
	}

	public void setStep(TaskStepEntity step) {
		this.step = step;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public TaskEntity getTask() {
		return task;
	}

	public void setTask(TaskEntity task) {
		this.task = task;
	}

}
