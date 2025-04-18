package vn.dkc.jobhunter.util;

import com.nimbusds.jose.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import vn.dkc.jobhunter.domain.response.ResLoginDTO;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Class cung cấp các tiện ích liên quan đến bảo mật và xác thực Xử lý việc tạo và quản lý JWT
 * token, kiểm tra quyền người dùng
 * 
 * @Service đánh dấu đây là một service bean của Spring
 */
@Service
public class SecurityUtil {

    /**
     * Encoder để mã hóa JWT token
     */
    private final JwtEncoder jwtEncoder;

    /**
     * Thuật toán mã hóa cho JWT, sử dụng HMAC-SHA512
     */
    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;

    public SecurityUtil(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    /**
     * Khóa bí mật để ký JWT, được cấu hình trong application.properties
     */
    @Value("${dkc.jwt.base64-secret}")
    private String jwtKey;

    /**
     * Thời gian hiệu lực của JWT token (tính bằng giây)
     */
    @Value("${dkc.jwt.access-token-validity-in-seconds}")
    private long accessTokenExpiration;

    /**
     * Tạo JWT token từ thông tin xác thực
     * 
     * @param authentication đối tượng chứa thông tin xác thực của người dùng
     * @return JWT token dạng chuỗi
     */
    public String createAccessToken(String email, ResLoginDTO dto) {
        ResLoginDTO.UserInsideToken userInsideToken = new ResLoginDTO.UserInsideToken();
        userInsideToken.setId(dto.getUser().getId());
        userInsideToken.setEmail(dto.getUser().getEmail());
        userInsideToken.setName(dto.getUser().getName());

        Instant now = Instant.now();
        Instant validity = now.plus(this.accessTokenExpiration, ChronoUnit.SECONDS);

        List<String> listAuthority = new ArrayList<String>();

        listAuthority.add("ROLE_USER_CREATE");
        listAuthority.add("ROLE_USER_UPDATE");

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(email)
                .claim("user", userInsideToken)
                .claim("permission", listAuthority)
                .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    @Value("${dkc.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;
    public String createRefreshToken(String email, ResLoginDTO dto) {
        ResLoginDTO.UserInsideToken userInsideToken = new ResLoginDTO.UserInsideToken();
        userInsideToken.setId(dto.getUser().getId());
        userInsideToken.setEmail(dto.getUser().getEmail());
        userInsideToken.setName(dto.getUser().getName());

        Instant now = Instant.now();
        Instant validity = now.plus(this.refreshTokenExpiration, ChronoUnit.SECONDS);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(email)
                .claim("user", userInsideToken)
                .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    /**
     * Lấy tên đăng nhập của người dùng hiện tại
     * 
     * @return Optional chứa tên đăng nhập hoặc empty nếu chưa đăng nhập
     */
    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
    }

    /**
     * Trích xuất thông tin người dùng từ đối tượng Authentication
     * 
     * @param authentication đối tượng chứa thông tin xác thực
     * @return tên đăng nhập của người dùng hoặc null
     */
    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails springSecurityUser) {
            return springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getSubject();
        } else if (authentication.getPrincipal() instanceof String s) {
            return s;
        }
        return null;
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(jwtKey).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length,
                SecurityUtil.JWT_ALGORITHM.getName());
    }

    public Jwt checkValidRefreshToken(String token){
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(getSecretKey())
                .macAlgorithm(SecurityUtil.JWT_ALGORITHM).build();
        try {
            return jwtDecoder.decode(token);
        } catch (Exception e) {
            System.out.println(">>> JWT ERROR: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Check if a user is authenticated.
     *
     * @return true if the user is authenticated, false otherwise.
     */
    // public static boolean isAuthenticated() {
    // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    // return authentication != null &&
    // getAuthorities(authentication).noneMatch(AuthoritiesConstants.ANONYMOUS::equals);
    // }

    /**
     * Checks if the current user has any of the authorities.
     *
     * @param authorities the authorities to check.
     * @return true if the current user has any of the authorities, false otherwise.
     */
    // public static boolean hasCurrentUserAnyOfAuthorities(String... authorities) {
    // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    // return (
    // authentication != null && getAuthorities(authentication).anyMatch(authority ->
    // Arrays.asList(authorities).contains(authority))
    // );
    // }

    /**
     * Checks if the current user has none of the authorities.
     *
     * @param authorities the authorities to check.
     * @return true if the current user has none of the authorities, false otherwise.
     */
    // public static boolean hasCurrentUserNoneOfAuthorities(String... authorities) {
    // return !hasCurrentUserAnyOfAuthorities(authorities);
    // }

    /**
     * Checks if the current user has a specific authority.
     *
     * @param authority the authority to check.
     * @return true if the current user has the authority, false otherwise.
     */
    // public static boolean hasCurrentUserThisAuthority(String authority) {
    // return hasCurrentUserAnyOfAuthorities(authority);
    // }

    // private static Stream<String> getAuthorities(Authentication authentication) {
    // Collection<? extends GrantedAuthority> authorities = authentication instanceof
    // JwtAuthenticationToken
    // ? extractAuthorityFromClaims(((JwtAuthenticationToken)
    // authentication).getToken().getClaims())
    // : authentication.getAuthorities();
    // return authorities.stream().map(GrantedAuthority::getAuthority);
    // }
    //
    // public static List<GrantedAuthority> extractAuthorityFromClaims(Map<String, Object> claims) {
    // return mapRolesToGrantedAuthorities(getRolesFromClaims(claims));
    // }
    //
    // @SuppressWarnings("unchecked")
    // private static Collection<String> getRolesFromClaims(Map<String, Object> claims) {
    // return (Collection<String>) claims.getOrDefault(
    // "groups",
    // claims.getOrDefault("roles", claims.getOrDefault(CLAIMS_NAMESPACE + "roles", new
    // ArrayList<>()))
    // );
    // }
    //
    // private static List<GrantedAuthority> mapRolesToGrantedAuthorities(Collection<String> roles)
    // {
    // return roles.stream().filter(role ->
    // role.startsWith("ROLE_")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    // }

}
