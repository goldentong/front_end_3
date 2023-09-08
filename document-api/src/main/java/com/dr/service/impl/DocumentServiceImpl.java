package com.dr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dr.bean.Document;
import com.dr.mapper.DocumentMapper;
import com.dr.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class DocumentServiceImpl extends ServiceImpl<DocumentMapper, Document> implements DocumentService {

    @Autowired
    private DocumentMapper documentMapper;

    @Override
    public Page<Document> getPageList(Page page, String keyWord) {
        QueryWrapper<Document> queryWrapper = new QueryWrapper();
        if (!StringUtils.isEmpty(keyWord)) {
            queryWrapper.like("name", keyWord)
                    .or().like("languageNames", keyWord)
                    .or().like("favoriteNames", keyWord)
                    .or().like("backInfo", keyWord);
        }
        // 分页查询
        return documentMapper.selectPage(page, queryWrapper);
    }

    @Override
    public void saveOrUpdateData(Document document) {
        if (document.getId() == null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            document.setCreateDate(simpleDateFormat.format(new Date()));
        }
        // 执行保存
        this.saveOrUpdate(document);
    }

    @Override
    public void deleteData(Long id) {
        // 删除文件
        documentMapper.deleteById(id);
    }
}
