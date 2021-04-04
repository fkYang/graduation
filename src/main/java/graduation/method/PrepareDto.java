package graduation.method;

import graduation.dao.IProjectDao;
import graduation.dao.IUserDao;
import graduation.dto.MethodDto;
import graduation.entity.GitHubUser;
import graduation.entity.Project;
import graduation.util.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 类描述
 *
 * @author yfk
 * @date 2021/4/4
 */
public class PrepareDto {
    static final MySQL mySQL = new MySQL();
    MethodDto dto = null;

    PrepareDto(MethodDto dto) {
        this.dto = dto;
    }

    void initHITSSDTO() {
        if (!dto.isInitDTOEnd()) {
            initMethodDto();
            // init
            dto.setUserList(new ArrayList<>(dto.getUserHub().keySet()));
            dto.setInitDTOEnd(true);
            FileUtil.writeMethodObject(dto);
            LogUtil.info(String.format("initDto end"));
        }
    }

    void initMethodDto() {
        IUserDao userDao = mySQL.getMapper(IUserDao.class);
        int size = Parameter.batch, id = dto.getInitDTOId(), times = 0;
        while (size == Parameter.batch) {
            List<GitHubUser> users = userDao.findUsersById(id, size);
            size = users.size();
            id = users.get(users.size() - 1).getId();
            Integer[] ids = new Integer[users.size()];
            for (int index = 0; index < users.size(); index++) {
                GitHubUser user = users.get(index);
                List<Integer> followers = TransUtil.string2List(user.getFollowers());
                dto.getUserFollowerMap().put(user.getId(), followers);
                // 初始化map
                dto.getUserProjectMap().put(user.getId(), new LinkedList<>());
                // add project
                ids[index] = user.getId();
            }
            users = null;
            // 减少数据库查询次数
            initMethodDtoProject(ids);
            //init hub auth
            dto.setInitDTOId(id);
            LogUtil.info(String.format("times:%d ,user:%d ", times, id));
            times++;
            if (times % 100 == 0) {
                FileUtil.writeMethodObject(dto);
                return;
            }
        }
    }

    void initMethodDtoProject(Integer[] ids) {
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
}
