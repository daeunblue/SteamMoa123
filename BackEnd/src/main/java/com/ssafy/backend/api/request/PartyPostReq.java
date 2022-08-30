package com.ssafy.backend.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 파티 생성 API ([POST] /api/moazone/) 요청에 필요한 리퀘스트 바디 정의.
 */
@Data
@ApiModel("PartyPostRequest")
public class PartyPostReq {
	@ApiModelProperty(name="게임 ID", example = "73048")
	Long gameId;

	@ApiModelProperty(name="파티 이름", example="스밸할 사람!")
	String partyTitle;

	@ApiModelProperty(name="파티 최대 인원", example="3")
	String maxPlayer;

	@ApiModelProperty(name="파티 설명", example="빨리 모여라~")
	String partyDescription;

	@ApiModelProperty(name="게임 플레이 약속 시간", example="2022-08-15T12:15")
	String startTime;

	@ApiModelProperty(name="외부 소통 링크(디스코드 등)", example="https://discord.com/invite/fakelink")
	String chatLink;

	@ApiModelProperty(name="파티 성향 태그(태그 코드)", example="[\"1\", \"2\"]")
	String[] partyTags;

	@ApiModelProperty(name="파티장 유저 서비스 ID", example="user_id")
	String userId;


}
