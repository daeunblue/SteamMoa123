package com.ssafy.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.backend.api.controller.AuthController;
import com.ssafy.backend.api.response.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class AuthControllerTest {

    @Autowired
    AuthController authController;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    @Test
    public void requestBodyTest(){
        UserDto userDto = new UserDto();
        userDto.setUserServiceId("user_id");
//        userDto.setUserServicePw("user_pass");

          //        authController.login(userDto);

    }
}
