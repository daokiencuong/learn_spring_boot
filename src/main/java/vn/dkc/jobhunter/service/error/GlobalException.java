package vn.dkc.jobhunter.service.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import vn.dkc.jobhunter.domain.RestResponse;

//Global exception handler
@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(IdInvalidException.class)
    public ResponseEntity<Object> handleIdInvalidException(IdInvalidException e) {
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setMessage("Id is invalid");
        res.setError(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }
}
