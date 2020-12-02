package code.service;

import code.entity.Perms;
import code.entity.RoleEntity;
import code.entity.UserDataEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.authz.Permission;

import java.util.List;

public interface UserDataService  extends IService<UserDataEntity> {


//    UserDataEntity getRole(String userName);
    UserDataEntity login(String userName,String userPwd);
    UserDataEntity findUsersByName(String userName);
    List<RoleEntity> selectAllRoles(Long userId);
    List<Perms> selectAllpermissions(Long roleId);

}
