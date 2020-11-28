package code.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.sql.Timestamp;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * ${comments}
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2020-11-19 11:33:56
 */
@Data
@TableName("user_info")
@Component
public class UserInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * $column.comments
	 */
	private String userName;
	/**
	 * $column.comments
	 */
	private Integer userAge;
	/**
	 * $column.comments
	 */
	@TableId
	private Long id;
	/**
	 * 创建时间
	 */
	private Timestamp createTime;
	/**
	 * 更新时间
	 */
	private Timestamp updateTime;
	/**
	 * 版本
	 */
	private Long version;
	/**
	 * 逻辑删除
	 */
	private Integer deleted;

}
