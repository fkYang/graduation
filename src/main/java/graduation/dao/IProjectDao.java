package graduation.dao;

import graduation.entity.Project;
import graduation.entity.PullRequestHistory;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 类描述
 *
 * @author yfk
 * @date 2021/2/21
 */
public interface IProjectDao {
    @Select("select * from projects where id = #{id}")
    Project findById(Integer id);

//    @Select("select * from projects where owner_id = #{id}")
//    List<Project> findByForkedFromId(Integer id);

    @Select("select * from projects where owner_id = #{id};")
    List<Project> findByOwnerId(Integer id);
// 数组查找减少时间
    @Select("<script>"  +
            "select * from projects where owner_id in ( "
            + " <foreach collection=\"array\" item=\"id\" index=\"index\" separator=\",\">" +
            "#{id}" +
            " </foreach>" +") "
            + "</script>")
        //@Select("select * from pull_request_history where pull_request_id in(#{array})")
    List<Project> findByOwnerIds(Integer[] array);

    @Select("select * from projects where id > #{arg0} limit #{arg1};")
    List<Project> findProjectsById(Integer id, Integer limit);

    @Select("select * from projects where id > #{arg0}  limit #{arg1};")
    List<Project> findProjectsByIdInit(Integer id, Integer limit);

    @Update("update projects set star_num = #{arg1} where id = #{arg0};")
    Integer updateProjectStarById(Integer id, Integer star);

    @Update("update projects set auth = #{arg1} where id = #{arg0};")
    Integer updateProjectAuthById(Integer id, Double auth);

    // 删除
    @Delete("delete from projects where id = #{id}")
    Integer deleteById(Integer id);

// 待定
    @Update("update projects set watchers = #{project.watchers},requests = #{project.requests},issues = #{project.issues} where id = #{project.id};")
    Integer updateProjectInit(@Param("project") Project project);


}
