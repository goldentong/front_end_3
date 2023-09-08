package com.dr.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dr.bean.PageResponse;
import com.dr.bean.Response;
import com.dr.bean.User;
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

/**
 * @description: user controller

 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * @description: login
     * @param: user info
     */
    @PostMapping("/login")
    public Response login(@RequestBody User user) {
        // check
        if (BeanUtil.isEmpty(user)) {
            return Response.error("paramiss");
        }
        // check pin
        if (StringUtils.isEmpty(user.getAccount()) || StringUtils.isEmpty(user.getPassword())) {
            return Response.error("paramiss");
        }
        User result = userService.login(user);
        if (BeanUtil.isEmpty(result)) {
            return Response.error("loginfail");
        }
        return Response.ok(result);
    }


    /**
     * @description: Paging for user information
     * @param: page
     * @param: limit
     * @param: keyWord
     */
    @GetMapping
    public PageResponse getPageList(@RequestParam(required = false, defaultValue = "1") long page,
                                @RequestParam(required = false, defaultValue = "10") long size,
                                String keyWord) {
        // 分页查询
        Page<User> pageList = userService.getPageList(new Page<>(page, size), keyWord);
        return PageResponse.ok(pageList.getRecords(), pageList.getTotal());
    }

    /**
     * @description: add, edit user
     * @param: user info
     */
    @PostMapping
    public Response saveOrUpdateData(@RequestBody User user) {
        // para check
        if (BeanUtil.isEmpty(user)) {
            return Response.error("para miss");
        }
        userService.saveOrUpdateData(user);
        return Response.ok();
    }

    /**
     * @description: delete user
     * @param: id
     */
    @DeleteMapping("/{id}")
    public Response deleteData(@PathVariable Long id) {
        // para check
        if (id == null) {
            return Response.error("para miss");
        }
        // excute delete
        userService.removeById(id);
        return Response.ok();
    }
}
