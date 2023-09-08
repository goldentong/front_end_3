package com.dr.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dr.bean.PageResponse;
import com.dr.bean.Response;
import com.dr.bean.Role;
import com.dr.bean.User;
import com.dr.service.RoleService;
import com.dr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description: user controller

 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * @description: get all role
     */
    @GetMapping("/all")
    public Response getAll() {
        List<Role> roles = roleService.list();
        return PageResponse.ok(roles);
    }

    /**
     * @description: Paging for role information
     * @param: page
     * @param: limit
     * @param: keyWord
     */
    @GetMapping
    public PageResponse getPageList(@RequestParam(required = false, defaultValue = "1") long page,
                                @RequestParam(required = false, defaultValue = "10") long size,
                                String keyWord) {
        // 分页查询
        Page<Role> pageList = roleService.getPageList(new Page<>(page, size), keyWord);
        return PageResponse.ok(pageList.getRecords(), pageList.getTotal());
    }

    /**
     * @description: add edit role
     * @param: role
     */
    @PostMapping
    public Response saveOrUpdateData(@RequestBody Role role) {
        // para check
        if (BeanUtil.isEmpty(role)) {
            return Response.error("para miss");
        }
        roleService.saveOrUpdateData(role);
        return Response.ok();
    }

    /**
     * @description: delete para
     * @param: id
     */
    @DeleteMapping("/{id}")
    public Response deleteData(@PathVariable Long id) {
        // 参数校验
        if (id == null) {
            return Response.error("para miss");
        }
        // excute delete
        roleService.deleteData(id);
        return Response.ok();
    }
}
