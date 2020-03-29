package project;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class APIError {
    private String error_message;

//    private String username_not_available = "The user is not available.";
//
//    private String email_invalid = "Email is invalid.";
//
//    private String email_not_available = "Email not available.";
//
//    private String login_incorrect_info = "Username/Email or password is incorrect.";


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
}
