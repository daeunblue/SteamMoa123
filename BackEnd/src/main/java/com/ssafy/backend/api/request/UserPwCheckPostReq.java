package com.ssafy.backend.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 유저 회원가입 API ([POST] /api/users/login) 요청에 필요한 리퀘스트 바디 정의.
 */
@Data
@ApiModel("UserPwCheckPostRequest")
public class UserPwCheckPostReq {
    @ApiModelProperty(name="유저 ID", example="user_id")
    String user_service_id;

    @ApiModelProperty(name="유저 Password", example="user_pw")
    String user_service_pw;
}

