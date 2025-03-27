package vn.dkc.jobhunter.service.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//Global exception handler
@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(IdInvalidException.class)
    public ResponseEntity<String> handleIdInvalidException(IdInvalidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
