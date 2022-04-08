package com.ai.st.microservice.tasks.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "TaskCategoryDto", description = "Task Category Dto")
public class TaskCategoryDto implements Serializable {

    private static final long serialVersionUID = 7237234886460619822L;

    @ApiModelProperty(required = true, notes = "Category ID")
    private Long id;

    @ApiModelProperty(required = true, notes = "Name")
    private String name;

    public TaskCategoryDto() {

    }

    public TaskCategoryDto(Long id, String name) {
        super();
        this.id = id;
        this.name = name;
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

}
