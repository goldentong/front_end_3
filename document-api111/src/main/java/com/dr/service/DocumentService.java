package com.dr.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.dr.bean.Document;

/**
 * @description: personl profile service

 */
public interface DocumentService extends IService<Document> {

    /**
     * @description: Paged search of personnel files
     * @param: page paged data
     * @param: keyWord
     */
    Page<Document> getPageList(Page page, String keyWord);

    /**
     * @description: add edit profile
     * @param: profile info
     */
    void saveOrUpdateData(Document document);

    /**
     * @description: delete profile
     * @param: id
     */
    void deleteData(Long id);
}
