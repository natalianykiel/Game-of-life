package exceptions;

import java.util.ResourceBundle;

public class GameBoardLoadingException extends Exception {
    public GameBoardLoadingException(String messageKey, ResourceBundle texts) {
        super(texts.getString(messageKey));
    }

    public GameBoardLoadingException(String messageKey, ResourceBundle texts, Throwable cause) {
        super(texts.getString(messageKey), cause);
    }
}
