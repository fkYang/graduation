package graduation.service;

import com.github.kevinsawicki.http.HttpRequest;
import graduation.entity.Project;
import graduation.util.ExecutorUtils;
import graduation.util.LogUtil;
import graduation.util.MySQL;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.config.Lookup;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.util.PublicSuffixMatcher;
import org.apache.http.conn.util.PublicSuffixMatcherLoader;
import org.apache.http.cookie.CookieSpecProvider;;
import org.apache.http.impl.cookie.RFC6265CookieSpecProvider;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import sun.net.www.http.HttpClient;


import java.io.IOException;
import java.net.URL;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 类描述
 *
 * @author yfk
 * @date 2021/3/10
 */
public class GitApi {

    static public void close() {
        //   executorPool.shutdown();
        GitApi.run = false;
        executorHttpPool.shutdown();
//        try {
//        //    httpclient.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    static boolean run = true;


    // static ExecutorService executorPool;
    static ExecutorService executorHttpPool;

    public static void addTask(Project project) {
        String url = project.getUrl();
        url = url.replaceAll("api.", "").replaceAll("repos/", "");
        project.setUrl(url);
        new Api(project).run();
//        HttpSendApi api = new HttpSendApi(project);
//        api.run();
        // executorHttpPool.execute(new HttpSendApi(project));
        // executorPool.execute(new HttpSendApi(project));
//        try {
//            projectBlockingDeque.put(project);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    static BlockingDeque<Project> projectBlockingDeque = new LinkedBlockingDeque<>(10);

    public static void process() {
        while (run) {
            try {
                Project project = projectBlockingDeque.take();
                executorHttpPool.execute(new Api(project));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static {
        executorHttpPool = ExecutorUtils.getHttpExecutorPool();
        System.out.println("init succeed");
    }


//    public static void main(String[] args) throws InterruptedException {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                GitApi.process();
//            }
//        }).start();
//        for (int i = 0; i < 100; i++) {
//            String url1 = "https://cn.bing.com/";
//            Project project1 = new Project();
//            project1.setUrl(url1);
//            GitApi.addTask(project1);
//        }
//        int value = 0;
//        while ((value = latch.get()) != 0) {
//            Thread.sleep(1000);
//            System.out.printf("main sleep :%d\n", value);
//        }
//    }

    public static AtomicInteger getLatch() {
        return latch;
    }

    static AtomicInteger latch = new AtomicInteger();

    public static class HttpSendApi implements Runnable {
        // static MySQL mySQL = new MySQL();
        Project project;

        public HttpSendApi(Project project) {
            this.project = project;
            //         latch.addAndGet(1);
//            System.out.print("HttpSendApi name:" + Thread.currentThread().getName());
        }

        @Override
        public void run() {
            HttpRequest request;
            try {
                //request.re
                //         latch.addAndGet(-1);
                request = HttpRequest.get(project.getUrl());

//                request.connectTimeout(10*1000);
//                request.readTimeout(10*1000);
                if (request.code() == 200) {
                    String response = request.body();

                    System.out.println("HttpSendApi run:" + Thread.currentThread().getName());
                    // System.out.println("->" + httpResponse.getStatusLine());
                    Document document = Jsoup.parse(response);
                    Elements elements = document.getElementsByClass("social-count js-social-count");
                    String attr = elements.attr("aria-label");
                    String[] s = attr.split(" ");
                    int star = 0;
                    try {
                        star = Integer.parseInt(s[0]);
                    } catch (Exception e) {
                        LogUtil.info(" 空字符串：" + project.getId());
                    }
                    project.setStarNum(star);
                    LogUtil.info(String.format("projectid:%d, star:%d", project.getId(), star));
                    System.out.println("projectid:" + project.getId() + ",star:" + star);
                    ProjectsService.updateStar(project.getId(), project.getStarNum());
                    //executorPool.execute(new ProjectsService.UpdateProjectStar(project));
                } else {
                    ProjectsService.delete(project.getId());
                    // project.setStarNum(-1);
                    // executorPool.execute(new ProjectsService.UpdateProjectStar(project));
                    System.out.printf("getStatusCode err:%d , project:%d, url:%s\n", request.code(), project.getId(), project.getUrl());
                }
            } catch (Exception e) {
                System.out.printf("err:%s , project:%d,url:%s\n", e.toString(), project.getId(), project.getUrl());
            } finally {


                latch.addAndGet(-1);
            }

            //
        }
    }


    static class Api implements Runnable {
        static final ThreadLocal<CloseableHttpAsyncClient> httpclient = new ThreadLocal<>();

        void initApi() {
            PublicSuffixMatcher publicSuffixMatcher = null;
            try {
                publicSuffixMatcher = PublicSuffixMatcherLoader.load(
                        new URL("https://publicsuffix.org/list/effective_tld_names.dat"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            RFC6265CookieSpecProvider cookieSpecProvider = new RFC6265CookieSpecProvider(publicSuffixMatcher);
            Lookup<CookieSpecProvider> cookieSpecRegistry = RegistryBuilder.<CookieSpecProvider>create()
                    .register(CookieSpecs.DEFAULT, cookieSpecProvider)
                    .register(CookieSpecs.STANDARD, cookieSpecProvider)
                    .register(CookieSpecs.STANDARD_STRICT, cookieSpecProvider)
                    .build();
            RequestConfig defaultRequestConfig = RequestConfig.custom()
                    .setSocketTimeout(30000)
                    .setConnectTimeout(30000)
                    .setConnectionRequestTimeout(30000)
                    .build();
            CloseableHttpAsyncClient build = HttpAsyncClientBuilder.create().
                    setDefaultCookieSpecRegistry(cookieSpecRegistry).
                    setDefaultRequestConfig(defaultRequestConfig).build();
            httpclient.set(build);
            httpclient.get().start();
        }

        Project project;

        Api(Project project) {
            this.project = project;

        }

        @Override
        public void run() {
            if (httpclient.get() == null) {
                initApi();
            }
            HttpGet request = new HttpGet(project.getUrl());
            httpclient.get().execute(request, new SendApi(project));
        }
    }

    static class SendApi implements FutureCallback<HttpResponse> {
        static MySQL mySQL = new MySQL();

        Project project;

        SendApi(Project project) {
            this.project = project;
            latch.addAndGet(1);
            //System.out.printf("thread:%s \n", Thread.currentThread().getName());
        }

        @Override
        public void completed(HttpResponse httpResponse) {
            System.out.println("complete");
            latch.addAndGet(-1);
            HttpEntity resEntity = httpResponse.getEntity();
            if (httpResponse.getStatusLine().getStatusCode() == 200 && resEntity != null) {
                try {
                    String data = EntityUtils.toString(resEntity, "utf-8");
                    Document document = Jsoup.parse(data);
                    Elements elements = document.getElementsByClass("social-count js-social-count");
                    String attr = elements.attr("aria-label");
                    String[] s = attr.split(" ");
                    int star = 0;
                    try {
                        if (!s[0].equals("")) {
                            star = Integer.parseInt(s[0]);
                        }
                    } catch (NumberFormatException e) {

                    }
                    project.setStarNum(star);
                    System.out.println(String.format("userid:%d ,projectId:%d, star:%d", project.getOwnerId(), project.getId(), star));
                    //LogUtil.info();
                    ProjectsService.updateStar(project.getId(), project.getStarNum());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                if (httpResponse.getStatusLine().getStatusCode() == 404){
                    ProjectsService.delete(project.getId());
                }
                System.out.printf("getStatusCode err:%d , project:%d\n", httpResponse.getStatusLine().getStatusCode(), project.getId());

            }
        }

        @Override
        public void failed(Exception e) {
            //       latch1.countDown();
            // ProjectsService.delete(project.getId());
            LogUtil.excep(String.format("projects:%s is failed , project:%d\n", project.getId(), project.getId()), e);
            latch.addAndGet(-1);
        }

        @Override
        public void cancelled() {
            //  ProjectsService.delete(project.getId());
            //       latch1.countDown();
            LogUtil.info(String.format("projects:%s is cancelled\n", project.toString()));
            latch.addAndGet(-1);
        }
    }
}


//  static CountDownLatch latch1 = new CountDownLatch(1);
//    public static void oneReuest(){
//
//        try {
//            String url = "https://api.github.com/repos/kennethkalmer/ruote-kit";
//            url = url.replaceAll("api.", "").replaceAll("repos/", "");
//            System.out.print(url+" :url \n");
//            HttpGet request = new HttpGet(url);
//            httpclient.execute(request, new SendApi());
//
//            // One most likely would want to use a callback for operation result
////            for (int i = 0 ; i < 100 ; i++){
////                HttpGet request = new HttpGet("https://github.com/kennethkalmer/ruote-kit");
////                httpclient.execute(request, new SendApi());
////                Thread.sleep(1000);
////            }
//            try {
//                latch1.await();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        } finally {
//            try {
//                httpclient.close();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//    }
