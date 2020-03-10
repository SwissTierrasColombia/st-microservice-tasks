package com.ai.st.microservice.tasks.dto;

import java.io.Serializable;

public class CreateTaskStepDto implements Serializable {

	private static final long serialVersionUID = 2220658679258020082L;

	private String title;
	private String description;
	private Long typeStepId;

	public CreateTaskStepDto() {

	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getTypeStepId() {
		return typeStepId;
	}

	public void setTypeStepId(Long typeStepId) {
		this.typeStepId = typeStepId;
	}

}
