package graduation;

import graduation.dao.IFollowersDao;
import graduation.dto.MethodDto;
import graduation.dto.PrepareFigureDto;
import graduation.entity.Followers;
import graduation.method.HITS;
import graduation.service.Figure;
import graduation.service.GitApi;
import graduation.service.GitHubUserService;
import graduation.service.ProjectsService;
import graduation.util.FileUtil;
import graduation.util.LogUtil;
import graduation.util.MySQL;
import graduation.util.Parameter;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 类描述
 *
 * @author yfk
 * @date 2021/2/19
 */

public class Main {
    public static CountDownLatch latch= new CountDownLatch(1);
    public static void main(String[] args) throws InterruptedException {
        // hits计算
     //   HITS.hits();

        // 绘制图片
    //    Figure.hitsFigure();


        // followers
       //GitHubUserService.initPrepareParameter();
        //new Thread(GitHubUserService::initPrepareParameter).start();
//        for( int i = 1 ; i < 30 ; i++){
//            ProjectsService.fillStar(i* Parameter.star);
//        }
        //star
//        Thread temp = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                GitApi.process();
//            }
//        });
//        temp.start();


//        ProjectsService.fillStar();
//        latch.await();
//        System.exit(0);


        // 数据整理存储
        //igure.drawPrepare();
        MethodDto dto = FileUtil.readMethodDtoObject(".\\data\\10\\methodDto10.txt");
       // MethodDto dto = new MethodDto();
        FileUtil.writeUserFollow(dto, null);


//        System.exit(0);
    }
}
