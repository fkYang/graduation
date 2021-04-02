package graduation.dao;

import graduation.entity.Watcher;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 类描述
 *
 * @author yfk
 * @date 2021/2/21
 */
public interface IWatcherDao {
    @Select("select * from watchers where repo_id = #{id}")
    List<Watcher> findByRepoId(Integer id);

    @Select("select * from watchers where user_id = #{id}")
    List<Watcher> findByUserId(Integer id);
}
