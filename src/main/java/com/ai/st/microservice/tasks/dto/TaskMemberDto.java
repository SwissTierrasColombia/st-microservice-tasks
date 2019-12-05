package com.ai.st.microservice.tasks.dto;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "TaskMemberDto", description = "Task Member")
public class TaskMemberDto implements Serializable {

	private static final long serialVersionUID = 7581006574042957605L;

	@ApiModelProperty(required = true, notes = "Task member ID")
	private Long id;

	@ApiModelProperty(required = true, notes = "Member code")
	private Long memberCode;

	@ApiModelProperty(required = true, notes = "Date creation")
	private Date createdAt;

	public TaskMemberDto() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getMemberCode() {
		return memberCode;
	}

	public void setMemberCode(Long memberCode) {
		this.memberCode = memberCode;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

}
