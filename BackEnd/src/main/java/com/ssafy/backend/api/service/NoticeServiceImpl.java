package com.ssafy.backend.api.service;

import com.ssafy.backend.db.entity.notice.Notice;
import com.ssafy.backend.api.response.NoticeDTO;
import com.ssafy.backend.db.repository.notice.NoticeRepository;
import com.ssafy.backend.db.repository.user.UserRepository;
import com.ssafy.backend.db.repository.game.GameRepository;
import com.ssafy.backend.db.repository.party.PartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service("noticeService")
public class NoticeServiceImpl implements NoticeService {
    @Autowired
    NoticeRepository noticeRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PartyRepository partyRepository;
    @Autowired
    GameRepository gameRepository;

    @Override
    public String createNotice(String userServiceId, Long partyId) {
        if(!userRepository.findByUserServiceId(userServiceId).isPresent())
            return "fail: 유효하지 않은 유저 아이디";
        if(partyRepository.findByPartyId(partyId) == null)
            return "fail: 유효하지 않은 파티 아이디";

        Notice notice = new Notice(
                null,
                userServiceId,
                partyId,
                LocalDateTime.now().plusHours(9),
                false
        );

        noticeRepository.save(notice);

        return "success";
    }

    @Override
    public List<NoticeDTO> getNoticeList(String userServiceId) {
        List<NoticeDTO> noticeDTOS = new ArrayList<>();
        List<Notice> notices = noticeRepository.findAllByUserServiceId(userServiceId).orElse(null);

        for (Notice n: notices) {
            NoticeDTO tempNoticeDTO = new NoticeDTO(
                    n.getNoticeId(),
                    n.getUserServiceId(),
                    n.getPartyId(),
                    partyRepository.findByPartyId(n.getPartyId()).orElse(null).getTitle(),
                    partyRepository.findByPartyId(n.getPartyId()).orElse(null).getGame().getName(),
                    n.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")),
                    n.isRead()
            );

            noticeDTOS.add(tempNoticeDTO);
        }
        return noticeDTOS;
    }

    @Override
    public String deleteNotice(Long noticeId) {
        if(!noticeRepository.findByNoticeId(noticeId).isPresent())
            return "fail: 유효한 notice id가 아닙니다.";

        noticeRepository.delete(noticeRepository.findByNoticeId(noticeId).orElse(null));

        return "success";
    }

    @Override
    public String readNotice(Long noticeId) {
        if(!noticeRepository.findByNoticeId(noticeId).isPresent())
            return "fail: 유효한 notice id가 아닙니다.";

        Notice notice = noticeRepository.findByNoticeId(noticeId).orElse(null);
        notice.setRead(true);
        noticeRepository.save(notice);
        return "success";
    }
}
