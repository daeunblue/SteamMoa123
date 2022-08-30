package com.ssafy.backend.db.entity.party;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="ptagstorage")
public class PtagStorage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ptag_id")
    private Long ptagId;

    @Column(name="ptag_content")
    private String content;
}
