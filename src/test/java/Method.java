
import com.koloboke.collect.map.hash.HashIntDoubleMaps;
import graduation.dao.*;
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
       // hits();
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

}
