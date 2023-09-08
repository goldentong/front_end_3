package com.dr.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @description: role

 */
@Data
public class Role {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String remark;

    private String menuIds;
}
