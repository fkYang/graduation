package graduation.method;

import graduation.Main;
import graduation.dao.IProjectDao;
import graduation.dao.IUserDao;
import graduation.entity.GitHubUser;
import graduation.entity.Project;
import graduation.util.*;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * 类描述
 *
 * @author yfk
 * @date 2021/3/14
 */
public class IteratorProcess implements Runnable{
    static MySQL mySQL  = new MySQL();

    public double userProcess(GitHubUser user1) {
        Parameter.authSum = Parameter.hubSum = 0;
        ExecutorService pool = ExecutorUtils.getExecutorPool();
        IUserDao userDao = mySQL.getMapper(IUserDao.class);
        // 遍历user
        int size = Parameter.batch, id = Parameter.init, index = 0;
        while(true){
            // 计算本轮
            while (size == 30) {
                List<GitHubUser> users = userDao.findUsersById(id, size);
                //size = users.size();
                id = users.get(users.size() - 1).getId();
                for (GitHubUser user : users) {
                    //break;
                    if(user.getFollowers() == null){
                        // 没有的需要加入
                        InitProcess process = new InitProcess(user);
                        pool.execute(process);
                    }
                }
                LogUtil.info("PrepareParameter id:" + id );
            }
            // 等待线程全部跑完

            // 归一化
            while (size == 30) {
                List<GitHubUser> users = userDao.findUsersById(id, size);
                //size = users.size();
                id = users.get(users.size() - 1).getId();
                for (GitHubUser user : users) {
                    //break;
                    if(user.getFollowers() == null){
                        // 没有的需要加入
                        InitProcess process = new InitProcess(user);
                        pool.execute(process);
                    }
                }
                LogUtil.info("PrepareParameter id:" + id );
            }
            if(Parameter.times == 1){
                break;
            }
        }

        Main.latch.countDown();
        return 0;
    }
    public double initHits(GitHubUser user) {
        double sum;
        int follower,watchers = 0;
        IProjectDao projectDao = mySQL.getMapper(IProjectDao.class);
        List<Project> projects = projectDao.findByOwnerId(user.getId());
        follower = TransUtil.string2List( user.getFollowers()).size();
        for (Project project:projects){
            double auth = 0;
            int prh ,issue , watcher;
            prh = TransUtil.string2List( project.getRequests()).size();
            issue = TransUtil.string2List( project.getRequests()).size();

            auth = prh + issue ;
            Parameter.hubSum += Math.pow(auth,2);

            watcher = TransUtil.string2List( project.getRequests()).size();
            watchers += watcher;
        }
        sum = watchers + follower;
        Parameter.hubSum += Math.pow(sum,2);
        return  sum;
    }

    public double hitsNormalization(GitHubUser user){
        double hub = user.getHub()/Math.sqrt(Parameter.hubSum);

        IProjectDao projectDao = mySQL.getMapper(IProjectDao.class);
        List<Project> projects = projectDao.findByOwnerId(user.getId());
        for (Project project:projects){
            double auth = project.getAuth()/Math.sqrt(Parameter.authSum);
        }
        return 0;
    }
    public double initProjectAuth(Project project) {
        return 0;
    }
    public double iteratorHits(GitHubUser user) {
        return 0;

    }


    @Override
    public void run() {

    }
}
