package vn.ngotien.jobhunter.service.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(value = IdInvalidException.class)
    public ResponseEntity<String> idInvalidException(IdInvalidException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
