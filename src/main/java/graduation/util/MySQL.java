package graduation.util;


import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;


/**
 * 类描述
 *
 * @author yfk
 * @date 2021/2/13
 */
public class MySQL {
    static SqlSessionFactory factory;
    private final static ThreadLocal<SqlSession> session = new ThreadLocal<>();

    public static SqlSessionFactory getFactory() {
        return factory;
    }

    public ThreadLocal<SqlSession> getSession() {
        return session;
    }

    static {
        //1. 读取配置文件
        InputStream in = null;
        try {
            in = Resources.getResourceAsStream("SqlMapConfig.xml");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //2. 创建SqlSessionFactory
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();//构建者模式
         factory = builder.build(in);                  //in：构建者
//        //3. 使用工厂创建SqlSession对象
//         session = factory.openSession() ;            //工厂模式·
        session.set(factory.openSession());
    }

    public MySQL() {
        if(session.get() == null){
            System.out.println("start mysql");
            session.set(factory.openSession());
        }
    }
    public  <T> T getMapper(Class<T> var1){
        if (session.get() == null){
            session.set(factory.openSession());
        }
        return session.get().getMapper(var1);
    }
}
