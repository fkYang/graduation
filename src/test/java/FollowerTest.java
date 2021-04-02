
import graduation.dao.IFollowersDao;
import graduation.entity.Followers;

import org.junit.Test;

import graduation.util.ExecutorUtils;
import graduation.util.MySQL;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * 类描述
 *
 * @author yfk
 * @date 2021/2/13
 */
public class FollowerTest {
//    private InputStream in;
//    private IFollowersDao userDao;
//    private SqlSession session;
//
//    @Before //测试方法之前执行
//    public void init() throws IOException {
//        //1. 读取配置文件
//        in = Resources.getResourceAsStream("SqlMapConfig.xml");
//        //2. 创建SqlSessionFactory
//        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();//构建者模式
//        SqlSessionFactory factory = builder.build(in);
//        //in：构建者
//        session = factory.openSession(true);
////        //4.获取代理对象
//        userDao = session.getMapper(IFollowersDao.class);
//    }
//
//    @After //测试方法之后执行
//    public void destory() throws IOException {
//        session.close();
//        in.close();
//    }


    @Test
    public void testFindAllRole() throws InterruptedException {
        ExecutorService executorPool = ExecutorUtils.getExecutorPool();

        temp();
        for (int i = 0; i < 10; i++) {
            executorPool.execute(new tempThread());
        }
        Thread.sleep(10000);
    }

    class tempThread extends Thread {
        @Override
        public void run() {
            temp();
            super.run();
        }
    }

    public void temp() {
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
        System.out.printf("count:%d \n", users.size());
        System.out.printf("thread num :%d\n", Thread.currentThread().getId());
        System.out.printf("thread  :%s", Thread.currentThread());
        System.out.printf("MYSQL：%s ", mySQL);
        System.out.printf("MYSQL session ：%s", mySQL.getSession());
        mySQL.getSession().get().close();
    }


}
