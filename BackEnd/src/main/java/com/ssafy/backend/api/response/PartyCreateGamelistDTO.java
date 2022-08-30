package com.ssafy.backend.api.response;

import com.ssafy.backend.db.entity.game.Game;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PartyCreateGamelistDTO {
    private Long gameId;
    private String gameName;
    private String gameImgPath;

    public PartyCreateGamelistDTO(Game game) {
        this.gameId = game.getGameId();
        this.gameName = game.getName();
        this.gameImgPath = game.getImgpath();
    }
}
