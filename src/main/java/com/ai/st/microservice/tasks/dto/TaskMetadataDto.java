package com.ai.st.microservice.tasks.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "TaskMedataDto", description = "Task Metadata")
public class TaskMetadataDto implements Serializable {

    private static final long serialVersionUID = 5451686139173510244L;

    @ApiModelProperty(required = true, notes = "Id")
    private Long id;

    @ApiModelProperty(required = true, notes = "Key")
    private String key;

    @ApiModelProperty(required = true, notes = "Properties")
    private List<TaskMetadataPropertyDto> properties;

    public TaskMetadataDto() {
        this.properties = new ArrayList<>();
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

    public List<TaskMetadataPropertyDto> getProperties() {
        return properties;
    }

    public void setProperties(List<TaskMetadataPropertyDto> properties) {
        this.properties = properties;
    }

}
