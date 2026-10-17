package com.student.manager.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.student.manager.entity.OperationLog;
import com.student.manager.mapper.OperationLogMapper;
import com.student.manager.service.LogService;
import org.springframework.stereotype.Service;

@Service
public class LogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements LogService {
}
