package code.dao;

import code.entity.Perms;
import code.entity.RoleEntity;
import code.entity.UserDataEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.authz.Permission;

import java.util.List;

@Mapper
public interface UserDataDao extends BaseMapper<UserDataEntity>{

    UserDataEntity getRole1(@Param("userName") String userName);
    UserDataEntity findUsersByName(@Param("username") String userName);
    UserDataEntity login(@Param("username") String userName,@Param("userPwd") String userPwd);
    List<RoleEntity> selectAllRoles(@Param("userId") Long userId);
    List<Perms> selectAllpermissions(@Param("roleId") Long roleId);



}
