import lombok.Data;

/**
 * 类描述
 *
 * @author yfk
 * @date 2021/3/9
 */
@Data
public class Repos {
    private String name;
    private String full_name;
    private int stargazers_count;
    private int watchers_count;
    private String language;
    private int forks_count;
    private int open_issues_count;
    private int forks;
    private int open_issues;
    private int watchers;
    private String default_branch;
}