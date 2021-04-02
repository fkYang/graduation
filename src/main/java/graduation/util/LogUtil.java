package graduation.util;

import org.apache.log4j.Logger;

/**
 * 类描述
 *
 * @author yfk
 * @date 2021/3/10
 */
public class LogUtil {
    private static Logger logger = Logger.getLogger(LogUtil.class);
    public static void info(String info){
//        StringBuilder traceString = new StringBuilder();
//        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
//        for(StackTraceElement st : trace){
//            traceString.append(st.toString()).append("\n");
//        }
//        traceString.deleteCharAt(traceString.length()-1);
//        info =  info + "\n " + traceString;
       // System.out.println(info);
        logger.info(info);
    }
    public static void excep(String error, Throwable throwable){
        logger.error(error, throwable);
    }
}
