package com.ssafy.backend.db.repository.party;

import com.ssafy.backend.db.entity.party.Party;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

/*
    /api/moazone?name=게임이름&genre=장르1&genre=장르2&status=1&sort=1

    필터링: 게임 이름, 파티 상태들, 파티 태그들

    status
    모집 중: 1
    모집 완료: 2
    플레이 중: 3
    플레이 완료: 4
    모집 실패: 5

    sort
    파티 생성 최근순: 1
    마감 날짜 가까운 순: 2
    마감 인원 많은 순:3
 */
public class PartyCustomRepositoryImpl implements PartyCustomRepository {

    @Autowired
    EntityManager em;

    @Override
    public Optional<List<Party>> findAllPartyByFilter(String searchString, String[] partyTags, String[] partyStatuses, String sortString, Pageable pageable) {
        // 쿼리문 생성
        String sql = "select *, party.cur_player/party.max_player as per \n" +
                "from\n" +
                "\t(       \n" +
                "\t\tselect game.game_id, game.game_name, game.game_img\n" +
                "\t\tfrom\n" +
                "\t\t\t(select\tgame_id \n" +
                "\t\t\tfrom gamecategory \n" +
                "\t\t\twhere gamecategory.category_id = 1) as gamecategory \n" +
                "\t\tinner join game \n" +
                "\t\ton gamecategory.game_id = game.game_id ";

        sql +=  "where game.game_name like \'%" + searchString + "%\' and (game.game_price != -1 or game.is_free = 1) ) as game\n"+
        "inner join \n" +
                "(\n" +
                "\tselect party.game_id, party.party_id, party.party_title, party.max_player, party.cur_player, party.start_time, party.write_time, party.party_status, party.chat_link, party.party_description, party.is_closed\n" +
                "    from party \n" +
                "    left join partytag as partytag\n" +
                "    on party.party_id = partytag.party_id\n";

        // 파티 태그
        if(partyTags != null && partyTags.length != 0){
            sql += "where \n";
            for (int i = 0; i < partyTags.length; i++) {
                sql += "partytag.ptag_id = \'" + partyTags[i] + "\'\n";
                if(i != partyTags.length-1)
                    sql += "or \n";
            }
        }

        sql +=  "    group by party.party_id\n" +
                ") as party\n" +
                "on party.game_id = game.game_id\n";

        // 파티 상태
        if(partyStatuses != null && partyStatuses.length != 0){
            sql += "where \n";
            for (int i = 0; i < partyStatuses.length; i++) {
                sql += "party.party_status = \'" + partyStatuses[i] + "\'\n";
                if(i != partyStatuses.length-1)
                    sql += "or \n";
            }
        }

        if (sortString.equals("1"))
            sql += "order by party.write_time desc";
        else if(sortString.equals("2"))
            sql += "order by party.start_time desc";
        else if(sortString.equals("3"))
            sql += "order by per desc";

        Query query = em.createNativeQuery(sql, Party.class);
        List<Party> partylist = query
                .setMaxResults(pageable.getPageSize())
                .setFirstResult(pageable.getPageSize()*pageable.getPageNumber())
                .getResultList();

        return Optional.ofNullable(partylist);
    }

        @Override
        public int findAllPartyByFilter(String searchString, String[] partyTags, String[] partyStatuses, String sortString) {
            // 쿼리문 생성
            String sql = "select *, party.cur_player/party.max_player as per \n" +
                    "from\n" +
                    "\t(       \n" +
                    "\t\tselect game.game_id, game.game_name, game.game_img\n" +
                    "\t\tfrom\n" +
                    "\t\t\t(select\tgame_id \n" +
                    "\t\t\tfrom gamecategory \n" +
                    "\t\t\twhere gamecategory.category_id = 1) as gamecategory \n" +
                    "\t\tinner join game \n" +
                    "\t\ton gamecategory.game_id = game.game_id ";

            sql +=  "where game.game_name like \'%" + searchString + "%\' and (game.game_price != -1 or game.is_free = 1) ) as game\n"+
                    "inner join \n" +
                    "(\n" +
                    "\tselect party.game_id, party.party_id, party.party_title, party.max_player, party.cur_player, party.start_time, party.write_time, party.party_status, party.chat_link, party.party_description, party.is_closed\n" +
                    "    from party \n" +
                    "    left join partytag as partytag\n" +
                    "    on party.party_id = partytag.party_id\n";

            // 파티 태그
            if(partyTags != null && partyTags.length != 0){
                sql += "where \n";
                for (int i = 0; i < partyTags.length; i++) {
                    sql += "partytag.ptag_id = \'" + partyTags[i] + "\'\n";
                    if(i != partyTags.length-1)
                        sql += "or \n";
                }
            }

            sql +=  "    group by party.party_id\n" +
                    ") as party\n" +
                    "on party.game_id = game.game_id\n";

            // 파티 상태
            if(partyStatuses != null && partyStatuses.length != 0){
                sql += "where \n";
                for (int i = 0; i < partyStatuses.length; i++) {
                    sql += "party.party_status = \'" + partyStatuses[i] + "\'\n";
                    if(i != partyStatuses.length-1)
                        sql += "or \n";
                }
            }

            if (sortString.equals("1"))
                sql += "order by party.write_time desc";
            else if(sortString.equals("2"))
                sql += "order by party.start_time desc";
            else if(sortString.equals("3"))
                sql += "order by per desc";

            Query query = em.createNativeQuery(sql, Party.class);
            List<Party> partylist = query.getResultList();

            return partylist.size();
        }
}
