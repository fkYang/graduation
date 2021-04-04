package graduation.method;

import graduation.dto.MethodDto;
import graduation.util.FileUtil;
import graduation.util.LogUtil;
import graduation.util.Parameter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 类描述
 *
 * @author yfk
 * @date 2021/4/4
 */
public class CaculateHITS {
    MethodDto dto;
    double differentHubValue = 0;
    double differentAuthValue = 0;

    void setDifferentHubValue(double value) {
        differentHubValue = Math.max(value, differentHubValue);
    }

    void setDifferentAuthValue(double value) {
        differentAuthValue = Math.max(value, differentAuthValue);

    }

    CaculateHITS(MethodDto dto) {
        this.dto = dto;
    }


    void Hits() {
        while (differentHubValue > 0.001) {
            iteratorCaculate();
        }
    }

    void iteratorCaculate() {
        int times = dto.getTimes();
        int index = dto.getIteratorIndex();
        List<Integer> list = dto.getUserList();
        for (; index < list.size(); index++) {
            if (times == 0) {
                initHitsById(list.get(index));
            } else {
                iteratorById(list.get(index));
            }

            LogUtil.info(String.format("times:%d, id:%d", times, index));
            // LogUtil.info(String.format("times:%d ,user:%d ", times, id));
            if (index % 100 == 0) {
                FileUtil.writeMethodObject(dto);
            }
            dto.setIteratorIndex(index);
        }
        dto.setTimes(times + 1);
        normalization();
        FileUtil.writeMethodObject(dto);
    }

    void initHitsById(Integer id) {
        double hub = 0;
        List<Integer> followers = dto.getUserFollowerMap().get(id);
        hub += followers.size() * Parameter.userRatioFollower;

        //auth
        List<Integer> projects = dto.getUserProjectMap().get(id);
        List<Integer> deletedProject = new ArrayList<>(projects.size() + 1);
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
            Parameter.authSum += Math.pow(auth, 2);
            dto.getProjectAuth().put(project, new MethodDto.HitsValue(auth));
        }
        projects.removeAll(deletedProject);
        dto.getUserProjectMap().put(id, projects);

        // todo 计算hub， 0 不加,
        if (hub == 0) {
            dto.getUserProjectMap().remove(id);
            dto.getUserFollowerMap().remove(id);
            return;
        }
        Parameter.hubSum += Math.pow(hub, 2);
        dto.getUserHub().put(id, new MethodDto.HitsValue(hub));
    }

    void iteratorById(Integer id) {
        double hub = 0;
        List<Integer> followers = dto.getUserFollowerMap().get(id);
        // hub += followers.size() * Parameter.userRatioFollower;

        // follower -> project auth
        for (Integer follower : followers) {
            List<Integer> projects = dto.getUserProjectMap().get(follower);
            for (Integer project : projects) {
                double valueHub = dto.getProjectAuth().get(project).getValue();
                // hitsValue.getValue();
            }
        }

        List<Integer> projects = dto.getUserProjectMap().get(id);
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
        MethodDto.HitsValue hitsUserHub = dto.getUserHub().get(id);
        hitsUserHub.setTemp(hub);
    }

    void normalization() {
        // 归一化计算
        // userHub,projectAuth
        Set<Integer> set = dto.getUserHub().keySet();
        for (Integer userId : set) {

            MethodDto.HitsValue hub = dto.getUserHub().get(userId);
            double valueOld = hub.getValue();
            hub.setValue(hub.getTemp() / Parameter.hubSum);
            setDifferentHubValue(Math.abs(valueOld - hub.getValue()));

            List<Integer> projects = dto.getUserProjectMap().get(userId);
            for (Integer project : projects) {
                MethodDto.HitsValue auth = dto.getProjectAuth().get(project);

                valueOld = auth.getValue();
                auth.setValue(auth.getTemp() / Parameter.authSum);
                setDifferentAuthValue(Math.abs(valueOld - auth.getValue()));
            }
        }
    }
}
