package graduation.service;

import graduation.Main;
import graduation.dao.IProjectDao;
import graduation.entity.Project;
import graduation.util.LogUtil;
import graduation.util.MySQL;
import graduation.util.Parameter;

import java.util.List;

/**
 * 类描述
 *
 * @author yfk
 * @date 2021/3/10
 */
public class ProjectsService {
    static MySQL mySQL  = new MySQL();
    static class UpdateProjectStar implements Runnable{
        static MySQL mySQL  = new MySQL();
        Project project;
        UpdateProjectStar(Project project){
            this.project = project;
        }
        @Override
        public void run() {
            System.out.println("ProjectsService run:" + Thread.currentThread().getName());
            IProjectDao projectDao = mySQL.getMapper(IProjectDao.class);
            projectDao.updateProjectStarById(project.getId(),project.getStarNum());
            mySQL.getSession().get().commit();
        }
    }


    public static void updateStar(Integer id, Integer star) {
      //  ExecutorUtils.getExecutorPool().execute();
       // System.out.println("ProjectsService run:" + Thread.currentThread().getName());
        IProjectDao projectDao = mySQL.getMapper(IProjectDao.class);
        projectDao.updateProjectStarById(id,star);
        mySQL.getSession().get().commit();

    }
        public static void delete(Integer id) {
      //  ExecutorUtils.getExecutorPool().execute();
       // System.out.println("ProjectsService run:" + Thread.currentThread().getName());
        IProjectDao projectDao = mySQL.getMapper(IProjectDao.class);
        projectDao.deleteById(id);
        mySQL.getSession().get().commit();

    }

    // 遍历获取star
    public static void fillStar() {

        IProjectDao projectDao = mySQL.getMapper(IProjectDao.class);
        // 遍历projects
        int size = Parameter.batch, id = Parameter.star,index = 0;
        while (size == 30) {
            List<Project> projects = projectDao.findProjectsById(id, size);
            size = projects.size();
            id = projects.get(projects.size()-1).getId();
            for (Project project : projects) {
                if (project.getForkedFrom() != null || project.getStarNum() != null || project.getDeleted() == 1) {
                    continue;
                }
                GitApi.addTask(project);
                 //阻塞同过线程池
                int num = GitApi.getLatch().get();
                while (num > 20){
                    try {
                        Thread.sleep(10000);
                        System.out.printf("main while num :%d\n", num);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    num = GitApi.getLatch().get();
                }
            }
            LogUtil.info("star project id:" + id );
            // System.out.printf("star project id :%d\n", id);
        }
        int num = GitApi.getLatch().get();
        while( num != 0){
            try {
                Thread.sleep(1000);
                System.out.printf("main num num :%d\n", num);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            num = GitApi.getLatch().get();
        }
        GitApi.close();
        Main.latch.countDown();
    }
}
