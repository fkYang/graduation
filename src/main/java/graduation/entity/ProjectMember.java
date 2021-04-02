package graduation.entity;

import lombok.Data;

import java.sql.Date;

/**
 * 类描述
 *
 * @author yfk
 * @date 2021/2/21
 */
@Data
public class ProjectMember {
    private Integer repoId;
    private Integer userId;

    private Date createdAt;
}
