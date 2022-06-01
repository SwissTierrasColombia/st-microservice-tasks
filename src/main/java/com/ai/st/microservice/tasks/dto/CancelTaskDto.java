package com.ai.st.microservice.tasks.dto;

import java.io.Serializable;

public class CancelTaskDto implements Serializable {

    private static final long serialVersionUID = -8968447263161444063L;

    private String reason;

    public CancelTaskDto() {

    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "CancelTaskDto{" + "reason='" + reason + '\'' + '}';
    }
}
