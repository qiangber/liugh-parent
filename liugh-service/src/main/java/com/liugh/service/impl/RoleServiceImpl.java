package com.liugh.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liugh.base.BusinessException;
import com.liugh.base.Constant;
import com.liugh.base.PublicResultConstant;
import com.liugh.entity.Menu;
import com.liugh.entity.Role;
import com.liugh.entity.RoleToMenu;
import com.liugh.entity.UserToRole;
import com.liugh.mapper.RoleMapper;
import com.liugh.model.RoleModel;
import com.liugh.service.IMenuService;
import com.liugh.service.IRoleService;
import com.liugh.service.IRoleToMenuService;
import com.liugh.service.IUserToRoleService;
import com.liugh.util.ComUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author liugh123
 * @since 2018-05-03
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {


    @Autowired
    private IRoleToMenuService roleToMenuService;

    @Autowired
    private IUserToRoleService userToRoleService;

    @Autowired
    private IMenuService menuService;

    @Override
    public boolean addRoleAndPermission(RoleModel roleModel) throws Exception {
        Role role = new Role();
        BeanUtils.copyProperties(roleModel, role);
        boolean result = this.save(role);
        if (!result) {
            throw new BusinessException(PublicResultConstant.UPDATE_ROLEINFO_ERROR);
        }
        result = roleToMenuService.saveAll(role.getRoleCode(), roleModel.getMenuCodes());
        return result;
    }

    @Override
    public boolean updateRoleInfo(RoleModel roleModel) throws Exception {
        if (roleModel.getRoleCode().equals(
                this.getOne(Wrappers.<Role>lambdaQuery().eq(Role::getRoleName, Constant.RoleType.SYS_ASMIN_ROLE))
                        .getRoleCode())) {
            throw new BusinessException(PublicResultConstant.UPDATE_SYSADMIN_INFO_ERROR);
        }
        Role role = this.getById(roleModel.getRoleCode());
        if (ComUtil.isEmpty(role)) {
            return false;
        }
        BeanUtils.copyProperties(roleModel, role);
        boolean result = this.updateById(role);
        if (!result) {
            throw new BusinessException(PublicResultConstant.UPDATE_ROLEINFO_ERROR);
        }
        result = roleToMenuService.remove(Wrappers.<RoleToMenu>emptyWrapper().eq("role_code", roleModel.getRoleCode()));
        if (!result) {
            throw new BusinessException("删除权限信息失败");
        }
        result = roleToMenuService.saveAll(role.getRoleCode(), roleModel.getMenuCodes());
        if (!result) {
            throw new BusinessException("更新权限信息失败");
        }
        return result;

    }

    @Override
    public void getRoleIsAdminByUserNo(String userNo) throws Exception {
        UserToRole userToRole = userToRoleService.selectByUserNo(userNo);
        Role role = this.getById(userToRole.getRoleCode());
        if (role.getRoleName().equals(Constant.RoleType.SYS_ASMIN_ROLE)) {
            throw new BusinessException(PublicResultConstant.UPDATE_SYSADMIN_INFO_ERROR);
        }
    }

    @Override
    public Map<String, Object> selectByRoleCode(String roleCode) throws Exception {
        Role role = this.getById(roleCode);
        if (ComUtil.isEmpty(role)) {
            throw new BusinessException(PublicResultConstant.INVALID_ROLE);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("role", role);
        //权限信息
        result.put("nodes", this.getMenuByRoleCode(role.getRoleCode()));
        return result;
    }

    @Override
    public void deleteByRoleCode(String roleCode) throws Exception {
        if (ComUtil.isEmpty(this.getById(roleCode))) {
            throw new BusinessException("角色不存在");
        }
        if (!ComUtil.isEmpty(
                userToRoleService.list(Wrappers.<UserToRole>lambdaQuery().eq(UserToRole::getRoleCode, roleCode)))) {
            throw new BusinessException("角色存在相关用户,请先删除相关角色的用户");
        }
        this.remove(Wrappers.<Role>lambdaQuery().eq(Role::getRoleCode, roleCode));
    }


    @Override
    public Map<String, Object> getMenuByRoleCode(String roleCode) {
        Map<String, Object> retMap = new HashMap<>();
        List<Menu> menuList = menuService.findMenuByRoleCode(roleCode);
        List<Menu> buttonList = new ArrayList<Menu>();
        List<Menu> retMenuList = menuService.treeMenuList(Constant.ROOT_MENU, menuList);
        for (Menu buttonMenu : menuList) {
            if (buttonMenu.getMenuType() == Constant.TYPE_BUTTON) {
                buttonList.add(buttonMenu);
            }
        }
        retMap.put("menuList", retMenuList);
        retMap.put("buttonList", buttonList);
        return retMap;
    }
}
