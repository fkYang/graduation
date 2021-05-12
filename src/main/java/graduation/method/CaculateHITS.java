package graduation.method;

import graduation.dto.MethodDto;
import graduation.util.FileUtil;
import graduation.util.LogUtil;
import graduation.util.Parameter;
import org.apache.commons.math3.analysis.function.Sqrt;

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

    public CaculateHITS(MethodDto dto) {
        this.dto = dto;
    }


    public void Hits() {
        for (int i = 0; i < Parameter.iteratorTimes; i++) {
            iteratorCaculate();
            LogUtil.info(String.format("hub %d, diff:%f", i, differentHubValue));
        }
        FileUtil.writeUserFollow(dto, null);
    }

    void iteratorCaculate() {
        Parameter.authSum = 0;
        Parameter.hubSum = 0;
        // max
        differentHubValue = 0;
        differentAuthValue = 0;


        int times = dto.getTimes();
        int index = dto.getIteratorIndex();
        List<Integer> list = dto.getUserList();
        for (; index < list.size(); index++) {
            if (times == 0) {
                initHitsById(list.get(index));
            } else {
                iteratorById(list.get(index));
            }
            System.out.println(String.format("times:%d, id:%d", times, index));
           // LogUtil.info();
            dto.setIteratorIndex(index);
        }
        if (dto.getTimes() == 0) {
            // 去除follower失效数据
// 去除 project 失效项目
        }
        dto.setIteratorIndex(0);
        //dto.setTimes(times + 1);
        normalization();
        dto.setTimes(times + 1);
        FileUtil.writeMethodTimesObject(dto);
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
            removeUserHub(id);
            return;
        }
        Parameter.hubSum += Math.pow(hub, 2);
        dto.getUserHub().put(id, new MethodDto.HitsValue(hub));
    }

    private void removeUserHub(Integer id) {
        dto.getUserProjectMap().remove(id);
        dto.getUserFollowerMap().remove(id);
    }


    void iteratorById(Integer id) {
        if (id == 58) {
            System.out.println("iteratorById " + id);
        }
        double hub = 0;
        List<Integer> followers = dto.getUserFollowerMap().get(id);
        // hub += followers.size() * Parameter.userRatioFollower;
        // todo delete
        if (followers != null) {
            // follower -> project auth
            for (Integer follower : followers) {
                List<Integer> projects = dto.getUserProjectMap().get(follower);
                // todo delete
                if (projects == null) {
                    continue;
                }
                for (Integer project : projects) {
                    double valueHub = dto.getProjectAuth().get(project).getValue();
                    hub += valueHub * Parameter.userRatioFollower;
                }
            }
        }

        List<Integer> projects = dto.getUserProjectMap().get(id);
        if (projects != null) {
            for (Integer project : projects) {
                MethodDto.HitsValue hitsProjectAuth = dto.getProjectAuth().get(project);
                double auth = 0;
                // todo auth update
                MethodDto.ProjectMap projectMap = dto.getProjectMap().get(project);
                List<Integer> issues = projectMap.getIssues();
                for (Integer issue : issues) {
                    // todo delete
                    MethodDto.HitsValue hitsValue = dto.getUserHub().get(issue);
                    if (hitsValue != null) {
                        double value = hitsValue.getValue();
                        auth += value * Parameter.projectRatioIssue;
                    }
                }
                List<Integer> requests = projectMap.getRequests();
                for (Integer request : requests) {
                    // todo delete
                    MethodDto.HitsValue hitsValue = dto.getUserHub().get(request);
                    if (hitsValue != null) {
                        double value = hitsValue.getValue();
                        auth += value * Parameter.projectRatioRequest;
                    }
                }

                // todo hub update
                List<Integer> watchers = projectMap.getWatchers();
                for (Integer watcher : watchers) {
                    // hub
                    List<Integer> projectsWatcher = dto.getUserProjectMap().get(watcher);
                    // todo delete
                    if (projectsWatcher != null) {
                        for (Integer projectWatcher : projectsWatcher) {
                            MethodDto.HitsValue hitsValue = dto.getUserHub().get(projectWatcher);
                            // todo delete
                            if (hitsValue != null) {
                                double valueHub = hitsValue.getValue();
                                // hitsValue.getValue();
                                hub += valueHub * Parameter.userRatioWatcher;
                            }
                        }
                    }
                }
                //auth update
                Parameter.authSum += Math.pow(auth, 2);
                hitsProjectAuth.setTemp(auth);
            }
        }

        MethodDto.HitsValue hitsUserHub = dto.getUserHub().get(id);
        // hub update
        Parameter.hubSum += Math.pow(hub, 2);
        hitsUserHub.setTemp(hub);
    }

    void normalization() {
        Parameter.hubSum = Math.sqrt(Parameter.hubSum);
        Parameter.authSum = Math.sqrt(Parameter.authSum);

        // 归一化计算
        // userHub,projectAuth
        Set<Integer> set = dto.getUserHub().keySet();
        for (Integer userId : set) {
            MethodDto.HitsValue hub = dto.getUserHub().get(userId);


            double valueOld = hub.getValue();
            hub.setValue(hub.getTemp() / Parameter.hubSum);
            if (dto.getTimes() != 0) {
                setDifferentHubValue(Math.abs(valueOld - hub.getValue()));
            }
            hub.setTemp(0);
            List<Integer> projects = dto.getUserProjectMap().get(userId);
            if (projects != null) {
                for (Integer project : projects) {
                    MethodDto.HitsValue auth = dto.getProjectAuth().get(project);

                    valueOld = auth.getValue();
                    auth.setValue(auth.getTemp() / Parameter.authSum);
                    if (dto.getTimes() != 0) {
                        setDifferentAuthValue(Math.abs(valueOld - auth.getValue()));
                    }
                    auth.setTemp(0);
                }
            }
        }
    }
}
