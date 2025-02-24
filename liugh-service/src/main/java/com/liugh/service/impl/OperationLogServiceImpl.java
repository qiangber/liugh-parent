package com.liugh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liugh.entity.OperationLog;
import com.liugh.mapper.OperationLogMapper;
import com.liugh.service.IOperationLogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 操作日志 服务实现类
 * </p>
 *
 * @author liugh123
 * @since 2018-05-08
 */
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements IOperationLogService {

}
