package jp.co.axa.apidemo.exception;

/**
 * Custom Exception class for Input data mismatch
 *
 */
public class DataIssueException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DataIssueException(String message){
        super(message);
    }
}
