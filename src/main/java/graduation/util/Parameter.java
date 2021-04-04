package graduation.util;

/**
 * 类描述
 *
 * @author yfk
 * @date 2021/3/17
 */
public class Parameter {
    public static final int batch = 30;
    public static final int star = 780000;
    public static final int init = 9524030;
    public static int times = 0;
    public static double hubSum = 0;
    public static double authSum = 0;

    // 计算的参数
    public  static double userRatioWatcher = 1;
    public  static double userRatioFollower = 1;

    public  static double projectRatioIssue = 1;
    public  static double projectRatioRequest = 1;

    // 计算的最终参数
    public static int userSize =   782825 +100;
    public static int projectSize =   1273604 +100 ;


}
