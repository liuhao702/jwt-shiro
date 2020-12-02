package code.service.impl;

import code.dao.CesDao;
import code.entity.CesEntity;
import code.service.CesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;



@Service("cesService")
public class CesServiceImpl extends ServiceImpl<CesDao, CesEntity> implements CesService {
}