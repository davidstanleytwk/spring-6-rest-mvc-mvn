package guru.springframework.spring6restmvc.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


/*

Can be implemented as a global exception handler BUT simpler to use the @ResponseStatus(value = HttpStatus.NOT_FOUND,reason = "Value Not Found") annotation
on an Exception to manage specific exceptions, hence commented out the @ControllerAdvice annotation to remove it from exception handling

Added: @ResponseStatus(value = HttpStatus.NOT_FOUND,reason = "Value Not Found")
to the NotFoundException class to replace the global @ControllerAdvice
 */
//@ControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity handleNotFoundException()
    {
        log.debug("ExceptionController::HandleNotFoundException called");
        return ResponseEntity.notFound().build();
    }


}
