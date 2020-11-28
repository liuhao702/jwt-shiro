package code.service.impl;

import code.dao.UserDataDao;
import code.entity.UserDataEntity;
import code.service.UserDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserDataServiceImpl extends ServiceImpl<UserDataDao, UserDataEntity> implements UserDataService {
}
