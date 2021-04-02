import graduation.dao.*;
import graduation.entity.*;
import graduation.util.ExecutorUtils;
import graduation.util.MySQL;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * 类描述
 *
 * @author yfk
 * @date 2021/2/21
 */
public class DomainTest {
    @Test
    public void testIFollowers() throws InterruptedException {
        ExecutorService executorPool = ExecutorUtils.getExecutorPool();
        IFollowersDao followersDao;
        MySQL mySQL = new MySQL();
        followersDao = mySQL.getMapper(IFollowersDao.class);
        //   followersDao = session.getMapper(IFollowersDao.class);   //代理模式

        //5. 使用代理对象执行方法
        List<Followers> users = followersDao.findByFollowerId(2);
        System.out.println(users.get(0));
    }

    @Test
    public void testIUser() throws InterruptedException {
        ExecutorService executorPool = ExecutorUtils.getExecutorPool();
        IUserDao iUserDao;
        MySQL mySQL = new MySQL();
        iUserDao = mySQL.getMapper(IUserDao.class);
        //   followersDao = session.getMapper(IFollowersDao.class);   //代理模式
        //5. 使用代理对象执行方法
        GitHubUser user = iUserDao.findById(2);
        System.out.println(user);
    }

    @Test
    public void testIProject() throws InterruptedException {
       // ExecutorService executorPool = ExecutorUtils.getExecutorPool();
        IProjectDao iProjectDao;
        MySQL mySQL = new MySQL();
        iProjectDao = mySQL.getMapper(IProjectDao.class);
        //   followersDao = session.getMapper(IFollowersDao.class);   //代理模式
        //5. 使用代理对象执行方法
        Project project = iProjectDao.findById(2);
        project.setWatchers(null);
        project.setRequests(null);
        project.setIssues(null);
        iProjectDao.updateProjectInit(project);
        Integer integer = iProjectDao.updateProjectStarById(2, 101);
        mySQL.getSession().get().commit();


        System.out.println(integer);

    }

    @Test
    public void testWatcher() throws InterruptedException {
        ExecutorService executorPool = ExecutorUtils.getExecutorPool();
        IWatcherDao iWatcherDao;
        MySQL mySQL = new MySQL();
        iWatcherDao = mySQL.getMapper(IWatcherDao.class);
        //   followersDao = session.getMapper(IFollowersDao.class);   //代理模式
        //5. 使用代理对象执行方法
        List<Watcher> users = iWatcherDao.findByUserId(2);
        System.out.println(users);
        users = iWatcherDao.findByRepoId(2);
        System.out.println(users);
    }

    @Test
    public void testProjectMember() throws InterruptedException {
        ExecutorService executorPool = ExecutorUtils.getExecutorPool();
        IProjectMemberDao iProjectMemberDao;
        MySQL mySQL = new MySQL();
        iProjectMemberDao = mySQL.getMapper(IProjectMemberDao.class);
        //   followersDao = session.getMapper(IFollowersDao.class);   //代理模式
        //5. 使用代理对象执行方法
        List<ProjectMember> users = iProjectMemberDao.findByUserId(2);
        System.out.println(users);
        users = iProjectMemberDao.findByRepoId(2);
        System.out.println(users);
    }

    @Test
    public void testPullRequest() throws InterruptedException {
        ExecutorService executorPool = ExecutorUtils.getExecutorPool();
        IPullRequestDao iPullRequestDao;
        MySQL mySQL = new MySQL();
        iPullRequestDao = mySQL.getMapper(IPullRequestDao.class);
        //   followersDao = session.getMapper(IFollowersDao.class);   //代理模式
        //5. 使用代理对象执行方法
        PullRequest user = iPullRequestDao.findById(6350);
        System.out.println(user);
        List<PullRequest> users = iPullRequestDao.findByBaseRepoId(6350);
        System.out.println(users);
        users = iPullRequestDao.findByHeadRepoId(6350);
        System.out.println(users);
    }


    @Test
    public void testPullRequestHistory() throws InterruptedException {
        ExecutorService executorPool = ExecutorUtils.getExecutorPool();
        IPullRequestHistoryDao iPullRequestDao;
        MySQL mySQL = new MySQL();
        iPullRequestDao = mySQL.getMapper(IPullRequestHistoryDao.class);
        //   followersDao = session.getMapper(IFollowersDao.class);   //代理模式
        //5. 使用代理对象执行方法
        PullRequestHistory user = iPullRequestDao.findById(6350);
        System.out.println(user);
        List<PullRequestHistory> users = iPullRequestDao.findByActorId(2);
        System.out.println(users);
        users = iPullRequestDao.findByPullRequestId(6350);
        System.out.println(users);
    }

    @Test
    public void testIssue() throws InterruptedException {
        ExecutorService executorPool = ExecutorUtils.getExecutorPool();
        IIssueDao iIssueDao;
        MySQL mySQL = new MySQL();
        iIssueDao = mySQL.getMapper(IIssueDao.class);
        //   followersDao = session.getMapper(IFollowersDao.class);   //代理模式
        //5. 使用代理对象执行方法
        Issue user = iIssueDao.findById(6350);
        System.out.println(user);
        List<Issue> users = iIssueDao.findByRepoId(2);
        System.out.println(users);;
    }
}
