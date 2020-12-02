package code.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.apache.shiro.authz.Permission;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@TableName("user_data")
@Component
public class Perms implements Serializable , Permission {

    private static final long serialVersionUID = 1L;

    /**
     * $column.comments
     */
    private String perms;

    /**
     * $column.comments
     */
    private Long permsId;

    @Override
    public boolean implies(Permission permission) {
        return false;
    }
}
