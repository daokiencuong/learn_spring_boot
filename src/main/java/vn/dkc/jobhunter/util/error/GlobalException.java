package vn.dkc.jobhunter.util.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import vn.dkc.jobhunter.domain.response.RestResponse;

import java.util.stream.Collectors;

/**
 * Class xử lý exception toàn cục cho ứng dụng Định dạng các lỗi thành response chuẩn RestResponse
 * 
 * @RestControllerAdvice đánh dấu class này xử lý exception cho tất cả các controller
 */
@RestControllerAdvice
public class GlobalException {
    //Handle all exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestResponse<Object>> handleException(Exception e) {
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        res.setMessage(e.getMessage());
        res.setError("Internal server error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
    }


    /**
     * Xử lý các exception liên quan đến xác thực Bao gồm lỗi không tìm thấy username và thông tin
     * đăng nhập không hợp lệ
     * 
     * @param e Exception cần xử lý
     * @return ResponseEntity chứa thông tin lỗi đã được định dạng
     */
    @ExceptionHandler(value = {UsernameNotFoundException.class,
            BadCredentialsException.class,
            EmailExsitsException.class,
            IdInvalidException.class,
            SkillExsitsException.class,
            ObjectExistsException.class,
            StorageException.class,
            ResumeException.class,
            RoleException.class,
            PermissionException.class,
            IdInvalidException.class,
            SubscriberException.class})
    public ResponseEntity<RestResponse<Object>> handleIdInvalidException(Exception e) {
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setMessage("Exception occured...");
        res.setError(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    /**
     * Xử lý các lỗi validation của request body Tổng hợp các lỗi validation thành một thông báo duy
     * nhất
     * 
     * @param e Exception chứa các lỗi validation
     * @return ResponseEntity chứa thông tin các lỗi validation
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage).collect(Collectors.joining("; "));

        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setMessage(message);

        res.setError("Invalid request content");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Object> handleNoResourceFoundException(
            NoResourceFoundException e) {
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.NOT_FOUND.value());
        res.setMessage("404 NOT FOUND. Resource not found");
        res.setError(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
    }
}
