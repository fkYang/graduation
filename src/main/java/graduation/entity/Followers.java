package graduation.entity;

import lombok.Data;
import java.io.Serializable;
import java.sql.Date;


/**
 * 类描述
 *
 * @author yfk
 * @date 2021/2/13
 */
@Data
public class Followers implements Serializable {
    private Integer userId;
    private Integer followerId;
    private Date createdAt;
}
