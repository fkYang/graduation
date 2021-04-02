package graduation.util ;

import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;

import org.apache.ibatis.datasource.DataSourceFactory;
import com.alibaba.druid.pool.DruidDataSource;

/**
 * 类描述
 *
 * @author yfk
 * @date 2021/2/13
 */
public class DruidDataSourceFactory implements DataSourceFactory {
    private Properties props;

    @Override
    public DataSource getDataSource() {
        DruidDataSource dds = new DruidDataSource();
        dds.setDriverClassName(this.props.getProperty("driver"));
        dds.setUrl(this.props.getProperty("url"));
        dds.setUsername(this.props.getProperty("username"));
        dds.setPassword(this.props.getProperty("password"));
        // 其他配置可以根据MyBatis主配置文件进行配置
        dds.setInitialSize(Integer.parseInt(this.props.getProperty("initialSize")));
        dds.setMinIdle(Integer.parseInt(this.props.getProperty("minIdle")));
        dds.setMaxActive(Integer.parseInt(this.props.getProperty("maxActive")));
        dds.setTimeBetweenEvictionRunsMillis(Integer.parseInt(this.props.getProperty("timeBetweenEvictionRunsMillis")));
        //timeBetweenEvictionRunsMillis
        try {
            dds.init();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dds;
    }

    @Override
    public void setProperties(Properties props) {
        this.props = props;
    }
}
