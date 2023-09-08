package com.dr.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dr.bean.Document;
import com.dr.bean.PageResponse;
import com.dr.bean.Response;
import com.dr.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: profile controller
 */
@RestController
@RequestMapping("/document")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    /**
     * @description: Pagination of personnel file information
     * @param: page
     * @param: limit
     * @param: keyWord
     */
    @GetMapping
    public PageResponse getPageList(@RequestParam(required = false, defaultValue = "1") long page,
                                @RequestParam(required = false, defaultValue = "10") long size,
                                String keyWord) {
        // paging search
        Page<Document> pageList = documentService.getPageList(new Page<>(page, size), keyWord);
        return PageResponse.ok(pageList.getRecords(), pageList.getTotal());
    }

    /**
     * @description:  edit,create profile
     * @param: profile
     */
    @PostMapping
    public Response saveOrUpdateData(@RequestBody Document document) {

        if (BeanUtil.isEmpty(document)) {
            return Response.error("error");
        }
        documentService.saveOrUpdateData(document);
        return Response.ok();
    }

    /**
     * @description: delete profile
     * @param: id personnel profile
     */
    @DeleteMapping("/{id}")
    public Response deleteData(@PathVariable Long id) {
        // parameter calibration
        if (id == null) {
            return Response.error("error");
        }
        // excute delete
        documentService.deleteData(id);
        return Response.ok();
    }
}
