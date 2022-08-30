package com.ssafy.backend.api.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class IntegratedSearch_ContentDTO {
    List<PartylistDTO> parties = new ArrayList<>();
    List<GamelistDTO> games = new ArrayList<>();
    List<IntegratedSearch_TacticDTO> tactics = new ArrayList<>();
}
