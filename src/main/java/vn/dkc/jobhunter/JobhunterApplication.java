package vn.dkc.jobhunter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Đây là class chính của ứng dụng JobHunter, sử dụng Spring Boot Framework
 * 
 * @SpringBootApplication là annotation đánh dấu đây là ứng dụng Spring Boot, tự động kích hoạt: -
 *                        Cấu hình tự động (Auto-configuration) - Quét component (Component
 *                        scanning) - Các tính năng bổ sung của Spring Boot
 */
@SpringBootApplication
@EnableAsync
@EnableScheduling
public class JobhunterApplication {
	/**
	 * Phương thức main để khởi động ứng dụng Spring Boot
	 * 
	 * @param args tham số dòng lệnh khi chạy ứng dụng
	 */
	public static void main(String[] args) {
		SpringApplication.run(JobhunterApplication.class, args);
	}
}
