package vn.dkc.jobhunter.util;

import org.springframework.core.MethodParameter;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import jakarta.servlet.http.HttpServletResponse;
import vn.dkc.jobhunter.domain.response.RestResponse;
import vn.dkc.jobhunter.util.annotation.ApiMessage;

/**
 * Class định dạng response cho tất cả các API trong ứng dụng Implements ResponseBodyAdvice để can
 * thiệp vào quá trình tạo response
 * 
 * @ControllerAdvice đánh dấu class này sẽ áp dụng cho tất cả các controller
 */
@ControllerAdvice
public class FormatRestResponse implements ResponseBodyAdvice<Object> {

    /**
     * Xác định xem advice có được áp dụng cho response này không
     * 
     * @param returnType kiểu trả về của method
     * @param converterType class converter được sử dụng
     * @return true để áp dụng cho tất cả các response
     */
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    /**
     * Xử lý và định dạng response trước khi trả về client Đóng gói dữ liệu vào đối tượng
     * RestResponse chuẩn
     * 
     * @param body dữ liệu gốc từ controller
     * @param returnType kiểu trả về của method
     * @param selectedContentType kiểu content của response
     * @param selectedConverterType class converter được sử dụng
     * @param request HTTP request
     * @param response HTTP response
     * @return response đã được định dạng chuẩn
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType,
            MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request,
            ServerHttpResponse response) {
        HttpServletResponse servletResponse =
                ((ServletServerHttpResponse) response).getServletResponse();
        int status = servletResponse.getStatus();

        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(status);

        String path = request.getURI().getPath();

        if(path.startsWith("/v3/api-docs") || path.startsWith("/swagger-ui")) {
            return body;
        }

        if (body instanceof String || body instanceof Resource) {
            return body;
        }

        if (status >= 400) {
            // Case error
            return body;
        } else {
            // Case success
            res.setData(body);
            ApiMessage message = returnType.getMethodAnnotation(ApiMessage.class);
            res.setMessage(message != null ? message.value() : "Call API success");
        }
        return res;
    }

}
