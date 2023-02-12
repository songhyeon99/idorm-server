package idorm.idormServer.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

import static idorm.idormServer.exception.ExceptionCode.UNAUTHORIZED_MEMBER;

@Component("customAuthenticationEntryPoint")
public class CustomAuthenticationEntryPointHandler implements AuthenticationEntryPoint {

    private static DefaultExceptionResponseDto errorResponse =
            new DefaultExceptionResponseDto(
                    UNAUTHORIZED_MEMBER.name(),
                    UNAUTHORIZED_MEMBER.getMessage());

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException ex) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();

        response.setStatus(401);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        try (OutputStream os = response.getOutputStream()) {
            objectMapper.writeValue(os, errorResponse);
            os.flush();
        }
    }
}