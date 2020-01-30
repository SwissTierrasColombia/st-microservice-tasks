package com.ai.st.microservice.tasks.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "tasks", schema = "tasks")
public class TaskEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "name", nullable = false, length = 255)
	private String name;

	@Column(name = "description", nullable = true, length = 255)
	private String description;

	@Column(name = "deadline", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date deadline;

	@Column(name = "created_at", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdAt;

	@Column(name = "closing_date", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date closingDate;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "task_state_id", referencedColumnName = "id", nullable = false)
	private TaskStateEntity taskState;

	@OneToMany(mappedBy = "task")
	private List<TaskMemberEntity> members = new ArrayList<TaskMemberEntity>();

	@OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
	private List<TaskMetadataEntity> metadata = new ArrayList<TaskMetadataEntity>();

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(schema = "tasks", name = "tasks_x_categories", joinColumns = @JoinColumn(name = "task_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
	List<TaskCategoryEntity> categories;

	@OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
	private List<TaskStepEntity> steps = new ArrayList<TaskStepEntity>();

	public TaskEntity() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getClosingDate() {
		return closingDate;
	}

	public void setClosingDate(Date closingDate) {
		this.closingDate = closingDate;
	}

	public TaskStateEntity getTaskState() {
		return taskState;
	}

	public void setTaskState(TaskStateEntity taskState) {
		this.taskState = taskState;
	}

	public List<TaskCategoryEntity> getCategories() {
		return categories;
	}

	public void setCategories(List<TaskCategoryEntity> categories) {
		this.categories = categories;
	}

	public List<TaskMemberEntity> getMembers() {
		return members;
	}

	public void setMembers(List<TaskMemberEntity> members) {
		this.members = members;
	}

	public List<TaskMetadataEntity> getMetadata() {
		return metadata;
	}

	public void setMetadata(List<TaskMetadataEntity> metadata) {
		this.metadata = metadata;
	}

	public List<TaskStepEntity> getSteps() {
		return steps;
	}

	public void setSteps(List<TaskStepEntity> steps) {
		this.steps = steps;
	}

}
