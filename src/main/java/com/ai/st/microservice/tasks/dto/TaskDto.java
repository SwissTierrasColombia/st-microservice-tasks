package com.ai.st.microservice.tasks.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "TaskDto", description = "Task")
public class TaskDto implements Serializable {

	private static final long serialVersionUID = -572797587427843121L;

	@ApiModelProperty(required = true, notes = "Task ID")
	private Long id;

	@ApiModelProperty(required = true, notes = "Name")
	private String name;

	@ApiModelProperty(required = false, notes = "Description")
	private String description;

	@ApiModelProperty(required = false, notes = "Deadline")
	private Date deadline;

	@ApiModelProperty(required = true, notes = "Date creation")
	private Date createdAt;

	@ApiModelProperty(required = false, notes = "Closing Date")
	private Date closingDate;

	@ApiModelProperty(required = true, notes = "Task state")
	private TaskStateDto taskState;

	@ApiModelProperty(required = true, notes = "Members")
	private List<TaskMemberDto> members;

	@ApiModelProperty(required = true, notes = "Categories")
	private List<TaskCategoryDto> categories;

	@ApiModelProperty(required = true, notes = "Metadata")
	private List<TaskMetadataDto> metadata;

	public TaskDto() {
		this.members = new ArrayList<TaskMemberDto>();
		this.categories = new ArrayList<TaskCategoryDto>();
		this.metadata = new ArrayList<TaskMetadataDto>();
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

	public TaskStateDto getTaskState() {
		return taskState;
	}

	public void setTaskState(TaskStateDto taskState) {
		this.taskState = taskState;
	}

	public List<TaskMemberDto> getMembers() {
		return members;
	}

	public void setMembers(List<TaskMemberDto> members) {
		this.members = members;
	}

	public List<TaskCategoryDto> getCategories() {
		return categories;
	}

	public void setCategories(List<TaskCategoryDto> categories) {
		this.categories = categories;
	}

	public List<TaskMetadataDto> getMetadata() {
		return metadata;
	}

	public void setMetadata(List<TaskMetadataDto> metadata) {
		this.metadata = metadata;
	}

}
