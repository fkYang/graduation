package graduation.service;

import graduation.dao.IProjectDao;
import graduation.dao.IUserDao;
import graduation.dto.PrepareFigureDto;
import graduation.entity.GitHubUser;
import graduation.entity.Project;
import graduation.util.*;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static graduation.util.FileUtil.IteratorTrend;
import static graduation.util.FileUtil.readCsvAndDrawHITS;

/**
 * 类描述
 *
 * @author yfk
 * @date 2021/3/29
 */
public class Figure {
    static final MySQL mySQL = new MySQL();
    static PrepareFigureDto dto = FileUtil.readObject();

    public static void drawPrepare() {
        IUserDao userDao = mySQL.getMapper(IUserDao.class);
        // 遍历projects
        int size = Parameter.batch, id = dto.getId(), times = 0;
        // id = 59536;
        while (size == Parameter.batch) {
            List<GitHubUser> users = userDao.findUsersById(id, size);
            size = users.size();
            if (size == 0) {
                break;
            }
            id = users.get(users.size() - 1).getId();
            Integer[] ids = new Integer[users.size()];
            for (int index = 0; index < users.size(); index++) {
                GitHubUser user = users.get(index);
                List<Integer> followers = TransUtil.string2List(user.getFollowers());
                Integer last = dto.getUserFollower().getOrDefault(followers.size(), 0);
                dto.getUserFollower().put(followers.size(), last + 1);
                // add project
                ids[index] = user.getId();
            }
            users = null;
            Map<Integer, Integer> userProjectMap = iteratorProject(ids);
            dto.getUserProject().putAll(userProjectMap);

            dto.setId(id);
            LogUtil.info(String.format("times:%d ,user:%d ", times, id));
            times++;
            if (times % 100 == 0) {
                FileUtil.writeObject(dto);
            }
        }
        FileUtil.writeObject(dto);
        FileUtil.writeMap(dto.getUserFollower(), "follower2.csv");
        FileUtil.writeMap(dto.getProjectWatcher(), "watcher2.csv");
        FileUtil.writeMap(dto.getProjectRequest(), "request2.csv");
        FileUtil.writeMap(dto.getProjectIssue(), "issue.csv");

        FileUtil.writeMap(dto.getProjectIssue(), "userProject.csv");
    }

    private static  Map<Integer, Integer> iteratorProject(Integer[] ids) {
        Map<Integer, Integer> userProject = new LinkedHashMap<>();
        // findByOwnerIds
        IProjectDao projectDao = mySQL.getMapper(IProjectDao.class);
        List<Project> projects = projectDao.findByOwnerIds(ids);
        for (Project project : projects) {
            if (project.getForkedFrom() != null || project.getDeleted() == 1) {
                // 非原创
                continue;
            }
            userProject.put(project.getOwnerId(), userProject.getOrDefault(project.getOwnerId(), 0));

            List<Integer> watcher = TransUtil.string2List(project.getWatchers());
            Integer last = dto.getProjectWatcher().getOrDefault(watcher.size(), 0);
            dto.getProjectWatcher().put(watcher.size(), last + 1);

            List<Integer> issue = TransUtil.string2List(project.getIssues());
            last = dto.getProjectIssue().getOrDefault(issue.size(), 0);
            dto.getProjectIssue().put(issue.size(), last + 1);

            List<Integer> request = TransUtil.string2List(project.getRequests());
            last = dto.getProjectRequest().getOrDefault(request.size(), 0);
            dto.getProjectRequest().put(request.size(), last + 1);
        }
        return  userProject;
    }

    // 遍历数据，初始化数值，
    // 清除数据，hub为0的数据-project auth

    // hits结束后，绘制图片用于分析
    public static void hitsFigure() {
        readCsvAndDrawHITS();
        try {
            IteratorTrend();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
