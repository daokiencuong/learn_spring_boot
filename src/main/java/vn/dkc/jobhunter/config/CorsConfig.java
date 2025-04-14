package vn.dkc.jobhunter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Class cấu hình CORS (Cross-Origin Resource Sharing) cho ứng dụng
 * 
 * CORS là một cơ chế bảo mật của trình duyệt web, cho phép hoặc hạn chế các tài nguyên được yêu cầu
 * từ một domain khác với domain chứa tài nguyên. Điều này rất quan trọng trong kiến trúc
 * microservices và ứng dụng web hiện đại, nơi frontend và backend thường chạy trên các domain khác
 * nhau.
 * 
 * Các chức năng chính: 1. Kiểm soát quyền truy cập từ các domain khác 2. Cấu hình các phương thức
 * HTTP được phép 3. Quản lý các header được phép trong request 4. Xử lý preflight request (OPTIONS)
 * 5. Cấu hình việc gửi credentials (cookies, auth headers)
 */
@Configuration
public class CorsConfig {
    /**
     * Bean cấu hình chi tiết các quy tắc CORS
     * 
     * Cấu hình bao gồm: 1. Allowed Origins: Các domain được phép gọi API - http://localhost:3000
     * (React dev server) - http://localhost:4173, :5173 (Vite dev/preview server) 2. Allowed
     * Methods: Các phương thức HTTP được phép (GET, POST, PUT, DELETE, OPTIONS) 3. Allowed Headers:
     * Các header được phép trong request - Authorization: Cho phép gửi JWT token - Content-type:
     * Cho phép gửi các loại dữ liệu khác nhau - Accept: Cho phép client chỉ định kiểu dữ liệu mong
     * muốn 4. Allow Credentials: Cho phép gửi cookie và header xác thực 5. Max Age: Thời gian cache
     * kết quả preflight request (1 giờ)
     * 
     * @return CorsConfigurationSource đối tượng chứa cấu hình CORS đầy đủ
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        // Tạo đối tượng cấu hình CORS mới
        CorsConfiguration configuration = new CorsConfiguration();
        // Cho phép request từ domain localhost:3000 (React frontend)
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000",
                "http://localhost:4173", "http://localhost:5173"));
        // Cho phép các phương thức HTTP
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Cho phép các header Authorization và Content-type
        configuration.setAllowedHeaders(
                Arrays.asList("Authorization", "Content-type", "x-no-retry", "Accept"));
        // Cho phép gửi cookie trong request
        configuration.setAllowCredentials(true);
        // Thời gian cache preflight request (1 giờ)
        configuration.setMaxAge(86400L);


        // Tạo và cấu hình nguồn cấu hình CORS dựa trên URL
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Áp dụng cấu hình CORS cho tất cả các đường dẫn
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
