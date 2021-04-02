
import com.koloboke.collect.map.hash.HashIntDoubleMaps;
import graduation.dao.*;
import graduation.dto.MethodDto;
import graduation.entity.*;
import graduation.util.*;

import java.util.*;

/**
 * 类描述
 *
 * @author yfk
 * @date 2021/3/14
 */
public class Method {
    private static int lengthBatch = 30;

    public static void main(String[] args) {
        //   MySQL mySQL = new MySQL();
        //   MySQL mySQL2 = new MySQL();
        //  initProjectAuth();
        // initUserHub();
        //size();
        hits();
    }

    static void size() {
        Map<Integer, Double> mapBasic = HashIntDoubleMaps.newMutableMap();

        //   Map<Integer,Double> map = new HashMap<>(15000000);
        boolean flag = true;
        for (int i = 0; i < 15000000; i++) {
            // map.put(i, i*1.0);
            mapBasic.put(i, (double) i);
            if (i > 100000 && flag) {
                flag = false;
                System.out.println("remp");
                try {
                    Thread.sleep(100000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("end");
            }
        }
        System.out.println("final");
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    static void initProjectAuth() {
        MySQL mySQL = new MySQL();
        IProjectDao projectDao = mySQL.getMapper(IProjectDao.class);
        // 遍历projects
        int size = lengthBatch, id = 1522, index = 0;
        //init
        IPullRequestDao pullRequestDao = mySQL.getMapper(IPullRequestDao.class);
        IPullRequestHistoryDao pullRequestHistoryDao = mySQL.getMapper(IPullRequestHistoryDao.class);
        IIssueDao iIssueDao = mySQL.getMapper(IIssueDao.class);
        while (size == 30) {
            List<Project> projects = projectDao.findProjectsById(id, size);
            size = projects.size();
            id = projects.get(projects.size() - 1).getId();
            for (Project project : projects) {
                //
                // prh
                List<Integer> prh = new ArrayList<>();
                List<PullRequest> pullRequests = pullRequestDao.findByBaseRepoId(project.getId());
                if (pullRequests.size() == 0) {
                    continue;
                }
                List<Integer> pr = new ArrayList<>(pullRequests.size());

                for (PullRequest pullRequest : pullRequests) {
                    pr.add(pullRequest.getId());
                }
                // find [] prh  where id in ids and action = "merged"
                List<PullRequestHistory> historys = pullRequestHistoryDao.findByPullRequestIds(pr.toArray(new Integer[pullRequests.size()]));
                List<Integer> merged = new ArrayList<>(historys.size());
                // Map<Integer,Integer> merged = new HashMap<>();
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
                System.out.println(String.format("projects  id :%d, prh:%d", project.getId(), prh.size()));
            }
            break;
        }
    }


    // user,staticc参数，方法名称
    static final MySQL mySQL = new MySQL();
    static MethodDto dto = FileUtil.readMethodDtoObject();
    static double hubSum = 0, authSum = 0;


    static public void hits() {
        initUserHub();
       // iterator();
    }


    static void initUserHub() {
        if (!dto.isInitEnd()) {
            initMethodDto();
            // init
            dto.setUserList(new ArrayList<>(dto.getUserHub().keySet()));
            dto.setInitEnd(true);
            FileUtil.writeMethodObject(dto);
        }
    }

    static void initMethodDto() {
        IUserDao userDao = mySQL.getMapper(IUserDao.class);
        int size = Parameter.batch, id = dto.getInitId(), times = 0;
        // id = 59536;
        while (size == Parameter.batch) {
            List<GitHubUser> users = userDao.findUsersById(id, size);
            size = users.size();
            id = users.get(users.size() - 1).getId();
            Integer[] ids = new Integer[users.size()];
            for (int index = 0; index < users.size(); index++) {
                GitHubUser user = users.get(index);
                List<Integer> followers = TransUtil.string2List(user.getFollowers());
                dto.getUserFollowerMap().put(user.getId(), followers);
                dto.getUserProjectMap().put(user.getId(),  new LinkedList<>());
                // add project
                ids[index] = user.getId();
            }
            users = null;
            initMethodDtoProject(ids);
            //init hub auth
            initHits(ids);
            dto.setInitId(id);
            LogUtil.info(String.format("times:%d ,user:%d ", times, id));
            times++;
            if (times % 100 == 0) {
                FileUtil.writeMethodObject(dto);
                return;
            }
        }
        normalization();
    }

    static void initMethodDtoProject(Integer[] ids) {
        IProjectDao projectDao = mySQL.getMapper(IProjectDao.class);
        List<Project> projects = projectDao.findByOwnerIds(ids);
        for (Project project : projects) {
            if (project.getForkedFrom() != null || project.getDeleted() == 1) {
                // 非原创
                continue;
            }
            List<Integer> watcher = TransUtil.string2List(project.getWatchers());
            List<Integer> issue = TransUtil.string2List(project.getIssues());
            List<Integer> request = TransUtil.string2List(project.getRequests());

            MethodDto.ProjectMap projectMap = new MethodDto.ProjectMap();
            projectMap.setWatchers(watcher);
            projectMap.setIssues(issue);
            projectMap.setRequests(request);
            dto.getProjectMap().put(project.getId(), projectMap);
           // dto.getUserProjectMap().computeIfAbsent(project.getOwnerId(), k -> new LinkedList<>());
            List<Integer> list = dto.getUserProjectMap().get(project.getOwnerId());
            list.add(project.getId());
        }
    }

    static void initHits(Integer[] ids) {
        for (int i = 0; i < ids.length; i++) {
            //hub
            double hub = 0;
            Integer id = ids[i];
            List<Integer> followers = dto.getUserFollowerMap().get(id);
            hub += followers.size() * Parameter.userRatioFollower;

            //auth
            List<Integer> projects = dto.getUserProjectMap().get(id);
            List<Integer> deletedProject = new ArrayList<>(projects.size()+1);
            for (Integer project : projects) {
                MethodDto.ProjectMap map = dto.getProjectMap().get(project);
                hub += map.getWatchers().size() * Parameter.userRatioWatcher;

                double auth = 0;
                auth += map.getIssues().size() * Parameter.projectRatioIssue + map.getRequests().size() * Parameter.projectRatioRequest;
                if (auth == 0) {
                    // 0 删除元素，继续执行
                    deletedProject.add(project);
                  //projects.remove(project);
                   dto.getProjectMap().remove(project);
                   continue;
                }
                Parameter.authSum += Math.pow(auth,2);
                dto.getProjectAuth().put(project, new MethodDto.HitsValue(auth));
            }
            projects.removeAll(deletedProject);
            dto.getUserProjectMap().put(id,projects);

            // todo 计算hub， 0 不加,
            if (hub == 0) {
               dto.getUserProjectMap().remove(id);
               dto.getUserFollowerMap().remove(id);
                continue;
            }
            Parameter.hubSum += Math.pow(hub,2);
            dto.getUserHub().put(id, new MethodDto.HitsValue(hub));
        }
    }

    static void iterator() {
        IUserDao userDao = mySQL.getMapper(IUserDao.class);
        int size = Parameter.batch, id = dto.getIteratorIndex(), times = 0;
        // id = 59536;
        while (size == Parameter.batch) {
            List<GitHubUser> users = userDao.findUsersById(id, size);
            size = users.size();
            id = users.get(users.size() - 1).getId();
            Integer[] ids = new Integer[users.size()];
            for (int index = 0; index < users.size(); index++) {
                GitHubUser user = users.get(index);
                double hub = 0;

                List<Integer> followers = TransUtil.string2List(user.getFollowers());
                // follower -> project auth
                for (Integer follower : followers) {
                    List<Integer> projects = dto.getUserProjectMap().get(follower);
                    for (Integer project : projects) {
                        double valueHub = dto.getProjectAuth().get(project).getValue();
                        // hitsValue.getValue();
                    }
                }

                List<Integer> projects = dto.getUserProjectMap().get(user.getId());
                for (Integer project : projects) {
                    MethodDto.HitsValue hitsProjectAuth = dto.getProjectAuth().get(project);

                    double auth = 0;
                    // todo auth update
                    MethodDto.ProjectMap projectMap = dto.getProjectMap().get(project);
                    List<Integer> issues = projectMap.getIssues();
                    for (Integer issue : issues) {
                        double value = dto.getUserHub().get(issue).getValue();

                    }
                    List<Integer> requests = projectMap.getRequests();
                    for (Integer request : requests) {
                        double value = dto.getUserHub().get(request).getValue();

                    }

                    // todo hub update
                    List<Integer> watchers = projectMap.getWatchers();
                    for (Integer watcher : watchers) {
                        // hub
                        List<Integer> projectsWatcher = dto.getUserProjectMap().get(watcher);
                        for (Integer projectWatcher : projectsWatcher) {
                            double valueHub = dto.getUserHub().get(projectWatcher).getValue();
                            // hitsValue.getValue();
                        }
                    }

                }
                MethodDto.HitsValue hitsUserHub = dto.getUserHub().get(user.getId());
                hitsUserHub.setTemp(hub);
            }
            dto.setIteratorIndex(id);
            LogUtil.info(String.format("times:%d ,user:%d ", times, id));
            times++;
            if (times % 100 == 0) {
                FileUtil.writeMethodObject(dto);
            }
        }

        normalization();

    }

    static void normalization() {
        // 归一化计算
        // userHub,projectAuth
        Set<Integer> set = dto.getUserHub().keySet();
        for (Integer userId : set) {
            MethodDto.HitsValue hub = dto.getUserHub().get(userId);
            hub.setValue(hub.getTemp() / hubSum);
            List<Integer> projects = dto.getUserProjectMap().get(userId);
            for (Integer project : projects) {
                MethodDto.HitsValue auth = dto.getProjectAuth().get(project);
                auth.setValue(auth.getTemp() / authSum);
            }
        }
    }
}
