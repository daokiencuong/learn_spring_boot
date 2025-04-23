package vn.dkc.jobhunter.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import vn.dkc.jobhunter.util.SecurityUtil;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Class cấu hình bảo mật cho ứng dụng, quản lý xác thực, phân quyền và JWT (JSON Web Token)
 * 
 * Các chức năng chính:
 * 1. Cấu hình các endpoint được phép truy cập không cần xác thực (/api/v1/auth/login, /api/v1/auth/refresh)
 * 2. Cấu hình JWT token (tạo token, xác thực token, trích xuất thông tin từ token)
 * 3. Cấu hình CORS (Cross-Origin Resource Sharing)
 * 4. Cấu hình session (STATELESS - không lưu session)
 * 5. Cấu hình password encoder (BCrypt)
 * 
 * @Configuration đánh dấu đây là class cấu hình Spring
 * @EnableMethodSecurity kích hoạt bảo mật trên method level với annotation @Secured
 */
@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

    /**
     * Cấu hình encoder để mã hóa mật khẩu
     * 
     * @return BCryptPasswordEncoder để mã hóa mật khẩu sử dụng thuật toán BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Cấu hình chuỗi bộ lọc bảo mật (Security Filter Chain)
     * 
     * Các cấu hình chính:
     * 1. Vô hiệu hóa CSRF vì sử dụng JWT
     * 2. Cho phép CORS với cấu hình mặc định
     * 3. Cấu hình các endpoint:
     *    - Cho phép truy cập tự do: /, /api/v1/auth/login, /api/v1/auth/refresh
     *    - Các endpoint khác yêu cầu xác thực
     * 4. Cấu hình xác thực JWT:
     *    - Sử dụng JWT làm resource server
     *    - Xử lý lỗi xác thực với CustomAuthenticationEntryPoint
     * 5. Vô hiệu hóa form login mặc định
     * 6. Sử dụng session STATELESS (không lưu trạng thái)
     * 
     * @param http đối tượng HttpSecurity để cấu hình bảo mật
     * @param customAuthenticationEntryPoint xử lý khi xác thực thất bại
     * @return SecurityFilterChain đã được cấu hình
     * @throws Exception nếu có lỗi trong quá trình cấu hình
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
            CustomAuthenticationEntryPoint customAuthenticationEntryPoint) throws Exception {

        String[] whiteList = {
                "/",
                "/api/v1/auth/login",
                "/api/v1/auth/refresh",
                "/storage/**",
                "/api/v1/auth/register",
                "/api/v1/email/**",
        };

        http.csrf(c -> c.disable()).cors(Customizer.withDefaults())
                .authorizeHttpRequests(authz ->
                                authz
                                        .requestMatchers(whiteList).permitAll()
                                        .requestMatchers(HttpMethod.GET, "/api/v1/companies").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/api/v1/jobs").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/api/v1/skills").permitAll()
                        .anyRequest().authenticated()

                // .anyRequest().permitAll()
                )
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults())
                        .authenticationEntryPoint(customAuthenticationEntryPoint))
                // .exceptionHandling(
                // exceptions -> exceptions
                // .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint()) //401
                // .accessDeniedHandler(new BearerTokenAccessDeniedHandler()) //403
                // )
                .formLogin(f -> f.disable()) // Disable form login default
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    /**
     * Khóa bí mật để ký và xác thực JWT, được cấu hình trong application.properties
     */
    @Value("${dkc.jwt.base64-secret}")
    private String jwtKey;

    /**
     * Tạo khóa bí mật từ chuỗi base64 để sử dụng trong JWT
     * 
     * @return SecretKey đối tượng khóa bí mật
     */
    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(jwtKey).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length,
                SecurityUtil.JWT_ALGORITHM.getName());
    }

    /**
     * Cấu hình bộ chuyển đổi JWT để trích xuất thông tin xác thực từ token
     * 
     * Chức năng:
     * 1. Cấu hình cách trích xuất quyền (authorities) từ JWT claims
     * 2. Xóa prefix của authority (mặc định là "ROLE_")
     * 3. Đặt tên claim chứa thông tin quyền là "permission"
     * 
     * @return JwtAuthenticationConverter đã được cấu hình để xử lý JWT
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter =
                new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("");
        grantedAuthoritiesConverter.setAuthoritiesClaimName("permission");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    /**
     * Cấu hình bộ mã hóa JWT để tạo token
     * 
     * @return JwtEncoder sử dụng khóa bí mật đã cấu hình
     */
    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(getSecretKey()));
    }

    /**
     * Cấu hình bộ giải mã JWT để xác thực token
     * 
     * @return JwtDecoder với xử lý lỗi tùy chỉnh
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(getSecretKey())
                .macAlgorithm(SecurityUtil.JWT_ALGORITHM).build();
        return token -> {
            try {
                return jwtDecoder.decode(token);
            } catch (Exception e) {
                System.out.println(">>> JWT ERROR: " + e.getMessage());
                throw e;
            }
        };
    }
}
