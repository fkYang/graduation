package graduation.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 类描述
 *
 * @author yfk
 * @date 2021/3/29
 */
@Data
public class PrepareFigureDto implements Serializable {
    Integer id = 0;
    // watcher数，此数量的数量
    Map<Integer,Integer> userFollower;
    Map<Integer,Integer> projectWatcher;
    Map<Integer,Integer> projectIssue;
    Map<Integer,Integer> projectRequest;
    {
        userFollower = new LinkedHashMap<>();
        projectIssue = new LinkedHashMap<>();
        projectRequest = new LinkedHashMap<>();
        projectWatcher = new LinkedHashMap<>();
    }
}
