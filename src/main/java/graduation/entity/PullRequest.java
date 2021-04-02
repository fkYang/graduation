package graduation.entity;

import lombok.Data;

/**
 * 类描述
 *
 * @author yfk
 * @date 2021/2/21
 */
@Data
public class PullRequest {
    private Integer id;
    private Integer headRepoId;
    private Integer baseRepoId;

}
