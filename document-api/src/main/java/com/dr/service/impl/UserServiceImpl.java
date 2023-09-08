package com.dr.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dr.bean.User;
import com.dr.mapper.UserMapper;
import com.dr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(User user) {
        // 账号
        String account = user.getAccount();
        // 密码
        String password = user.getPassword();
        // 获取数据
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>().eq("account", account)
                .eq("password", SecureUtil.sha256(password));
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public Page<User> getPageList(Page page, String keyWord) {
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        if (!StringUtils.isEmpty(keyWord)) {
            queryWrapper.like("nickname", keyWord) // 昵称
                    .or().like("account", keyWord) // 账号
                    .or().like("phone", keyWord) // 手机号
                    .or().like("email", keyWord) // 邮箱
                    .or().like("remark", keyWord); // 备注
        }
        // 分页查询
        Page users = userMapper.selectPage(page, queryWrapper);
        // 处理旧密码
        List<User> records = users.getRecords();
        for (User record : records) {
            record.setOldPassword(record.getPassword());
        }
        users.setRecords(records);
        return users;
    }

    @Override
    public void saveOrUpdateData(User user) {
        // id
        Long id = user.getId();
        // 判断密码是否修改
        String newPassword;
        // 密码
        String password = user.getPassword();
        // 原密码
        String oldPassword = user.getOldPassword();
        if (id == null) {
            // 密码加密
            newPassword = SecureUtil.sha256(password);
        } else {
            if (!Objects.equals(password, oldPassword) && !Objects.equals(SecureUtil.sha256(password), oldPassword)) {
                newPassword = SecureUtil.sha256(password);
            } else {
                newPassword = oldPassword;
            }
        }
        // 设置密码
        user.setPassword(newPassword);
        user.setOldPassword(newPassword);
        // 执行保存
        this.saveOrUpdate(user);
    }
}
