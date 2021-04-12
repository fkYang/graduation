package graduation.service;

import graduation.Main;
import graduation.dao.IUserDao;
import graduation.entity.GitHubUser;
import graduation.method.InitProcess;
import graduation.util.ExecutorUtils;
import graduation.util.LogUtil;
import graduation.util.MySQL;
import graduation.util.Parameter;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * 类描述
 *
 * @author yfk
 * @date 2021/3/10
 */
public class GitHubUserService {
    // 遍历user
    static MySQL mySQL  = new MySQL();
    public static  void temp(){
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("end ");
    }
    public static void initPrepareParameter() {
        ExecutorService pool = ExecutorUtils.getExecutorPool();
        IUserDao userDao = mySQL.getMapper(IUserDao.class);
        // 遍历projects
        int size = Parameter.batch, id = Parameter.init, index = 0;
        while (size == Parameter.batch) {
            List<GitHubUser> users = userDao.findUsersById(id, size);
            size = users.size();
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
          //  break;


//            if(id > 9524030) {
//                id = Parameter.init;
//                }
        }
        System.out.println("END");
        Main.latch.countDown();
    }
    public static void initHits() {
        ExecutorService pool = ExecutorUtils.getExecutorPool();
        IUserDao userDao = mySQL.getMapper(IUserDao.class);
        // 遍历projects
        int size = Parameter.batch, id = Parameter.init, index = 0;
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
            if(id > 9524030) {
                id = Parameter.init;
            }
        }
        System.out.println("END");
        Main.latch.countDown();
    }
    public static void iteratorHits() {
        ExecutorService pool = ExecutorUtils.getExecutorPool();
        IUserDao userDao = mySQL.getMapper(IUserDao.class);
        // 遍历projects
        int size = Parameter.batch, id = Parameter.init, index = 0;
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
            if(id > 9524030) {
                id = Parameter.init;
            }
        }
        System.out.println("END");
        Main.latch.countDown();
    }
}
