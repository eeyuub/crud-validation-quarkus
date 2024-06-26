package org.acme.validation;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public  class MessageResponse {
    private boolean success;
    private String message;
    private Object content;

    public MessageResponse(boolean success, String message, Object content) {
        this.success = success;
        this.message = message;
        this.content = content;
    }

    public MessageResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // Getters and setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getContent() {
        return content;
    }
}   