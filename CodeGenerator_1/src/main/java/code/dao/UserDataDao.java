package code.dao;

import code.entity.UserDataEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDataDao extends BaseMapper<UserDataEntity>{
}
