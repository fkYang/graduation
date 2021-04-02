import graduation.dao.IFollowersDao;
import graduation.dao.IProjectDao;
import graduation.entity.Followers;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 类描述
 *
 * @author yfk
 * @date 2020/5/27
 */
public class MybatisTest {
    /*
    * 入门Demo
    * */
    public static void main(String[] args) throws IOException {
        //simple：
       // Resources.getResourceAsStream("SqlMapConfig.xml").getMapper(IUserDao.class);
        //1. 读取配置文件
        InputStream in = Resources.getResourceAsStream("SqlMapConfig.xml");

        //2. 创建SqlSessionFactory
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();//构建者模式
        SqlSessionFactory factory = builder.build(in);                  //in：构建者

        //3. 使用工厂创建SqlSession对象
        SqlSession session = factory.openSession() ;            //工厂模式·

//        //4. 使用 SqlSession创建Dao接口的代理对象
//        IFollowersDao followersDao = session.getMapper(IFollowersDao.class);   //代理模式
        IProjectDao projectDao = session.getMapper(IProjectDao.class);   //代理模式
        projectDao.updateProjectStarById(2, 10);

//
//        //5. 使用代理对象执行方法
//        List<Followers> users = followersDao.findByFollowerId(2);
//        for( Followers user : users){
//            System.out.println(user);
//        }

        //6. 释放资源
        session.close();
        in.close();

    }
}
