package com.ssafy.backend.api.response;

import com.ssafy.backend.db.entity.user.User;
import com.ssafy.backend.db.entity.user.UserTag;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class IntegratedSearch_UserDTO {
    private Long userId;
    private String userName;
    private String userServiceId;
    private double userPoint;
    private List<String> userTags = new ArrayList<>();
    private List<PartylistDTO> userParties = new ArrayList<>();

    public IntegratedSearch_UserDTO(User u) {
        this.userId = u.getUserId();
        this.userName = u.getUserName();
        this.userServiceId = u.getUserServiceId();
        this.userPoint = u.getUserPoint();
        for(UserTag userTag: u.getUTagLists())
            this.userTags.add(userTag.getUTagStorage().getContent());
    }
}
