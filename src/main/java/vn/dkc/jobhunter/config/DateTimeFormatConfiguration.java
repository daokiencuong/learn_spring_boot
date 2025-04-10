package vn.dkc.jobhunter.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Class cấu hình định dạng ngày giờ cho toàn bộ ứng dụng Implements WebMvcConfigurer để tùy chỉnh
 * cấu hình MVC
 * 
 * @Configuration đánh dấu đây là class cấu hình Spring
 */
@Configuration
public class DateTimeFormatConfiguration implements WebMvcConfigurer {
    /**
     * Đăng ký formatter cho việc chuyển đổi ngày giờ Sử dụng định dạng ISO để đảm bảo tính nhất
     * quán
     * 
     * @param registry đối tượng chứa các formatter đã đăng ký
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();
        registrar.setUseIsoFormat(true);
        registrar.registerFormatters(registry);
    }
}
