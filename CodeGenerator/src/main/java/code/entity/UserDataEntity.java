package code.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Data
@TableName("user_data")
@Component
public class UserDataEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * $column.comments
     */
    private String userName;
    /**
     * $column.comments
     */
    private String userPwd;

    /**
     * $column.comments
     */
    private String permsValue;
    /**
     * $column.comments
     */
    @TableId
    private Long id;

    /**
     * $column.comments
     */
//    private List<RoleEntity> role;

}
