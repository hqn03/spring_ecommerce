package github.hqn03.auth_service.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        // Log thêm URL để biết chính xác thằng nào đang bị chặn
        log.error("Auth Error tại URL: {} | Lỗi: {}", request.getRequestURI(), authException.getMessage());
        // Kiểm tra xem có phải lỗi từ hệ thống đẩy sang /error không
        Object exception = request.getAttribute("jakarta.servlet.error.exception");
        if (exception != null) {
            log.error("Lỗi gốc gây ra redirect sang /error: ", (Throwable) exception);
        }
//        log.error("Authentication error: {}", authException.getMessage());

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);

        problem.setDetail(authException.getMessage());
        problem.setTitle("Unauthorized");
        problem.setDetail("Authentication is required to access this resource");
        problem.setStatus(HttpStatus.UNAUTHORIZED);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");

        objectMapper.writeValue(response.getOutputStream(), problem);
    }
}
