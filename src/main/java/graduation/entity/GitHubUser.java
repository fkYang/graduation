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
public class GitHubUser {
    private Integer id;
    private String name;

    private Date createdAt;
    private Double hub;

    //
    private String followers;

}
