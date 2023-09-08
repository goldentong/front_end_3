package com.dr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dr.bean.Role;
import com.dr.bean.User;
import com.dr.mapper.RoleMapper;
import com.dr.service.RoleService;
import com.dr.service.UserService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserService userService;

    @Override
    public Page<Role> getPageList(Page page, String keyWord) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper();
        if (!StringUtils.isEmpty(keyWord)) {
            queryWrapper.like("name", keyWord) // 名称
                    .or().like("remark", keyWord); // 备注
        }
        // 分页查询
        return roleMapper.selectPage(page, queryWrapper);
    }

    @Override
    public void saveOrUpdateData(Role role) {
        // 执行保存
        this.saveOrUpdate(role);
    }

    @Override
    public void deleteData(Long id) {
        // 处理用户数据
        List<User> users = userService.list();
        if (!CollectionUtils.isEmpty(users)) {
            for (User user : users) {
                // 处理角色
                String roleIds = user.getRoleIds();
                if (!StringUtils.isEmpty(roleIds)) {
                    List<String> ids = Arrays.asList(roleIds.split(",")).
                            stream().filter(item -> Long.valueOf(item) != id).collect(Collectors.toList());
                    user.setRoleIds(Strings.join(ids, ','));
                }
            }
            userService.updateBatchById(users);
        }
        // 删除角色
        roleMapper.deleteById(id);
    }
}
