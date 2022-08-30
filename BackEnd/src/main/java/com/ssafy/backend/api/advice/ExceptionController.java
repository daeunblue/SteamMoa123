package com.ssafy.backend.api.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class ExceptionController {

//    @ExceptionHandler(HttpMessageNotReadableException.class)
//    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadableException(
//            HttpMessageNotReadableException ex) {
//        // 아예 잘못된 형식으로 request 를 요청할 경우 예외 발생
//        Map<String, String> error = new HashMap<>();
//        error.put("code", "ERR 500");
//        error.put("message", "Required request body is missing");
//        return ResponseEntity.badRequest().body(error);
//    }


    // 400
    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<Map<String, String>> BadRequestException(final RuntimeException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("code", "ERR 400");
        error.put("message", "Bad Request, check your uri");
        System.out.println(error);
        return ResponseEntity.badRequest().body(error);
    }

    // 401   - 토큰 권한없을때
    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<Map<String, String>> handleAccessDeniedException(final AccessDeniedException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("code", "ERR 401");
        error.put("message", "AccessDenied, check your token");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    // 500
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Map<String, String>> handleAll(final Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("code", "ERR 500");
        error.put("message", "Server Error");
        System.out.println(ex.getMessage());
        System.out.println(ex.getStackTrace());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}