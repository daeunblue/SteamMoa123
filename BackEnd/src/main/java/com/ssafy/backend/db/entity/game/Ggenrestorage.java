package com.ssafy.backend.db.entity.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="ggenrestorage")
public class Ggenrestorage {
    @Id
    @Column(name="genre_id")
    private long genreId;

    @Column(name="genre")
    private String genre;
}
