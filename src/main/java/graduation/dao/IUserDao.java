package graduation.dao;

import graduation.entity.GitHubUser;
import graduation.entity.Project;
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
public interface IUserDao {
    // 安装id查询user
    @Select("select * from users where id = #{id}")
    GitHubUser findById(Integer id);

    // 遍历user表方法
    @Select("select * from users where id > #{id} limit #{limit}")
    List<GitHubUser> findUsersById(@Param("id") Integer id, @Param("limit") Integer limit);

    // 删除user，预处理使用的函数
    @Delete("delete from users where id = #{id}")
    Integer deleteById(Integer id);

    // 删除user，预处理使用的函数0
    @Delete("delete from users where id >= #{arg0} and id < #{arg1} and (fake = 1 or deleted = 1)")
    Integer deleteUsersByIdQuery(Integer min, Integer max);

    @Update("update users set hub = #{arg1} where id = #{arg0};")
    Integer updateUserHubById(Integer id, Double hub);

    @Update("update users set followers = #{user.followers} where id = #{user.id};")
    Integer updateUserInit(@Param("user") GitHubUser user);
}
