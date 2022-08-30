package com.ssafy.backend.api.response;


import com.ssafy.backend.common.model.response.BaseResponseBody;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * 유저 로그인 API ([POST] /api/v1/auth) 요청에 대한 응답값 정의.
 */
@Getter
@Setter
@ApiModel("UserPwCheckGetResponse")
public class UserPwCheckPostRes extends BaseResponseBody {
	public static UserPwCheckPostRes of(Integer statusCode, String message) {
		UserPwCheckPostRes res = new UserPwCheckPostRes();
		res.setStatusCode(statusCode);
		res.setMessage(message);
		return res;
	}
}
