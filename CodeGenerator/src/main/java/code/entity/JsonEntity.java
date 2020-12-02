package code.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
//@TableName("ces")
@Component
public class JsonEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * $column.comments
     */
    @TableId
    private Long id;
    /**
     * $column.comments
     */
    private String value;

    /**
     * $column.comments
     */
    private String name;

    /**
     * $column.comments
     */
    private Integer age;

    private CesEntity cesEntity;

}
