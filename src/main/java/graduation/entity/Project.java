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
public class Project {
    private Integer id;
    private Integer ownerId;
    private String url;

    private Integer forkedFrom;

    private Date createdAt;
    private Double auth;
    private Integer starNum;

    private  Integer deleted;
    //
    private String watchers;
    private String requests;
    private String issues;


}
