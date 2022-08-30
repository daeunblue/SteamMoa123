package com.ssafy.backend.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 유저 팔로우 추가 API ([POST] /api/user/follow) 요청에 필요한 리퀘스트 바디 정의.
 */
@Data
@ApiModel("UserFollowPostRequest")
public class UserFollowPostReq {


    @ApiModelProperty(name="본인 ID", example="user_id2")
    String followerUserId;

    @ApiModelProperty(name="팔로우 할 유저 ID", example="user_id")
    String followingUserId;


}
