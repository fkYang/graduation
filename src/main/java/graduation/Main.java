package graduation;

import graduation.dao.IFollowersDao;
import graduation.entity.Followers;
import graduation.service.Figure;
import graduation.service.GitApi;
import graduation.service.GitHubUserService;
import graduation.service.ProjectsService;
import graduation.util.LogUtil;
import graduation.util.MySQL;
import graduation.util.Parameter;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 类描述
 *
 * @author yfk
 * @date 2021/2/19
 */

public class Main {
    public static CountDownLatch latch= new CountDownLatch(1);
    public static void main(String[] args) throws InterruptedException {
       // LogUtil.info("test");
        // followers
     //  GitHubUserService.initPrepareParameter();
        //new Thread(GitHubUserService::initPrepareParameter).start();
//        for( int i = 1 ; i < 30 ; i++){
//            ProjectsService.fillStar(i* Parameter.star);
//        }
        //star
//        Thread temp = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                GitApi.process();
//            }
//        });
//        temp.start();
//        ProjectsService.fillStar();
//        latch.await();
//        System.exit(0);


        Figure.drawPrepare();

//        ExecutorService executorPool = ExecutorUtils.getExecutorPool();
//
//        temp();
//        for (int i = 0; i < 10; i++) {
//            executorPool.execute(new tempThread());
//        }
//        Thread.sleep(10000);
//        executorPool.shutdown();
    }
    static class tempThread extends Thread {
        @Override
        public void run() {
            temp();
            super.run();
        }
    }
    public static void temp() {
        System.out.printf("thread num :%d\n", Thread.currentThread().getId());

        IFollowersDao followersDao;
        MySQL mySQL = new MySQL();
        followersDao = mySQL.getMapper(IFollowersDao.class);
        //   followersDao = session.getMapper(IFollowersDao.class);   //代理模式

        //5. 使用代理对象执行方法
        List<Followers> users = followersDao.findByFollowerId(2);
//        for( Followers user : users){
//            System.out.println(user);
//        }
        System.out.println(users.get(0));
        System.out.printf("count:%d \n", users.size());
        System.out.printf("thread num :%d\n", Thread.currentThread().getId());
        System.out.printf("thread  :%s", Thread.currentThread());
        System.out.printf("MYSQL：%s ", mySQL);
        System.out.printf("MYSQL session ：%s", mySQL.getSession());
        mySQL.getSession().get().close();
    }
}
