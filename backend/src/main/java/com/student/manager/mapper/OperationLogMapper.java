package com.student.manager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.student.manager.entity.OperationLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OperationLogMapper extends BaseMapper<OperationLog> {
}
