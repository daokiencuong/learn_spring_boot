package vn.dkc.jobhunter.util.error;

/**
 * Exception được ném ra khi phát hiện email đã tồn tại trong hệ thống
 * 
 * Được sử dụng trong các trường hợp: 1. Đăng ký tài khoản mới với email đã tồn tại 2. Cập nhật
 * thông tin user với email đã được sử dụng bởi user khác
 * 
 * Kế thừa từ RuntimeException để không bắt buộc phải khai báo throws trong method signature
 */
public class EmailExsitsException extends RuntimeException {
    /**
     * Constructor nhận message mô tả lỗi
     * 
     * @param message Thông báo chi tiết về lỗi email đã tồn tại
     */
    public EmailExsitsException(String message) {
        super(message);
    }
}
