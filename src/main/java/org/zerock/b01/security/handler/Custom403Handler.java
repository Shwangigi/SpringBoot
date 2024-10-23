package org.zerock.b01.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@Log4j2
public class Custom403Handler implements AccessDeniedHandler {
    // Spring security에서 권한이 없는 사용자가 접근할 때 발생하는 AcessDeniedExecption을 처리하기 위한 Custom403Handler
    // 권한이 없는 403응답을 설정
    
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException{

        log.info("------ access denied -------");

        response.setStatus(HttpStatus.FORBIDDEN.value());  // HTTP응답 상태 코드르 403으로 설정하여 , 클라이언트에게 권한이 없음을 알림

        // JSON 요청이었는지 확인
        String contentType = request.getHeader("Content-Type");

        boolean jsonRequest = contentType.startsWith("application/json");

        log.info("json : " + jsonRequest);

        //일반 request
        if (!jsonRequest){
            response.sendRedirect("/member/login?error=ACCESS+DENIED");
        }
    }
}
