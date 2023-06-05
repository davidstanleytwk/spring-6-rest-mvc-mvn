package guru.springframework.spring6restmvc.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomErrorController {

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity handleTransactionSystemException(TransactionSystemException e)
    {

        ResponseEntity.BodyBuilder responseEntity=ResponseEntity.badRequest();

        if( e.getCause().getCause() instanceof ConstraintViolationException)
        {
            ConstraintViolationException cve = (ConstraintViolationException) e.getCause().getCause();
            Map<String, String> constraintViolationsMap=
            cve.getConstraintViolations()
                    .stream()
                    .collect(Collectors
                            .toMap(
                                    //ConstraintViolation::getPropertyPath::toString,
                                    cv->{return cv.getPropertyPath().toString();},
                                    ConstraintViolation::getMessage,(existingValue, newValue)->{
                                        return existingValue+"; "+newValue;
                                    }));
            return responseEntity.body(constraintViolationsMap);
        }

        return responseEntity.build();
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity handleMethodArgumentValidException(MethodArgumentNotValidException e)
    {

        // naff way to get a Map of values
        //        List errorList = e.getFieldErrors().stream().map(fieldError -> {
        //            Map<String, String> errorMap = new HashMap<>();
        //            errorMap.put(fieldError.getField(),fieldError.getDefaultMessage());
        //            return errorMap;
        //
        //        }).toList();

        // error because multiple keys of the same value
        Map<String, String> errorMap = e.getFieldErrors()
                .stream()
                .collect(
                        Collectors
                                .toMap( FieldError::getField,
                                        FieldError::getDefaultMessage,
                                        (eValue,nValue)->{return eValue+"; "+nValue; }));

        // Way too much data returned to client using below...
        //return ResponseEntity.badRequest().body(e.getBindingResult().getFieldError());

        // so use the tidier errorList populated above...
        return ResponseEntity.badRequest().body(errorMap);
    }
}
