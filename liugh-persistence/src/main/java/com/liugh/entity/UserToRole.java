package com.liugh.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * <p>
 * 用户角色关系表
 * </p>
 *
 * @author liugh123
 * @since 2018-06-25
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@TableName("tb_user_to_role")
public class UserToRole extends Model<UserToRole> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "user_to_role_id", type = IdType.AUTO)
    private Integer userToRoleId;
    /**
     * 用户编号
     */
    @TableField("user_no")
    private String userNo;
    /**
     * 角色代号
     */
    @TableField("role_code")
    private String roleCode;

    @Override
    protected Serializable pkVal() {
        return this.userToRoleId;
    }
}
