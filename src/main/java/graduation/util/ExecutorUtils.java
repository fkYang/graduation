package graduation.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 类描述
 *
 * @author yfk
 * @date 2020/7/3
 */
public class ExecutorUtils {


    private static final ExecutorService executorService ;//= Executors.newFixedThreadPool(15);
    private static final ExecutorService executorHttpService;
    static {
        executorService = new ThreadPoolExecutor(40,
                60, 60, TimeUnit.SECONDS,new ArrayBlockingQueue<>(1000),new ThreadPoolExecutor.CallerRunsPolicy());
        executorHttpService = new ThreadPoolExecutor(10,
                10, 60, TimeUnit.SECONDS,new ArrayBlockingQueue<>(60),new ThreadPoolExecutor.CallerRunsPolicy());
    }
    static public ExecutorService getExecutorPool(){
        return executorService;
    }
    static public ExecutorService getHttpExecutorPool(){
        return executorHttpService;
    }



}
