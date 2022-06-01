package com.ai.st.microservice.tasks.dto;

import java.io.Serializable;

public class CreateTaskPropertyDto implements Serializable {

    private static final long serialVersionUID = -4791673243561643847L;

    private String key;
    private String value;

    public CreateTaskPropertyDto() {

    }

    public CreateTaskPropertyDto(String key, String value) {
        this.key = key;
        this.value = value;
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

    @Override
    public String toString() {
        return "CreateTaskPropertyDto{" + "key='" + key + '\'' + ", value='" + value + '\'' + '}';
    }
}
