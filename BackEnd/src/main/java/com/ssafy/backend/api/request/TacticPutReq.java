package com.ssafy.backend.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 공략글 수정 API ([PUT] /api/tactics) 요청에 필요한 리퀘스트 바디 정의.
 */
@Data
@ApiModel("TacticPutReq")
public class TacticPutReq {
    @ApiModelProperty(name="유저 서비스 ID", example="user_id")
    String userServiceId;

    @ApiModelProperty(name="게임 식별자 ID", example="25551")
    Long gameId;

    @ApiModelProperty(name="공략글 식별자 ID", example="1")
    Long tacticId;

    @ApiModelProperty(name="공략글 타이틀", example="공략글 제목")
    String tacticTitle;

    @ApiModelProperty(name="공략글 내용", example="공략글 내용")
    String tacticContent;
    
    
}
