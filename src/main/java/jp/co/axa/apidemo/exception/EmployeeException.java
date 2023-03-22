package jp.co.axa.apidemo.exception;

import jp.co.axa.apidemo.constants.ApplicationConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


/**
 * Class is responsible for handling exceptions in the application
 */
@ControllerAdvice
public class EmployeeException {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeException.class);

    /**
     * Exception handler for not found
     *
     * @param exception
     * @return ResponseEntity
     */
    @ExceptionHandler(value = DataIssueException.class)
    public ResponseEntity<Object> exception(DataIssueException exception) {
        logger.error("Data is not found in the records : ");
        this.printExceptionTrace(exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Exception handler for generic exceptions
     *
     * @param exception
     * @param request
     * @return ResponseEntity
     */
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception exception, WebRequest request) {

        logger.error("Error while fetching the details");
        this.printExceptionTrace(exception);
        return new ResponseEntity<>(ApplicationConstants.SERVER_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    /**
     * Method prints the exception stack trace to the logger
     *
     * @param exception
     */
    private void printExceptionTrace(Exception exception) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        exception.printStackTrace(new PrintStream(os));
        logger.error(new String(os.toByteArray()));
    }

}