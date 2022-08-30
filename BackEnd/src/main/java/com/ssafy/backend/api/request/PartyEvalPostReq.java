package com.ssafy.backend.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 파티원 평가 API ([POST] /api/moazone/eval/) 요청에 필요한 리퀘스트 바디 정의.
 */
@Data
@ApiModel("PartyEvalPostReqest")
public class PartyEvalPostReq {
    @ApiModelProperty(name="평가하는 사람의 ID", example = "user_id")
    String voterId;

    @ApiModelProperty(name="평가받는 사람의 ID", example = "2")
    Long userId;

    @ApiModelProperty(name="평가 점수(1~5)", example = "3")
    Integer score;

    @ApiModelProperty(name="파티 ID", example = "1")
    Long partyId;
}
