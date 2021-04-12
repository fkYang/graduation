package graduation.dao;

import graduation.entity.Followers;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 类描述
 *
 * @author yfk
 * @date 2021/2/13
 */
public interface IFollowersDao {
    @Select("select * from followers where user_id = #{id}")
    List<Followers> findByFollowerId(Integer id);
    //user_id
    // follower_id
//    List<Followers> findByFollowerId(Integer id);

    // 查询id的跟随着
    @Select("select * from followers where follower_id = #{id}")
    List<Followers> findFollowersByUserId(Integer id);
}
