package vn.dkc.jobhunter.util.error;

public class StorageException extends RuntimeException {
    /**
     * Constructor nhận message mô tả lỗi
     *
     * @param message Thông báo chi tiết về lỗi ID không hợp lệ
     */
    public StorageException(String message) {
        super(message);
    }
}
