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
@Table(name="gcategorystorage")
public class Gcategorystorage {
    @Id
    @Column(name="category_id")
    private long categoryId;

    @Column(name="category")
    private String category;
}
