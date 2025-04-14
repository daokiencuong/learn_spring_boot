package vn.dkc.jobhunter.util.error;

/**
 * Exception được ném ra khi ID không hợp lệ hoặc không tồn tại trong hệ thống
 * 
 * Được sử dụng trong các trường hợp: 1. Truy vấn thông tin với ID không tồn tại 2. Cập nhật/xóa dữ
 * liệu với ID không hợp lệ 3. Tham chiếu đến entity với ID không tồn tại
 * 
 * Kế thừa từ RuntimeException để không bắt buộc phải khai báo throws trong method signature
 */
public class IdInvalidException extends RuntimeException {
    /**
     * Constructor nhận message mô tả lỗi
     * 
     * @param message Thông báo chi tiết về lỗi ID không hợp lệ
     */
    public IdInvalidException(String message) {
        super(message);
    }
}
