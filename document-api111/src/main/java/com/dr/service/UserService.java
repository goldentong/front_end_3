package com.dr.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dr.bean.User;

/**
 * @description: user service

 */
public interface UserService extends IService<User> {

    /**
     * @description: login
     * @param: user info
     */
    User login(User user);

    /**
     * @description: Paging for user information
     * @param: paged data
     * @param: keyWord
     */
    Page<User> getPageList(Page page, String keyWord);

    /**
     * @description: add or edit user
     * @param: userinfo
     */
    void saveOrUpdateData(User user);
}
