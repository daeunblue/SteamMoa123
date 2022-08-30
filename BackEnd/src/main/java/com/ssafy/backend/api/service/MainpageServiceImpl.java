package com.ssafy.backend.api.service;

import com.ssafy.backend.api.response.*;
import com.ssafy.backend.db.entity.banner.Banner;
import com.ssafy.backend.db.entity.game.Game;
import com.ssafy.backend.db.entity.party.Party;
import com.ssafy.backend.db.entity.party.Puser;
import com.ssafy.backend.db.entity.tactic.Tactic;
import com.ssafy.backend.db.entity.user.User;
import com.ssafy.backend.db.repository.banner.BannerRepository;
import com.ssafy.backend.db.repository.tactic.TacticRepository;
import com.ssafy.backend.db.repository.user.UserRepository;
import com.ssafy.backend.db.repository.game.GameRepository;
import com.ssafy.backend.db.repository.party.PartyRepository;
import com.ssafy.backend.db.repository.party.PuserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("mainpageService")
public class MainpageServiceImpl implements MainpageService{
    @Autowired
    private BannerRepository bannerRepository;
    @Autowired
    private PartyRepository partyRepository;
    @Autowired
    private GameRepository gameRepository;


    @Override
    public MainpageDTO getMainpage() {
        MainpageDTO resultItem = new MainpageDTO();
        Pageable pageable = PageRequest.of(0, 15);

        // 배너
        List<Banner> banners = bannerRepository.findAll();
        banners.forEach(Banner->resultItem.addBanners(Banner));

        // 파티
        List<Party> parties = partyRepository.findAllPartyByFilter("", null, new String[]{"1"}, "1", pageable).orElse(Collections.EMPTY_LIST);
        System.out.println("파티 사이즈: "+parties.size());
        parties.forEach(Party->resultItem.addParties(Party));
        if(resultItem.getParties().size() > 20){
            resultItem.setParties(resultItem.getParties().subList(0, 20));
        }

        // 기획상품
        List<Game> templist;
        List<Game> randlist;

        // Bests 5
        templist = gameRepository.findAllMultiGameForBests().orElse(Collections.EMPTY_LIST);
        for(int i = 0; i < 5; i++) {
            resultItem.addBests(templist.get(i));
        }

        // Frees 10
        templist = gameRepository.findAllMultiGameForFrees().orElse(Collections.EMPTY_LIST);
        for(int i = 0; i < 10; i++) {
            resultItem.addFrees(templist.get(i));
        }

        // Today 5
        templist = gameRepository.findAllMultiGameForToday().orElse(Collections.EMPTY_LIST);
        for(int i = 0; i < 5; i++) {
            resultItem.addToday(templist.get(i));
        }

        // Hots 10
        templist = gameRepository.findAllMultiGameForHots().orElse(Collections.EMPTY_LIST);
        randlist = gameRepository.findTop15MultiGameForRandom().orElse(Collections.EMPTY_LIST);
        for(int i = 0; i < 10; i++) {
            if(i < templist.size())
                resultItem.addHots(templist.get(i));
            else
                resultItem.addHots(randlist.get(i));
        }

        // Picks 10
        templist = gameRepository.findAllMultiGameForPicks().orElse(Collections.EMPTY_LIST);
        randlist = gameRepository.findTop15MultiGameForRandom().orElse(Collections.EMPTY_LIST);
        for(int i = 0; i < 10; i++) {
            if(i < templist.size())
                resultItem.addPicks(templist.get(i));
            else
                resultItem.addPicks(randlist.get(i));
        }

        return resultItem;
    }

}
