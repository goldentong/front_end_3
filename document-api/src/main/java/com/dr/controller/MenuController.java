package com.dr.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dr.bean.Menu;
import com.dr.bean.PageResponse;
import com.dr.bean.Response;
import com.dr.bean.Role;
import com.dr.service.MenuService;
import com.dr.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description:  menu controller

 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    /**
     * @description: get all menu
     */
    @GetMapping("/all")
    public Response getAll() {
        List<Menu> menus = menuService.list();
        return PageResponse.ok(menus);
    }

    /**
     * @description: Query menu information
     */
    @GetMapping
    public Response getList() {
        List<Menu> menus = menuService.getList();
        return Response.ok(menus);
    }

    /**
     * @description: Query menu information with
     * @param: roleIds
     */
    @GetMapping("/permission")
    public Response getPermissionList(@RequestParam String roleIds) {
        // para check
        if (StringUtils.isEmpty(roleIds)) {
            Response.error("para miss");
        }
        List<Menu> menus = menuService.getPermissionList(roleIds);
        return Response.ok(menus);
    }

    /**
     * @description: add,edit menu
     * @param: menu
     */
    @PutMapping
    public Response updateData(@RequestBody Menu menu) {
        // 参数校验
        if (BeanUtil.isEmpty(menu)) {
            return Response.error("para miss");
        }
        menuService.updateData(menu);
        return Response.ok();
    }
}
