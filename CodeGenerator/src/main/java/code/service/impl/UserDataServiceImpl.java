package code.service.impl;

import code.dao.UserDataDao;
import code.entity.Perms;
import code.entity.RoleEntity;
import code.entity.UserDataEntity;
import code.service.UserDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.shiro.authz.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDataServiceImpl extends ServiceImpl<UserDataDao, UserDataEntity> implements UserDataService {

    @Autowired
    UserDataDao userDataDao;

//    @Override
//    public UserDataEntity getRole(String userName) {
//        return userDataDao.getRole(userName);
//    }

    @Override
    public UserDataEntity login(String userName, String userPwd) {
        return userDataDao.login(userName,userPwd);
    }

    @Override
    public UserDataEntity findUsersByName(String userName) {
        return userDataDao.findUsersByName(userName);
    }

    @Override
    public List<RoleEntity> selectAllRoles(Long userId) {
        return userDataDao.selectAllRoles(userId);
    }

    @Override
    public List<Perms> selectAllpermissions(Long roleId) {
        return userDataDao.selectAllpermissions(roleId);
    }
}
