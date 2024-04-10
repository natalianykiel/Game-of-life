package exceptions;

import java.util.ResourceBundle;

public class CustomAppException extends Exception {
    private final String errorCode;
    private final ResourceBundle resourceBundle;

    public CustomAppException(String message, String errorCode, ResourceBundle resourceBundle) {
        super(message);
        this.errorCode = errorCode;
        this.resourceBundle = resourceBundle;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getLocalizedMessage() {
        return resourceBundle.getString(errorCode);
    }
}

