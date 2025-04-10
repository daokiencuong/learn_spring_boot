package vn.dkc.jobhunter.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import vn.dkc.jobhunter.util.SecurityUtil;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Class cấu hình bảo mật cho ứng dụng Quản lý xác thực, phân quyền và JWT (JSON Web Token)
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
     * Cấu hình chuỗi bộ lọc bảo mật
     * 
     * @param http đối tượng HttpSecurity để cấu hình bảo mật
     * @param customAuthenticationEntryPoint xử lý khi xác thực thất bại
     * @return SecurityFilterChain đã được cấu hình
     * @throws Exception nếu có lỗi trong quá trình cấu hình
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
            CustomAuthenticationEntryPoint customAuthenticationEntryPoint) throws Exception {
        http.csrf(c -> c.disable()).cors(Customizer.withDefaults())
                .authorizeHttpRequests(authz -> authz.requestMatchers("/", "/login").permitAll()
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
     * Cấu hình bộ chuyển đổi JWT để trích xuất thông tin xác thực
     * 
     * @return JwtAuthenticationConverter đã được cấu hình
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter =
                new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("");
        grantedAuthoritiesConverter.setAuthoritiesClaimName("daokiencuong");

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
