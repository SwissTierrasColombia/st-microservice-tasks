package com.ai.st.microservice.tasks.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "TaskMetadataPropertyDto", description = "Task Metadata Property Dto")
public class TaskMetadataPropertyDto implements Serializable {

	private static final long serialVersionUID = 3708739121137799257L;

	@ApiModelProperty(required = true, notes = "Id")
	private Long id;

	@ApiModelProperty(required = true, notes = "Key")
	private String key;

	@ApiModelProperty(required = true, notes = "Value")
	private String value;

	public TaskMetadataPropertyDto() {

	}

	public TaskMetadataPropertyDto(Long id, String key, String value) {
		this.id = id;
		this.key = key;
		this.value = value;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
