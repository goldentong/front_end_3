package com.dr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dr.bean.Menu;

import java.util.List;

/**
 * @description: menu service

 */
public interface MenuService extends IService<Menu> {


    /**
     * @description:search menu info
     */
    List<Menu> getList();

    /**
     * @description: search menu info with authority
     * @param: roleIds
     */
    List<Menu> getPermissionList(String roleIds);

    /**
     * @description: edit meny
     * @param: menu info
     */
    void updateData(Menu menu);
}
