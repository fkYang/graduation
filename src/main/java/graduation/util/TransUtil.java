package graduation.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类描述
 *
 * @author yfk
 * @date 2021/3/17
 */
public class TransUtil {

    public static <T> String List2String(List<T> list) {
        if (list.size() == 0) {
            return "";
        }
        list.remove(null);
        list = list.stream().distinct().collect(Collectors.toList());

        // 祛除null
        StringBuilder result = new StringBuilder();
        for (T index : list) {
            if (index != null) {
                result.append(index + ",");
            }
        }
        return result.toString();
    }

    public static List<Integer> string2List(String s) {
        List<Integer> list;
        if (s == null || s.equals("")) {
            list = new LinkedList<>();
            return list;
        }
        String[] split = s.split(",");
        list = new ArrayList<>(split.length);
        for (String index : split) {
            list.add(Integer.parseInt(index));
        }
        return list;
    }

}
