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
public class PullRequestHistory {
    private Integer id;
    private Integer actorId;
    private Integer pullRequestId;
    private  String action;

    private Date createdAt;

}
