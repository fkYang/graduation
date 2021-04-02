package graduation.dao;

import graduation.entity.PullRequestHistory;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 类描述
 *
 * @author yfk
 * @date 2021/2/21
 */
public interface IPullRequestHistoryDao {
    // 查询request人
    @Select("select * from pull_request_history where actor_id = #{id}")
    List<PullRequestHistory> findByActorId(Integer id);

    @Select("select * from users where id = #{id}")
    PullRequestHistory findById(Integer id);

    // 查询request记录的id
    @Select("select * from pull_request_history where pull_request_id = #{id}")
    List<PullRequestHistory> findByPullRequestId(Integer id);

    @Select("<script>"  +
            "select * from pull_request_history where pull_request_id in ( "
            + " <foreach collection=\"array\" item=\"id\" index=\"index\" separator=\",\">" +
            "#{id}" +
            " </foreach>" +") "
            + "</script>")
    //@Select("select * from pull_request_history where pull_request_id in(#{array})")
    List<PullRequestHistory> findByPullRequestIds(Integer[] array);
    //and i.approve_status IN
    //        <foreach item="status" collection="approveStatus.split(',')" open="(" separator="," close=")">
    //            #{status}
    //        </foreach>

    @Select("SELECT * FROM pull_request_history WHERE pull_request_id IN (\n" +
            "SELECT pull_request_id FROM pull_request_history WHERE pull_request_id IN ( SELECT id FROM pull_requests WHERE base_repo_id = #{id}) AND ACTION = 'merged'\n" +
            ") AND ACTION = 'opened'")
    List<PullRequestHistory> findByPullRequestIdsAndActions(Integer id);
}
