package com.ssafy.backend.db.entity.banner;

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
@Table(name="banner")
public class Banner {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="banner_id")
    private Long bannerId;

    @Column(name="banner_img_path", length = 1024)
    private String bannerImgPath;

    @Column(name="banner_img_name")
    private String nambannerImgName;

    @Column(name="banner_uri_path", length = 1024)
    private String bannerUriPath;
}
