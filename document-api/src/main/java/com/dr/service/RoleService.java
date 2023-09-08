package com.dr.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dr.bean.Role;

/**
 * @description: role service
 */
public interface RoleService extends IService<Role> {

    /**
     * @description: Paging for user information
     * @param: page
     * @param: keyWord
     */
    Page<Role> getPageList(Page page, String keyWord);

    /**
     * @description: Add or Edit Roles
     * @param: role info
     */
    void saveOrUpdateData(Role role);

    /**
     * @description: delete role
     * @param: id
     */
    void deleteData(Long id);
}
