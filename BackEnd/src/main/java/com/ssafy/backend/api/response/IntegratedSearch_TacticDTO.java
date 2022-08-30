package com.ssafy.backend.api.response;

import com.ssafy.backend.db.entity.tactic.Tactic;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class IntegratedSearch_TacticDTO extends Tactic {
    private String userServiceId;
    private String gameName;
    private String gameImgPath;

    public IntegratedSearch_TacticDTO(Tactic t) {
        super(t.getTacticId(), t.getTacticTitle(), t.getTacticContent(), t.getCreateTime(), t.getUser(), t.getGame());
        this.userServiceId = t.getUser().getUserServiceId();
        this.gameName = t.getGame().getName();
        this.gameImgPath = t.getGame().getImgpath();
    }
}
