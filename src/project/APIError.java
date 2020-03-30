package project;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class APIError {
    private String error_message;

    public APIError(String error_message) {
        this.error_message = error_message;
    }

    public APIError() {
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

    public String toString(){
        return getError_message();
    }
}
