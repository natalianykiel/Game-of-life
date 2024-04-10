package exceptions;

import java.util.ResourceBundle;

public class InvalidBoardSizeException extends CustomAppException {
    public InvalidBoardSizeException(String errorCode, ResourceBundle resourceBundle) {
        super("Invalid board size", errorCode, resourceBundle);
    }
}

