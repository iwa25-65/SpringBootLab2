package pl.dmcs.rkotas.springbootlab2.message.reponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseMessage {

    @JsonProperty("message")
    private String message;

    @JsonProperty("data")
    private Object data;

    // Constructors
    public ResponseMessage() {} // Required for Jackson

    public ResponseMessage(String message) {
        this.message = message;
    }

    public ResponseMessage(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    // Getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}