package com.dr.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.List;

/**
 * @description: menu

 */
@Data
public class Menu {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String code;

    private Long parentId;

    private Integer sort;

    private String remark;

    @TableField(exist = false)
    private List<Menu> children;
}
