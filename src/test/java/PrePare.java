import graduation.entity.Project;
import graduation.service.GitApi;
import graduation.util.TransUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述
 *
 * @author yfk
 * @date 2021/3/10
 */
//Id	User	Host	db	Command	Time	State	Info
//1011	root	119.112.123.126:11420	ghtorrent	Query	49	updating	delete from users where id >= 0 and id < 10000 and (fake = 1 or deleted = 1)
public class PrePare {
    public static void main(String[] args) {
       //ProjectsService.fillStar();
       // new Thread(GitHubUserService::temp).start();
//temp();
        test();
        long startTime = System.currentTimeMillis ();
        //test();
        long endTime = System.currentTimeMillis();    //获取结束时间

        System.out.println("程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间
    }


    // testHitsPrepareData
    static void test(){
        Project project = new Project();
        project.setUrl("https://api.github.com/repos/anthavio/anthavio-xml");
        String url = project.getUrl();
        url = url.replaceAll("api.", "").replaceAll("repos/", "");
        project.setUrl(url);
        GitApi.HttpSendApi api = new GitApi.HttpSendApi(project);
        api.run();
//        InitProcess process = new InitProcess();
//        GitHubUser user = new GitHubUser();
//        user.setId(2);
//        process.initUser(user);
    }

    public  static void temp(){
        List<Integer> ids = new ArrayList<>();
        ids.add(1);
        ids.add(2);
        ids.add(3);
        ids.remove(new Integer(1));
      //  Integer[] s = ids.toArray(new Integer[ids.size()]);
        //String str = "测试1,测试2，测试3，测试4";
        //此处为了将字符串中的空格去除做了一下操作
        //List<String> list= Arrays.asList(str .split(",")).stream().map(s -> (s.trim())).collect(Collectors.toList());
        //list<String>转字符串（以逗号隔开）
        String s = TransUtil.List2String(ids);
        List<Integer> list = TransUtil.string2List(s);
        System.out.println(list.size());
        //List<Integer> ids2 = Arrays.asList(s);
    }

}

/*
*         CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
        httpclient.start();
        for ( int i = 0 ; i< 100 ; i++){
            String url = "https://cn.bing.com/";
            HttpGet request = new HttpGet(url);
            httpclient.execute(request, new FutureCallback<HttpResponse>() {
                @Override
                public void completed(HttpResponse httpResponse) {
                    System.out.printf("completed :%s\n", Thread.currentThread().getName());
                }

                @Override
                public void failed(Exception e) {
                    System.out.printf("failed:%s\n", Thread.currentThread().getName());
                }

                @Override
                public void cancelled() {
                    System.out.printf("cancelled:%s\n", Thread.currentThread().getName());
                }
            });
        }
        try {
            Thread.sleep(50000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            httpclient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/




//        IUserDao iUserDao;
//        MySQL mySQL = new MySQL();
//        iUserDao = mySQL.getMapper(IUserDao.class);
//        int min = 0;
//        int length = 100;
//        int sum = 0, batchCommit=0;
//        for(min = 0 ; min < 9525036; min = min + length){
//            //Integer integer = 0;
//            Integer integer = iUserDao.deleteUsersByIdQuery(min, min + length);
//            sum += integer;
//            if (min > batchCommit + 1000000){
//                mySQL.getSession().get().commit();
//            }
//            LogUtil.info(String.format("id :%d, sum:%d \n",min, sum));
//        }
//        mySQL.getSession().get().commit();