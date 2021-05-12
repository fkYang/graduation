package graduation.util;


/**
 * 类描述
 *
 * @author yfk
 * @date 2021/3/17
 */
public class Parameter {
    public static final int batch = 30;
    public static final int star = 35100;
    public static final int init = 0;
    public static int times = 0;
    public static double hubSum = 0;
    public static double authSum = 0;

    // 计算的参数
    public static double userRatioWatcher = 0.125;
    public static double userRatioFollower = 0.875;

    public static double projectRatioIssue = 1;
    public static double projectRatioRequest = 1;

    // 计算的最终参数
    public static int userSize = 782825 + 100;
    public static int projectSize = 1273604 + 100;

    //
    public static String HITSId = "hitsId";
    public static String HITSValue = "hitsValue";

    public static int iteratorTimes = 20;

    public static double minHitsThresholdValue = 0.01;

}
