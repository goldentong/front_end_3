package com.dr.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @description: user

 */
@Data
public class User {
    // 主键
    @TableId(type = IdType.AUTO)
    private Long id;
    // 昵称
    private String nickname;
    // 账号
    private String account;
    // 密码
    private String password;
    // 原密码
    private String oldPassword;
    // 手机号
    private String phone;
    // 邮箱
    private String email;
    // 备注
    private String remark;
    // 角色id
    private String roleIds;
    // 创建时间
    private Date createDate;
    // 角色
    @TableField(exist = false)
    private List<Role> roles;
}
