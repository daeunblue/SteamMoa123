package com.ssafy.backend.api.service;

import com.ssafy.backend.api.request.TacticPostReq;
import com.ssafy.backend.api.request.TacticPutReq;
import com.ssafy.backend.api.response.TacticDto;
import com.ssafy.backend.db.entity.tactic.Tactic;
import com.ssafy.backend.db.repository.tactic.TacticRepository;
import com.ssafy.backend.db.repository.user.UserRepository;
import com.ssafy.backend.db.repository.game.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

@Service
public class TacticServiceImpl implements TacticService{
    @Autowired
    TacticRepository tacticRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    GameRepository gameRepository;

    @Override
    public List<TacticDto> getTacticsByGameId(Long gameId) {
        List<Tactic> list = new ArrayList<>();
        List<TacticDto> resultList = new ArrayList<>();
        Optional<List<Tactic>> optionalTactics = tacticRepository.findByGameGameId(gameId);

        if(optionalTactics.isPresent()) {
            for (Tactic tactic : optionalTactics.get()) {
                TacticDto tacticDto = new TacticDto();
                tacticDto.setTacticId(tactic.getTacticId());
                tacticDto.setTacticTitle(tactic.getTacticTitle());
                tacticDto.setTacticContent(tactic.getTacticContent());
                tacticDto.setCreateTime(tactic.getCreateTime());
                tacticDto.setUserId(tactic.getUser().getUserId());
                tacticDto.setUserServiceId(tactic.getUser().getUserServiceId());
                tacticDto.setGameId(tactic.getGame().getGameId());
                tacticDto.setGameImgPath(tactic.getGame().getImgpath());
                tacticDto.setGameName(tactic.getGame().getName());
                resultList.add(tacticDto);
            }
        }

        return resultList;
    }

    @Override
    public List<TacticDto> getTacticsByUserId(String userId) {
        long userlongid = userRepository.findByUserServiceId(userId).get().getUserId();
        List<Tactic> list = tacticRepository.findByUserUserId(userlongid).get();
        List<TacticDto> resultList = new ArrayList<>();

        for (Tactic tactic : list) {
            TacticDto tacticDto = new TacticDto();
            tacticDto.setTacticId(tactic.getTacticId());
            tacticDto.setTacticTitle(tactic.getTacticTitle());
            tacticDto.setTacticContent(tactic.getTacticContent());
            tacticDto.setCreateTime(tactic.getCreateTime());
            tacticDto.setUserId(tactic.getUser().getUserId());
            tacticDto.setUserServiceId(tactic.getUser().getUserServiceId());
            tacticDto.setGameId(tactic.getGame().getGameId());
            tacticDto.setGameImgPath(tactic.getGame().getImgpath());
            tacticDto.setGameName(tactic.getGame().getName());
            resultList.add(tacticDto);
        }

        return resultList;
    }
    @Override
    public TacticDto getTacticByTacticId(Long tacticId) {
        Tactic tactic =  tacticRepository.findByTacticId(tacticId).get();
        TacticDto tacticDto = new TacticDto();
        tacticDto.setTacticId(tactic.getTacticId());
        tacticDto.setTacticTitle(tactic.getTacticTitle());
        tacticDto.setTacticContent(tactic.getTacticContent());
        tacticDto.setCreateTime(tactic.getCreateTime());
        tacticDto.setUserId(tactic.getUser().getUserId());
        tacticDto.setUserServiceId(tactic.getUser().getUserServiceId());
        tacticDto.setGameId(tactic.getGame().getGameId());
        tacticDto.setGameImgPath(tactic.getGame().getImgpath());
        tacticDto.setGameName(tactic.getGame().getName());
        return tacticDto;
    }

    @Override
    public boolean createTactics(TacticPostReq tacticPostReq) {

        Tactic tactic = new Tactic();
        try{
            tactic.setTacticTitle(tacticPostReq.getTacticTitle());
            tactic.setTacticContent(tacticPostReq.getTacticContent());
            /* Optional ????????? ?????? .isPresent()??? null ????????? ???????????? ! */
            /* ???????????? ?????? ????????? ??????(???????????? ?????? ?????????, ???????????? ?????? ????????? ?????? ??????)???????????? */
            if(!userRepository.findByUserServiceId(tacticPostReq.getUserServiceId()).isPresent() || (gameRepository.findByGameId(tacticPostReq.getGameId()) == null)){
                return false;
            }
            tactic.setUser(userRepository.findByUserServiceId(tacticPostReq.getUserServiceId()).get());
            tactic.setGame(gameRepository.findByGameId(tacticPostReq.getGameId()).orElse(null));
            tactic.setCreateTime(LocalDateTime.now().plusHours(9));
            tacticRepository.save(tactic);
            return true;
        }catch (Exception e){
            return false;
        }
    }
    @Override
    public boolean updateTactic(TacticPutReq tacticPutReq){
        Tactic tactic = tacticRepository.findByTacticId(tacticPutReq.getTacticId()).get();
        try{
            tactic.setTacticTitle(tacticPutReq.getTacticTitle());
            tactic.setTacticContent(tacticPutReq.getTacticContent());
            if(!userRepository.findByUserServiceId(tacticPutReq.getUserServiceId()).isPresent() || (gameRepository.findByGameId(tacticPutReq.getGameId()) == null)){
                return false;
            }
            tactic.setUser(userRepository.findByUserServiceId(tacticPutReq.getUserServiceId()).get());
            tactic.setGame(gameRepository.findByGameId(tacticPutReq.getGameId()).orElse(null));
            tacticRepository.save(tactic);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean deleteTactic(Long tacticId){
        Tactic tactic = tacticRepository.findByTacticId(tacticId).get();
        try{
            // ?????? ????????? ??????
            tactic.getUser().getTacticList().remove(tactic);

            // ?????? ????????? ??????
            tactic.getGame().getTactics().remove(tactic);

            // tactic ?????? ??????
            tacticRepository.deleteById(tacticId);

            return true;
        }catch (Exception e){
            return false;
        }
    }
}
