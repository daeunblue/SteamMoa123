package com.ssafy.backend.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
@ApiModel("FollowDto")
public class FollowDto {

    @ApiModelProperty(name="사용자 팔로우/팔로잉 목록", example="[\"팔로워1\", \"팔로워2\"]")
    private List<String> userServiceIdList=new ArrayList<>();

    public FollowDto(List<String> userServiceIdList) {
        for (String tag:userServiceIdList) {
            this.userServiceIdList.add(tag);
        }
    }

    public void addUserServiceId(String userServiceId){
        this.userServiceIdList.add(userServiceId);
    }

//    private String authToken; // 사용자 인증 정보 토큰
}
