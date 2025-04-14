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
import vn.dkc.jobhunter.domain.response.RestResponse;

import java.io.IOException;
import java.util.Optional;

/**
 * Class xử lý các request không được xác thực (unauthorized)
 * 
 * Chức năng chính:
 * 1. Xử lý các request không có token hoặc token không hợp lệ
 * 2. Format response trả về theo chuẩn RestResponse
 * 3. Cung cấp thông tin chi tiết về lỗi xác thực
 * 
 * Luồng xử lý: 1. Khi một request không được xác thực, Spring Security sẽ gọi phương thức commence()
 * 2. Phương thức này sẽ: - Gọi xử lý mặc định của BearerTokenAuthenticationEntryPoint -
 * Format response về dạng JSON với thông tin lỗi - Trả về status code 401 UNAUTHORIZED
 * 
 * @Component đánh dấu class này là một Spring bean được quản lý bởi Spring Container
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    /**
     * Delegate xử lý authentication mặc định cho Bearer token Sử dụng
     * BearerTokenAuthenticationEntryPoint để xử lý các vấn đề cơ bản của Bearer token
     * authentication như thiếu token, token hết hạn, token không hợp lệ
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
     * Các bước xử lý: 1. Gọi xử lý mặc định của BearerTokenAuthenticationEntryPoint 2. Set content
     * type là application/json và charset UTF-8 3. Tạo đối tượng RestResponse chứa thông tin lỗi: -
     * statusCode: 401 UNAUTHORIZED - message: "Token expired..." - error: Chi tiết lỗi từ
     * AuthenticationException 4. Chuyển đổi RestResponse thành JSON và ghi vào response
     * 
     * @param request HTTP request chứa thông tin request gốc
     * @param response HTTP response để ghi kết quả
     * @param authException Exception chứa thông tin chi tiết về lỗi xác thực
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
