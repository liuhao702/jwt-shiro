package code.dao;

import code.entity.UserInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * ${comments}
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-11-19 11:33:56
 */
@Mapper
public interface UserInfoDao extends BaseMapper<UserInfoEntity> {
	
}
