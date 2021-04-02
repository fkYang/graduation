package graduation.dto;

import com.koloboke.collect.Equivalence;
import com.koloboke.collect.ObjCollection;
import com.koloboke.collect.hash.HashConfig;
import com.koloboke.collect.map.IntObjCursor;
import com.koloboke.collect.map.hash.HashIntObjMap;
import com.koloboke.collect.set.hash.HashIntSet;
import com.koloboke.collect.set.hash.HashObjSet;
import com.koloboke.function.IntObjConsumer;
import com.koloboke.function.IntObjFunction;
import com.koloboke.function.IntObjPredicate;
import com.sun.javafx.fxml.builder.ProxyBuilder;
import lombok.Data;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;

/**
 * 类描述
 *
 * @author yfk
 * @date 2021/3/31
 */
@Data
public class MethodDto implements Serializable {

    // 初始化数据的内容
    int initId = 0;
    boolean initEnd = false;
    // 等全部弄完以后进行设置
    int iteratorIndex = 0;
    List<Integer> userList ;

    // static
    // user和follower
    Map<Integer, List<Integer>> userFollowerMap;
    Map<Integer, List<Integer>> userProjectMap;
    // project 和 issue等
    Map<Integer,ProjectMap> projectMap;
    {
        userFollowerMap = new LinkedHashMap<>();
        userProjectMap = new LinkedHashMap<>();
        projectMap = new LinkedHashMap<>();
    }

    // iterator
    Map<Integer,HitsValue> userHub;
    Map<Integer,HitsValue> projectAuth;
    {
        userHub = new LinkedHashMap<>();
        projectAuth = new LinkedHashMap<>();
    }
    @Data
    public static class HitsValue{
        double value;
        double temp;
        public HitsValue(double value){
            // 先temp，后归一化
            this.temp = value;
        }
    }

    @Data
    public static class ProjectMap{
        List<Integer> watchers;
        List<Integer> issues;
        List<Integer> requests;
    }
}
