package com.ssafy.backend.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 유저 회원가입 API ([POST] /api/v1/users) 요청에 필요한 리퀘스트 바디 정의.
 */
@Data
@ApiModel("UserRegisterPostRequest")
public class UserRegisterPostReq {
	@ApiModelProperty(name="유저 steam식별자", example = "user_steam_id")
	String user_steam_id;

	@ApiModelProperty(name="유저 ID", example="user_id")
	String user_service_id;

	@ApiModelProperty(name="유저 Password", example="user_password")
	String user_service_pw;

	@ApiModelProperty(name="유저 nickname", example="user_name")
	String user_name;
}
