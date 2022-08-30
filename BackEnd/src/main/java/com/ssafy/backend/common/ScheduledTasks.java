package com.ssafy.backend.common;

import com.ssafy.backend.api.service.NoticeService;
import com.ssafy.backend.db.entity.party.Party;
import com.ssafy.backend.db.entity.party.Puser;
import com.ssafy.backend.db.repository.party.PartyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/*
    스프링 스케줄러를 이용하여, 서버 작동 중에 자동으로 업데이트 되어야 하거나 처리되어야 하는 일을 구현합니다.

    - 파티 상태 자동 업데이트
    - 알림 발송
 */

@Slf4j
@Component
public class ScheduledTasks {
    @Autowired
    private PartyRepository partyRepository;
    @Autowired
    private NoticeService noticeService;

    // 로그용
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("mm:ss:SSS");

    // 5초마다 파티 상태 자동 업데이트
    // 1 모집 중, 2 모집 완료, 3 플레이 중, 4 플레이 완료, 5 모집 실패
    @Scheduled(cron = "0 0/1 * * * ?")
    public void updatePartyStatus() {

        // 로그
        // log.info("fixedRate: 현재시간 - {}", formatter.format(LocalDateTime.now()));


        for(Party party: partyRepository.findAll()) {
            System.out.println("지금시간:"+LocalDateTime.now()+" 시작시간:"+party.getStartTime().minusHours(9));
            // log.info("파티 번호: {}  플레이 시작: {}  현재: {}", party.getPartyId(), party.getStartTime(), LocalDateTime.now());

            // 파티 상태가 4나 5이면 건너뛰기
            if(party.getStatus().equals("4") || party.getStatus().equals("5")) {
                continue;
            }


            else if(party.getStatus().equals("1")) {
                // 1 모집 중에서 2 모집 완료로 : maxplayer = curplayer
                if(party.getMaxPlayer() <= party.getCurPlayer())
                    party.setStatus("2");

                // 1 모집 중에서 5 모집 실패로 : 지금 시간이 플레이 시간을 지남 && maxplayer > curplayer
                if(LocalDateTime.now().isAfter(party.getStartTime().minusHours(9)) && party.getMaxPlayer() > party.getCurPlayer())
                    party.setStatus("5");
            }


            else if(party.getStatus().equals("2")) {
                // 2 모집 완료에서 3 플레이 중으로 : 지금 시간이 플레이 시간을 지남
                if(LocalDateTime.now().isAfter(party.getStartTime().minusHours(9)))
                    party.setStatus("3");
            }


            else if(party.getStatus().equals("3")) {
                // 3 플레이 중에서 4 플레이 완료로 : 지금 시간이 플레이 시간 + 24h를 지남
                if(LocalDateTime.now().isAfter(party.getStartTime().minusHours(9).plusDays(1))) {
                    party.setStatus("4");

                    // 모든 파티원에게 평가 알림을 발송합니다. (알림을 알림 테이블에 집어넣습니다.)
                    for(Puser puser: party.getPusers()){
                        noticeService.createNotice(puser.getUser().getUserServiceId(), party.getPartyId());
                    }
                }
            }

            partyRepository.save(party);
        }
    }
}
