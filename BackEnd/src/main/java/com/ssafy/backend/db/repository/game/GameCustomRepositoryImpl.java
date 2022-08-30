package com.ssafy.backend.db.repository.game;

import com.ssafy.backend.db.entity.game.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Repository @Transactional
public class GameCustomRepositoryImpl implements GameCustomRepository {

    @Autowired
    EntityManager em;


    @Override
    public Optional<List<Game>> findAllMultiGameByOnlyName(String name){
        // 쿼리문 생성
        String sql = "select * from game inner join gamecategory on game.game_id = gamecategory.game_id where gamecategory.category_id = 1 and (game.game_price != -1 or game.is_free = 1) and game.game_name like \'%" + name + "%\' order by game.game_score desc";
        Query query = em.createNativeQuery(sql, Game.class);
        List<Game> gamelist = query.getResultList();
        return Optional.ofNullable(gamelist);
    }


    @Override
    public Optional<List<Game>> findAllMultiGameByFilter(String name, String[] tag, Pageable pageable) {

        // 쿼리문 생성
        String sql = "select *\n" +
                "from\n" +
                "(select distinct gamecategory.game_id\n" +
                "from\n" +
                "(select game_id\n" +
                "from gamecategory\n" +
                "where gamecategory.category_id = 1) as gamecategory\n" +
                "left join gamegenre\n" +
                "on gamecategory.game_id = gamegenre.game_id\n" +
                "left join ggenrestorage\n" +
                "on gamegenre.genre_id = ggenrestorage.genre_id\n";

        if(tag.length != 0){
            sql += "where \n";
            for (int i = 0; i < tag.length; i++) {
                sql += "ggenrestorage.genre like \'" + tag[i] + "\'\n";
                if(i != tag.length-1)
                    sql += "or \n";
            }
        }

        sql +=  ") as gamegenre\n" +
                "inner join game\n" +
                "on gamegenre.game_id = game.game_id\n" +
                "where game.game_name like \'%" + name + "%\' and (game.game_price != -1 or game.is_free = 1)\n" +
                "order by game.game_score desc";

        Query query = em.createNativeQuery(sql, Game.class);
        List<Game> gamelist = query
                .setMaxResults(pageable.getPageSize())
                .setFirstResult(pageable.getPageSize()*pageable.getPageNumber())
                .getResultList();
        return Optional.ofNullable(gamelist);
    }


    @Override
    public int findAllMultiGameByFilter(String name, String[] tag) {

        // 쿼리문 생성
        String sql = "select *\n" +
                "from\n" +
                "(select distinct gamecategory.game_id\n" +
                "from\n" +
                "(select game_id\n" +
                "from gamecategory\n" +
                "where gamecategory.category_id = 1) as gamecategory\n" +
                "left join gamegenre\n" +
                "on gamecategory.game_id = gamegenre.game_id\n" +
                "left join ggenrestorage\n" +
                "on gamegenre.genre_id = ggenrestorage.genre_id\n";

        if(tag.length != 0){
            sql += "where \n";
            for (int i = 0; i < tag.length; i++) {
                sql += "ggenrestorage.genre like \'" + tag[i] + "\'\n";
                if(i != tag.length-1)
                    sql += "or \n";
            }
        }

        sql +=  ") as gamegenre\n" +
                "inner join game\n" +
                "on gamegenre.game_id = game.game_id\n" +
                "where game.game_name like \'%" + name + "%\' and (game.game_price != -1 or game.is_free = 1)";

        Query query = em.createNativeQuery(sql, Game.class);
        List<Game> gamelist = query.getResultList();
        return gamelist.size();
    }

    @Override
    public Optional<List<Game>> findAllMultiGameForBests() {
        String sql = "select * from game inner join gamecategory on game.game_id = gamecategory.game_id where gamecategory.category_id = 1 and (game.game_price != -1 or game.is_free = 1) and game_score >= 90 order by rand() limit 10;";
        Query query = em.createNativeQuery(sql, Game.class);
        List<Game> gamelist = query.getResultList();
        return Optional.ofNullable(gamelist);
    }

    @Override
    public Optional<List<Game>> findAllMultiGameForFrees() {
        String sql = "select * from game inner join gamecategory on game.game_id = gamecategory.game_id where gamecategory.category_id = 1 and (game.game_price <= 3000 or game.is_free = 1) order by rand() limit 10;";
        Query query = em.createNativeQuery(sql, Game.class);
        List<Game> gamelist = query.getResultList();
        return Optional.ofNullable(gamelist);
    }

    @Override
    public Optional<List<Game>> findAllMultiGameForToday() {
        String sql = "select * from game inner join gamecategory on game.game_id = gamecategory.game_id where gamecategory.category_id = 1 and (game.game_price != -1 or game.is_free = 1) and game_score >= 65 order by rand() limit 10;";
        Query query = em.createNativeQuery(sql, Game.class);
        List<Game> gamelist = query.getResultList();
        return Optional.ofNullable(gamelist);
    }

    @Override
    public Optional<List<Game>> findAllMultiGameForHots() {
        String sql = "select * from game inner join (select game_id, count(*) as cnt from party where write_time > DATE_SUB(NOW(), INTERVAL 7 DAY) group by game_id order by cnt desc) as party on party.game_id = game.game_id order by rand() limit 10;";
        Query query = em.createNativeQuery(sql, Game.class);
        List<Game> gamelist = query.getResultList();
        return Optional.ofNullable(gamelist);
    }

    @Override
    public Optional<List<Game>> findAllMultiGameForPicks() {
        String sql = "select * from game inner join (select game_id, count(*) as cnt from review group by game_id order by cnt desc) as party on party.game_id = game.game_id order by rand() limit 10;";
        Query query = em.createNativeQuery(sql, Game.class);
        List<Game> gamelist = query.getResultList();
        return Optional.ofNullable(gamelist);
    }

    @Override
    public Optional<List<Game>> findTop15MultiGameForRandom() {
        String sql = "select * from game inner join gamecategory on game.game_id = gamecategory.game_id where category_id = 1 and (game.game_price != -1 or game.is_free = 1) order by rand() limit 15;";
        Query query = em.createNativeQuery(sql, Game.class);
        List<Game> gamelist = query.getResultList();
        return Optional.ofNullable(gamelist);
    }
}
