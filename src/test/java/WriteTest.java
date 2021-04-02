import com.mysql.cj.protocol.a.result.ResultsetRowsStatic;
import graduation.dto.MethodDto;
import graduation.dto.PrepareFigureDto;
import graduation.util.FileUtil;
import sun.awt.SunHints;

import java.io.*;
import java.util.*;

/**
 * 类描述
 *
 * @author yfk
 * @date 2021/3/29
 */
public class WriteTest {

    public static void main(String[] args) throws Exception {
//        List<String> list = new ArrayList<>();
//        list.add("12,12");
//        list.add("2,12");

        testOB();
        String path = "data.csv";
////        writeFileContext(list,path);
        Map<Integer,Integer> map = new LinkedHashMap<>();
        map.put(1,12);
        map.put(2,500);
        map.put(0,60);
        FileUtil.writeMap(map,path);
//       // Visual.drawCsv(path);




    }

    static void testOB(){
        MethodDto methodDto1 = FileUtil.readMethodDtoObject();
        PrepareFigureDto dto = new PrepareFigureDto();
        dto.setId(5);
        dto.getUserFollower().put(1,50);
        dto.getProjectIssue().put(1,50);
        dto.getProjectWatcher().put(10,50);
        dto.getProjectRequest().put(3,20);

        FileUtil.writeObject(dto);
        MethodDto methodDto = FileUtil.readMethodDtoObject();
    }

    public static void writeFileContext(List<String> strings, String path) throws Exception {
        File file = new File(path);
        //如果没有文件就创建
        if (!file.isFile()) {
            file.createNewFile();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        for (String l:strings){
            writer.write(l + "\r\n");
        }
        writer.close();
    }

    public static void writeFileContext(Map<Integer, Integer> map, String path)  {
        BufferedWriter writer = null;
        try {
            File file = new File(path);
            //如果没有文件就创建
            if (!file.isFile()) {
                file.createNewFile();
            }
             writer = new BufferedWriter(new FileWriter(path));
            writer.write("name,num\r\n");
            Set<Integer> keySet = map.keySet();
            //  int times = 0;
            for (Integer key : keySet) {
//                StringBuilder builder = new StringBuilder();
//                builder.append(key);
//                builder.append(",");
//                builder.append(map.get(key));
                String data = String.format("%d,%d\r\n",key, map.get(key));
                writer.write(data);
                //  times++;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (writer != null){
                try {
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
