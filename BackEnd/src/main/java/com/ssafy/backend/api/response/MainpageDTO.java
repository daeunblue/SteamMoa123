package com.ssafy.backend.api.response;

import com.ssafy.backend.db.entity.banner.Banner;
import com.ssafy.backend.db.entity.game.Game;
import com.ssafy.backend.db.entity.party.Party;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MainpageDTO {
    private List<Banner> banners = new ArrayList<>();
    private List<PartylistDTO> parties = new ArrayList<>();

    private List<GamelistDTO> bests = new ArrayList<>();
    private List<GamelistDTO> frees = new ArrayList<>();
    private List<GamelistDTO> today = new ArrayList<>();
    private List<GamelistDTO> hots = new ArrayList<>();
    private List<GamelistDTO> picks = new ArrayList<>();

    public void addBanners(Banner banner){
        banners.add(banner);
    }
    public void addParties(Party party){
        parties.add(new PartylistDTO(party));
    }
    public void addBests(Game game){
        bests.add(new GamelistDTO(game));
    }
    public void addFrees(Game game){
        frees.add(new GamelistDTO(game));
    }
    public void addToday(Game game){
        today.add(new GamelistDTO(game));
    }
    public void addHots(Game game){
        hots.add(new GamelistDTO(game));
    }
    public void addPicks(Game game){
        picks.add(new GamelistDTO(game));
    }
}
