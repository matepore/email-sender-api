package matepore.email.sender.exception;

//Custom exception for email sending errors
public class EmailSendException extends RuntimeException {

    public EmailSendException(String message, Throwable cause) {
        super(message, cause);
    }
}
