package graduation.service;

import graduation.Main;
import graduation.dao.IProjectDao;
import graduation.dao.IUserDao;
import graduation.entity.GitHubUser;
import graduation.entity.Project;
import graduation.util.LogUtil;
import graduation.util.MySQL;
import graduation.util.Parameter;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述
 *
 * @author yfk
 * @date 2021/3/10
 */
public class ProjectsService {
    static MySQL mySQL = new MySQL();

    static class UpdateProjectStar implements Runnable {
        static MySQL mySQL = new MySQL();
        Project project;

        UpdateProjectStar(Project project) {
            this.project = project;
        }

        @Override
        public void run() {
            System.out.println("ProjectsService run:" + Thread.currentThread().getName());
            IProjectDao projectDao = mySQL.getMapper(IProjectDao.class);
            projectDao.updateProjectStarById(project.getId(), project.getStarNum());
            mySQL.getSession().get().commit();
        }
    }


    public static void updateStar(Integer id, Integer star) {
        //  ExecutorUtils.getExecutorPool().execute();
        // System.out.println("ProjectsService run:" + Thread.currentThread().getName());
        IProjectDao projectDao = mySQL.getMapper(IProjectDao.class);
        projectDao.updateProjectStarById(id, star);
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
        IUserDao userDao = mySQL.getMapper(IUserDao.class);
        // 遍历projects
        int size = Parameter.batch, id = Parameter.star, index = 0;
        while (size == 30) {
            List<GitHubUser> users = userDao.findUsersById(id, size);
            size = users.size();
            id = users.get(users.size() - 1).getId();

            Integer[] ids = new Integer[users.size()];
            for (int i = 0; i < users.size(); i++) {
                ids[i] = users.get(i).getId();
            }
            List<Project> projects = projectDao.findByOwnerIds(ids);

         //   List<Project> projects = projectDao.findProjectsById(id, size);

            for (Project project : projects) {
                if (project.getForkedFrom() != null || project.getStarNum() != null || project.getDeleted() == 1) {
                    continue;
                }
                GitApi.addTask(project);
                //阻塞同过线程池
                int num = GitApi.getLatch().get();
                while (num > 25) {
                    try {
                        Thread.sleep(5000);
                        System.out.printf("main while num :%d\n", num);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    num = GitApi.getLatch().get();
                }
            }
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LogUtil.info("star user project id:" + id);
            // System.out.printf("star project id :%d\n", id);
        }

        int num = GitApi.getLatch().get();
        while (num != 0) {
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
