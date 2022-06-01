package com.ai.st.microservice.tasks.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CreateTaskMetadataDto implements Serializable {

    private static final long serialVersionUID = 3915775195611030554L;

    private String key;
    private List<CreateTaskPropertyDto> properties;

    public CreateTaskMetadataDto() {
        this.properties = new ArrayList<>();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<CreateTaskPropertyDto> getProperties() {
        return properties;
    }

    public void setProperties(List<CreateTaskPropertyDto> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "CreateTaskMetadataDto{" + "key='" + key + '\'' + ", properties=" + properties + '}';
    }
}
