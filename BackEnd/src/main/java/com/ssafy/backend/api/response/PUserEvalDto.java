package com.ssafy.backend.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Data
@ApiModel("PUserEvalDto")
public class PUserEvalDto {
    @ApiModelProperty(name="해당 파티원의 사용자 식별자",example = "1")
    private Long userId;

    @ApiModelProperty(name="해당 파티원의 서비스 아이디",example = "user_service_id")
    private String userServiceId;

}
