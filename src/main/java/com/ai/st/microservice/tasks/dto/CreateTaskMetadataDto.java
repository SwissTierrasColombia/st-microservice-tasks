package com.ai.st.microservice.tasks.dto;

import java.io.Serializable;

public class CreateTaskMetadataDto implements Serializable {

	private static final long serialVersionUID = 3915775195611030554L;

	private String key;
	private String value;

	public CreateTaskMetadataDto() {

	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
