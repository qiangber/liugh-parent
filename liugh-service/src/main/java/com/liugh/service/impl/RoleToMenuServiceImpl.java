package com.liugh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liugh.entity.RoleToMenu;
import com.liugh.mapper.RoleToMenuMapper;
import com.liugh.service.IRoleToMenuService;
import com.liugh.util.ComUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
public class RoleToMenuServiceImpl extends ServiceImpl<RoleToMenuMapper, RoleToMenu> implements IRoleToMenuService {

    @Override
    //redis生成key注解，以类名方法名和参数组成key
//    @Cacheable(value = "UserToRole",keyGenerator="wiselyKeyGenerator")
    public List<RoleToMenu> selectByRoleCode(String roleCode) {
        QueryWrapper<RoleToMenu> ew = new QueryWrapper<>();
        ew.eq("role_code", roleCode);
        return this.list(ew);
    }

    @Override
    public boolean saveAll(String roleCode, List<String> menuCodes) {
        boolean result = true;
        if (!ComUtil.isEmpty(menuCodes)) {
            List<RoleToMenu> modelList = new ArrayList<>();
            for (String menuCode : menuCodes) {
                modelList.add(RoleToMenu.builder().roleCode(roleCode).menuCode(menuCode).build());
            }
            result = this.saveBatch(modelList);
        }
        return result;
    }

    @Override
    public boolean deleteAllByRoleCode(String roleCode) {
        QueryWrapper<RoleToMenu> ew = new QueryWrapper<>();
        ew.eq("role_code", roleCode);
        return this.remove(ew);
    }
}
