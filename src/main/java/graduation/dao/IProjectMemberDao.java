package graduation.dao;

import graduation.entity.ProjectMember;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 类描述
 *
 * @author yfk
 * @date 2021/2/21
 */
public interface IProjectMemberDao {
    // 根据仓库查询成员
    @Select("select * from project_members where repo_id = #{id}")
    List<ProjectMember> findByRepoId(Integer id);

    // 根据用户id，查询项目
    @Select("select * from project_members where user_id = #{id}")
    List<ProjectMember> findByUserId(Integer id);
}
