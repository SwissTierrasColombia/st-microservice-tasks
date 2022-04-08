package com.ai.st.microservice.tasks.services.tracing;

public enum TracingKeyword {
    BODY_REQUEST("bodyRequest"), AUTHORIZATION_HEADER("authorizationHeader"), USER_ID("userId"), USER_NAME("username"),
    USER_EMAIL("userEmail"), MANAGER_ID("managerId"), MANAGER_NAME("managerName");

    private final String value;

    TracingKeyword(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
