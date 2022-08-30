package com.ssafy.backend.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 파티 수정 API ([PUT] /api/moazone/) 요청에 필요한 리퀘스트 바디 정의.
 */
@Data
@ApiModel("PartyPutRequest")
public class PartyPutReq {

	@ApiModelProperty(name="파티 설명", example="제발 들어와주세요")
	String partyDescription;

	@ApiModelProperty(name="외부 소통 링크(디스코드 등)", example="https://newlink.com")
	String chatLink;

	@ApiModelProperty(name="파티 성향 태그(태그 코드)", example="[\"1\", \"2\"]")
	String[] partyTags;
}
