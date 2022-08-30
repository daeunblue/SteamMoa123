package com.ssafy.backend.api.response;

import com.ssafy.backend.common.model.response.BaseResponseBody;

import com.ssafy.backend.db.entity.user.UserTag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 회원 본인 정보 조회 API ([GET] /api/users/me) 요청에 대한 응답값 정의.
 */
@Getter
@Setter
@Data
@NoArgsConstructor
@ApiModel("UserResponse")
public class UserRes extends BaseResponseBody{
//    Map<String, Object> response = new HashMap<>();

    @ApiModelProperty(name="사용자 인증 번호", example="1")
    private Long userId;

    @ApiModelProperty(name="사용자 아이디", example="ssafy123")
    private String userServiceId;

    @ApiModelProperty(name="사용자 매너점수", example="36.5")
    private Double userPoint;

    @ApiModelProperty(name="사용자 태그 목록", example="[\"즐겜\", \"생존겜 러버\"]")
    private List<String> userTags=new ArrayList<>();

    public static UserRes of(Integer statusCode, String message, Long userId, String userServiceId, Double userPoint, List<UserTag> userTags){
        UserRes res = new UserRes();
        res.setUserId(userId);
        res.setUserServiceId(userServiceId);
        res.setStatusCode(statusCode);
        res.setMessage(message);
        res.setUserPoint(Math.round(userPoint*10)/10.0);
        for (UserTag tag:userTags) {
            res.userTags.add(tag.getUTagStorage().getContent());
        }
        return res;
    }
}
