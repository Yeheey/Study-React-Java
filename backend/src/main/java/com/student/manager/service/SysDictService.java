package com.student.manager.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.student.manager.entity.SysDict;

public interface SysDictService extends IService<SysDict> {
    IPage<SysDict> pageList(String keyword, int page, int pageSize);
    void createDict(SysDict dict);
    void updateDict(Long id, SysDict dict);
    void deleteDict(Long id);
}
