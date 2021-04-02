package graduation.dao;

import graduation.entity.PullRequest;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 类描述
 *
 * @author yfk
 * @date 2021/2/21
 */
public interface IPullRequestDao {
    //SELECT * FROM pull_requests WHERE head_repo_id = 66183;
    @Select("select * from pull_requests where id = #{id}")
    PullRequest findById(Integer id);

    // 其他人forked仓库
    @Select("select * from pull_requests where head_repo_id = #{id}")
    List<PullRequest> findByHeadRepoId(Integer id);

    // 根
    @Select("select * from pull_requests where base_repo_id = #{id}")
    List<PullRequest> findByBaseRepoId(Integer id);
}
