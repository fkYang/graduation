package graduation.dao;

import graduation.entity.Issue;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 类描述
 *
 * @author yfk
 * @date 2021/2/21
 */
public interface IIssueDao {
    @Select("select * from issues where id = #{id}")
    Issue findById(Integer id);

    @Select("select * from issues where repo_id = #{id}")
    List<Issue> findByRepoId(Integer id);
}
