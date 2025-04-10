package vn.dkc.jobhunter.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import vn.dkc.jobhunter.domain.RestResponse;

import java.io.IOException;
import java.util.Optional;

/**
 * Class xử lý các request không được xác thực (unauthorized) Implements AuthenticationEntryPoint để
 * tùy chỉnh response khi xác thực thất bại
 * 
 * @Component đánh dấu class này là một Spring bean
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    /**
     * Delegate xử lý authentication mặc định cho Bearer token
     */
    private final AuthenticationEntryPoint delegate = new BearerTokenAuthenticationEntryPoint();

    /**
     * ObjectMapper để chuyển đổi object thành JSON
     */
    private final ObjectMapper mapper;

    public CustomAuthenticationEntryPoint(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Xử lý request không được xác thực và trả về response dạng JSON
     * 
     * @param request HTTP request
     * @param response HTTP response
     * @param authException Exception chứa thông tin lỗi xác thực
     * @throws IOException nếu có lỗi khi ghi response
     * @throws ServletException nếu có lỗi servlet
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        this.delegate.commence(request, response, authException);
        response.setContentType("application/json;charset=UTF-8");

        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(HttpStatus.UNAUTHORIZED.value());
        String errorMessage = Optional.ofNullable(authException.getCause())
                .map(Throwable::getMessage).orElse(authException.getMessage());

        res.setMessage("Token expired...");
        res.setError(errorMessage);

        mapper.writeValue(response.getWriter(), res);
    }

}
