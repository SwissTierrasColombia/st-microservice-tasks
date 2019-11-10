package com.ai.st.microservice.tasks.swagger.models;

import java.io.Serializable;

public class CreateTaskModel implements Serializable {

	private static final long serialVersionUID = 8405437490199078160L;

	private String name;
	private String description;
	private String deadline;
	private Long userId;

	public CreateTaskModel() {

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

	public String getDeadline() {
		return deadline;
	}

	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
