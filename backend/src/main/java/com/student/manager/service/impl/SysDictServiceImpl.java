package com.student.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.student.manager.entity.SysDict;
import com.student.manager.mapper.SysDictMapper;
import com.student.manager.service.SysDictService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements SysDictService {

    @Override
    public IPage<SysDict> pageList(String keyword, int page, int pageSize) {
        LambdaQueryWrapper<SysDict> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(SysDict::getDictName, keyword)
                   .or().like(SysDict::getDictType, keyword);
        }
        wrapper.orderByAsc(SysDict::getSort);
        return page(new Page<>(page, pageSize), wrapper);
    }

    @Override
    public void createDict(SysDict dict) {
        save(dict);
    }

    @Override
    public void updateDict(Long id, SysDict dict) {
        dict.setId(id);
        updateById(dict);
    }

    @Override
    public void deleteDict(Long id) {
        removeById(id);
    }
}
