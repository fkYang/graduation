package graduation.method;

import graduation.dao.*;
import graduation.entity.*;
import graduation.util.LogUtil;
import graduation.util.MySQL;
import graduation.util.TransUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类描述
 *
 * @author yfk
 * @date 2021/3/14
 */
public class InitProcess  implements Runnable {
    GitHubUser user;
    public InitProcess(GitHubUser user){
        this.user = user;
    }
    @Override
    public void run() {
        initUser(user);
    }
   static MySQL mySQL  = new MySQL();

    public void initUser(GitHubUser user) {
        try {
            //       long startTime = System.currentTimeMillis ();
            IUserDao userDao = mySQL.getMapper(IUserDao.class);
            IFollowersDao followersDao = mySQL.getMapper(IFollowersDao.class);
            IProjectDao projectDao = mySQL.getMapper(IProjectDao.class);

            // followers
            List<Followers> followers = followersDao.findFollowersByUserId(user.getId());
            List<Integer> followerList = new ArrayList<>(followers.size());
            for(Followers follower:followers){
                if (follower.getUserId() != null && !follower.getUserId().equals(user.getId())){
                    followerList.add(follower.getUserId());
                }
            }
            // 移除自身
            followerList.remove(user.getId());
            user.setFollowers(TransUtil.List2String(followerList));


//            List<Project> projects = projectDao.findByOwnerId(user.getId());
//            for (Project project : projects) {
//                projectsProcess(project);
//            }
            userDao.updateUserInit(user);
            mySQL.getSession().get().commit();
            //System.out.println( user.getId()+" follower  size : " +  followerList.size() + "   projects size: " + projects.size());
            System.out.println( user.getId()+" follower  size : " +  followerList.size() );


            //printList(user.getId() + " - follower " ,followerList);
        } catch (Exception e){
            LogUtil.excep("init user error", e);
        }

        //System.out.println(1);
//        long endTime = System.currentTimeMillis();    //获取结束时间
 //       System.out.println("程序运行时间：" + (endTime - startTime) + "ms");    //输出程序运行时间

    }

    public void projectsProcess(Project project) {
        if (project.getForkedFrom() != null || project.getDeleted() == 1) {
            // 非原创
            return ;
        }
        // watcher
        IWatcherDao watcherDao = mySQL.getMapper(IWatcherDao.class);
        List<Watcher> watchers = watcherDao.findByRepoId(project.getId());
        List<Integer> watchersList = new ArrayList<>(watchers.size());
        for(Watcher user:watchers){
            watchersList.add(user.getUserId());
        }

        // prh
        IPullRequestDao pullRequestDao = mySQL.getMapper(IPullRequestDao.class);
        IPullRequestHistoryDao pullRequestHistoryDao = mySQL.getMapper(IPullRequestHistoryDao.class);
        List<Integer> prh = new ArrayList<>();
        List<PullRequest> pullRequests = pullRequestDao.findByBaseRepoId(project.getId());
        if (pullRequests.size() != 0) {
            List<Integer> pr = new ArrayList<>(pullRequests.size());
            for (PullRequest pullRequest : pullRequests) {
                pr.add(pullRequest.getId());
            }
            // find [] prh  where id in ids
            List<PullRequestHistory> historys = pullRequestHistoryDao.findByPullRequestIds(pr.toArray(new Integer[pullRequests.size()]));
            // action = merged记录
            List<Integer> merged = new ArrayList<>(historys.size());
            // action = opened records
            Map<Integer, Integer> opened = new HashMap<>();
            for (PullRequestHistory history : historys) {
                switch (history.getAction()) {
                    case "merged":
                        merged.add(history.getPullRequestId());
                        break;
                    case "opened":
                        opened.put(history.getPullRequestId(), history.getActorId());
                        break;
                }
            }
            for (Integer pullRequestId : merged) {
                prh.add(opened.get(pullRequestId));
            }

        }

        // issue
        IIssueDao issueDao = mySQL.getMapper(IIssueDao.class);
        List<Issue> issues = issueDao.findByRepoId(project.getId());
        List<Integer> issueList = new ArrayList<>(issues.size());
        for(Issue issue:issues){
            issueList.add(issue.getReporterId());
        }
        //移除自身的影响
        watchersList.remove(project.getOwnerId());
        prh.remove(project.getOwnerId());
        issueList.remove(project.getOwnerId());

        IProjectDao projectDao = mySQL.getMapper(IProjectDao.class);
        project.setWatchers(TransUtil.List2String(watchersList));
        project.setRequests(TransUtil.List2String(prh));
        project.setIssues(TransUtil.List2String(issueList));
        projectDao.updateProjectInit(project);

     //   System.out.println( String.format("project id:%d, watcherSize:%d, prh:%d, issuee:%d", project.getId(), watchersList.size(),prh.size(),issueList.size()));
//        printList(project.getId() + " - watchersList " ,watchersList);
//        printList(project.getId() + " - prh " ,prh);
//        printList(project.getId() + " - issueList " ,issueList);
        mySQL.getSession().get().commit();
    }

    public void printList(String name,List<Integer> list){
        System.out.println( name + ": size : " + + list.size());

    }


}
