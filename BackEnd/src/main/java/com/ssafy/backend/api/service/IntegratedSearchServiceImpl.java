package com.ssafy.backend.api.service;

import com.ssafy.backend.api.response.*;
import com.ssafy.backend.db.entity.game.Game;
import com.ssafy.backend.db.entity.game.Gamecategory;
import com.ssafy.backend.db.entity.party.Party;
import com.ssafy.backend.db.entity.party.Puser;
import com.ssafy.backend.db.entity.tactic.Tactic;
import com.ssafy.backend.db.entity.user.User;
import com.ssafy.backend.db.repository.tactic.TacticRepository;
import com.ssafy.backend.db.repository.user.UserRepository;
import com.ssafy.backend.db.repository.game.GameRepository;
import com.ssafy.backend.db.repository.party.PartyRepository;
import com.ssafy.backend.db.repository.party.PuserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("integratedSearchService")
public class IntegratedSearchServiceImpl implements IntegratedSearchService{
    @Autowired
    private PartyRepository partyRepository;
    @Autowired
    private PuserRepository puserRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TacticRepository tacticRepository;

    @Override
    public Map<String,Object> getIntegratedSearch(String type, String keyword) {
        Map<String,Object> resultmap = new HashMap<>();

        if(type.equals("user")){
            List<IntegratedSearch_UserDTO> integratedSearchUserDTOList = new ArrayList<>();
            User theUser = userRepository.findByUserServiceId(keyword).orElse(null);
            List<User> users = userRepository.findAllByUserServiceIdContains(keyword).orElse(null);

            if(theUser != null) {
                IntegratedSearch_UserDTO temp_user = new IntegratedSearch_UserDTO(theUser);
                for(Puser puser: puserRepository.findTop3ByUserOrderByPuserIdDesc(theUser).orElse(null)){
                    temp_user.getUserParties().add(new PartylistDTO(puser.getParty()));
                }
                integratedSearchUserDTOList.add(temp_user);
                if(users != null)
                    users.remove(theUser);
            }

            if(users != null) {
                for(User user: users) {
                    IntegratedSearch_UserDTO temp_user = new IntegratedSearch_UserDTO(user);
                    for(Puser puser: puserRepository.findTop3ByUserOrderByPuserIdDesc(user).orElse(null)){
                        temp_user.getUserParties().add(new PartylistDTO(puser.getParty()));
                    }
                    integratedSearchUserDTOList.add(temp_user);
                }
            }

            resultmap.put("users", integratedSearchUserDTOList);
        }
        else if(type.equals("content")){
            IntegratedSearch_ContentDTO integratedSearchContentDTO = new IntegratedSearch_ContentDTO();
            List<Game> games = gameRepository.findAllMultiGameByOnlyName(keyword).orElse(Collections.EMPTY_LIST);
            for(Game g: games){
                if(g.getName().equals(keyword)) {
                    games.remove(g);
                    games.add(0, g);
                }
            }

            for(Game game: games) {
                integratedSearchContentDTO.getGames().add(new GamelistDTO(game));
                if(integratedSearchContentDTO.getGames().size() >= 8)
                    break;
            }
            for(Game game: games) {
                for (Party party : (List<Party>) partyRepository.findTop8ByGameOrderByWriteTimeDesc(game).orElse(Collections.EMPTY_LIST)) {
                    integratedSearchContentDTO.getParties().add(new PartylistDTO(party));
                    if(integratedSearchContentDTO.getParties().size() >= 8)
                        break;
                }
                if(integratedSearchContentDTO.getParties().size() >= 8)
                    break;
            }
            for(Game game: games) {
                for(Tactic tactic: (List<Tactic>) tacticRepository.findTop8ByGameGameIdOrderByCreateTimeDesc(game.getGameId()).orElse(Collections.EMPTY_LIST)) {
                    if(integratedSearchContentDTO.getTactics().size() >= 8)
                        break;
                    integratedSearchContentDTO.getTactics().add(new IntegratedSearch_TacticDTO(tactic));
                }
                if(integratedSearchContentDTO.getTactics().size() >= 8)
                    break;
            }


            resultmap.put("contents", integratedSearchContentDTO);
        }
        else
            resultmap.put("message", "fail: wrong searching type");

        return resultmap;
    }
}
