package graduation.method;

import graduation.dao.*;
import graduation.entity.GitHubUser;
import graduation.entity.Project;

import graduation.util.MySQL;
import graduation.util.Parameter;

import java.util.List;

/**
 * 类描述
 *
 * @author yfk
 * @date 2021/3/17
 */
public class Method {
    static MySQL mySQL  = new MySQL();
     MethodProcess process;
     Method(MethodProcess process){
         this.process = process;
     }
     void init(){
//         Method method = new Method(new InitProcess());
//         method.calculateUserHub();
//         method.calculateProjectAuth();
     }
     void iterator(){
//         Method method = new Method(new IteratorProcess());
//         method.calculateUserHub();
//         method.calculateProjectAuth();
     }
    public void setProcess(MethodProcess process) {
        this.process = process;
    }

     public void calculateUserHub(){
         // user
         IUserDao userDao = mySQL.getMapper(IUserDao.class);
         // 遍历projects
         int size = Parameter.batch, id = 0, index = 0;
         //init
         double hubSum = 0;
         while (size == 30) {
             List<GitHubUser> users = userDao.findUsersById(id, size);
             size = users.size();
             id = users.get(users.size() - 1).getId();
             for (GitHubUser user : users) {
                 double hub = process.userProcess(user);
                 hubSum += Math.pow(hub, 2);
                 userDao.updateUserHubById(user.getId(), hub);
             }
             break;
         }
         id = 0;
         while (size == 30) {
             List<GitHubUser> users = userDao.findUsersById(id, size);
             size = users.size();
             id = users.get(users.size() - 1).getId();
             for (GitHubUser user : users) {
                 double hub = user.getHub() / hubSum;
                 System.out.printf("userid:%d, hub:%f \n", user.getId(),hub);
                 // userDao.updateUserHubById(user.getId(), hub);
             }
             break;
         }

     }
    public void calculateProjectAuth(){
        IProjectDao projectDao = mySQL.getMapper(IProjectDao.class);
        // 遍历projects
        int size = Parameter.batch, id = 1522, index = 0;
        //init

        while (size == 30) {

            List<Project> projects = projectDao.findProjectsById(id, size);
            size = projects.size();
            id = projects.get(projects.size() - 1).getId();
            for (Project project : projects) {

            }
            break;
        }

    }
     public void user(GitHubUser user){
         process.userProcess(user);
     }
     public void project(){

     }

}
