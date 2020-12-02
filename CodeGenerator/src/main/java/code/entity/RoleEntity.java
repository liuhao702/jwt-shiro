package code.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Data
@TableName("user_data")
@Component
public class RoleEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * $column.comments
     */
    private String role;
    /**
     * $column.comments
     */
    private List<Perms> perms;

    /**
     * $column.comments
     */
    private Long roleId;
}
