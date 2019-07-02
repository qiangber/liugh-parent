package com.liugh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liugh.entity.UserToRole;
import com.liugh.mapper.UserToRoleMapper;
import com.liugh.service.IUserToRoleService;
import com.liugh.util.ComUtil;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liugh123
 * @since 2018-05-03
 */
@Service
public class UserToRoleServiceImpl extends ServiceImpl<UserToRoleMapper, UserToRole> implements IUserToRoleService {

    @Override
//    @Cacheable(value = "UserToRole",keyGenerator="wiselyKeyGenerator")
    public UserToRole selectByUserNo(String userNo) {
        QueryWrapper<UserToRole> ew = new QueryWrapper<>();
        ew.eq("user_no", userNo);
        List<UserToRole> userToRoleList = this.list(ew);
        return ComUtil.isEmpty(userToRoleList)? null: userToRoleList.get(0);
    }
}
