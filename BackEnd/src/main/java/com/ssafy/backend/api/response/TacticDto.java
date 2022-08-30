package com.ssafy.backend.api.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class TacticDto {
    Long tacticId;
    String tacticTitle;
    String tacticContent;
    Long userId;
    String userServiceId;
    Long gameId;
    String gameName;
    String gameImgPath;
    LocalDateTime createTime;
    Integer status;
}
