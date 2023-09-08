package com.dr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dr.bean.Menu;
import com.dr.bean.Role;
import com.dr.mapper.MenuMapper;
import com.dr.mapper.RoleMapper;
import com.dr.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<Menu> getList() {
        // 获取菜单数据
        QueryWrapper<Menu> queryWrapper = new QueryWrapper();
        List<Menu> menus = menuMapper.selectList(queryWrapper);
        // 处理父级菜单
        List<Menu> parentMenus = menus.stream().filter(item -> item.getParentId() == -1) // 过滤
                .sorted(Comparator.comparing(Menu::getSort)).collect(Collectors.toList()); // 排序
        // 处理子级菜单
        Map<Long, List<Menu>> childrenMap = menus.stream().filter(item -> item.getParentId() != -1) // 过滤
                .collect(Collectors.groupingBy(Menu::getParentId));// 分组
        for (Menu parentMenu : parentMenus) {
            Long id = parentMenu.getId();
            List<Menu> childMenus = childrenMap.get(id);
            childMenus = childMenus.stream().sorted(Comparator.comparing(Menu::getSort)).collect(Collectors.toList()); // 排序
            parentMenu.setChildren(childMenus);
        }
        return parentMenus;
    }

    @Override
    public List<Menu> getPermissionList(String roleIds) {
        List<Menu> menus = new ArrayList<>();
        // 获取角色权限
        String[] roleIdsArray = roleIds.split(",");
        List<Long> ids = new ArrayList<>();
        for (String roleId : roleIdsArray) {
            ids.add(Long.valueOf(roleId));
        }
        QueryWrapper<Role> queryWrapper = new QueryWrapper();
        queryWrapper.in("id", ids);
        List<Role> roles = roleMapper.selectList(queryWrapper);
        // 过滤权限为空的角色
        roles = roles.stream().filter(item -> !StringUtils.isEmpty(item.getMenuIds())).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(roles)) {
            List<String> menuIdList = roles.stream().map(Role::getMenuIds).collect(Collectors.toList());
            List<Long> finalMenuIds = new ArrayList<>();
            for (String menuIds : menuIdList) {
                String[] menuIdArray = menuIds.split(",");
                for (String menuId : menuIdArray) {
                    Long menuIdLong = Long.valueOf(menuId);
                    if (!finalMenuIds.contains(menuIdLong)) {
                        finalMenuIds.add(menuIdLong);
                    }
                }
            }
            // 获取所有菜单
            List<Menu> menuList = this.list();
            // 过滤父级
            menuList = menuList.stream().filter(item -> finalMenuIds.contains(item.getId())).collect(Collectors.toList());
            menus = menuList;
        }
        return menus;
    }

    @Override
    public void updateData(Menu menu) {
        // 执行修改
        this.updateById(menu);
    }
}
