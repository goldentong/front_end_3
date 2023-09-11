package com.dr.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.List;

/**
 *profile instance
 */
@Data
public class Document {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private Integer age;

    private Integer gender;

    private String roomNumber;

    private String pictureUrl;

    private String backInfo;

    private String languageIds;

    private String languageNames;

    private String favoriteIds;

    private String favoriteNames;

    private String createDate;
}
