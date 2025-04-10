package vn.dkc.jobhunter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * Class cấu hình CORS (Cross-Origin Resource Sharing) cho ứng dụng CORS cho phép các request từ
 * domain khác có thể truy cập API của ứng dụng
 */
@Configuration
public class CorsConfig {
    /**
     * Bean cấu hình chi tiết các quy tắc CORS
     * 
     * @return CorsConfigurationSource đối tượng chứa cấu hình CORS
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        // Tạo đối tượng cấu hình CORS mới
        CorsConfiguration configuration = new CorsConfiguration();
        // Cho phép request từ domain localhost:3000 (React frontend)
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        // Cho phép các phương thức HTTP
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Cho phép các header Authorization và Content-type
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-type"));
        // Cho phép gửi cookie trong request
        configuration.setAllowCredentials(true);
        // Thời gian cache preflight request (1 giờ)
        configuration.setMaxAge(3600L);


        // Tạo và cấu hình nguồn cấu hình CORS dựa trên URL
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Áp dụng cấu hình CORS cho tất cả các đường dẫn
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
