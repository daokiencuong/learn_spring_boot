package vn.dkc.jobhunter.domain.response;

/**
 * Class đại diện cho cấu trúc response chuẩn của API Sử dụng Generic type T để có thể trả về nhiều
 * loại dữ liệu khác nhau
 */
public class RestResponse<T> {
    /**
     * Mã trạng thái HTTP của response
     */
    private int statusCode;

    /**
     * Thông báo lỗi nếu có
     */
    private String error;

    /**
     * Thông điệp từ server Có thể là String hoặc ArrayList tùy theo context
     */
    private Object message;

    /**
     * Dữ liệu trả về, kiểu dữ liệu phụ thuộc vào type T
     */
    private T data;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
