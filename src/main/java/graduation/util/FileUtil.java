package graduation.util;

import graduation.dto.MethodDto;
import graduation.dto.PrepareFigureDto;
import graduation.entity.GitHubUser;
import graduation.method.PrepareDto;
import graduation.service.GitHubUserService;
import javafx.scene.control.Tab;
import tech.tablesaw.api.CategoricalColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.columns.Column;
import tech.tablesaw.plotly.Plot;
import tech.tablesaw.plotly.api.LinePlot;
import tech.tablesaw.plotly.api.ScatterPlot;
import tech.tablesaw.plotly.components.Figure;
import tech.tablesaw.plotly.components.Layout;
import tech.tablesaw.plotly.traces.ScatterTrace;
import tech.tablesaw.table.TableSliceGroup;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * 类描述
 *
 * @author yfk
 * @date 2021/3/29
 */
public class FileUtil {

    public static void writeMap(Map<Integer, Integer> map, String path) {
        BufferedWriter writer = null;
        try {
            File file = new File(path);
            //如果没有文件就创建
            if (!file.isFile()) {
                file.createNewFile();
            }
            writer = new BufferedWriter(new FileWriter(path));
            writer.write("num,times,log10(num),log10(times)\r\n");
            Set<Integer> keySet = map.keySet();
            List<Integer> keys = new ArrayList<>(map.keySet());
            Collections.sort(keys, new Comparator<Integer>() {
                @Override
                public int compare(Integer first, Integer second) {
                    return map.get(first).compareTo(map.get(second));
                    // return  first - second;
                }
            });
            //  int times = 0;
            for (int i = 0; i < keys.size(); i++) {
                int key = keys.get(i);
                String data = String.format("%d,%d,%.8f,%.8f\r\n", key, map.get(key), Math.log10(key), Math.log10(map.get(key)));
                writer.write(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void writeObject(PrepareFigureDto dto) {
        try {
            FileOutputStream f = new FileOutputStream(new File("myObjects.txt"));
            ObjectOutputStream o = new ObjectOutputStream(f);
            // Write objects to file
            o.writeObject(dto);
            o.close();
            f.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        }
    }

    public static PrepareFigureDto readObject() {
        PrepareFigureDto dto = new PrepareFigureDto();
        try {
            FileInputStream fi = new FileInputStream(new File("myObjects.txt"));
            ObjectInputStream oi = new ObjectInputStream(fi);

            // Read objects
            dto = (PrepareFigureDto) oi.readObject();

            oi.close();
            fi.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch blocks
            e.printStackTrace();
        }
        return dto;
    }

    public static void writeMethodTimesObject(MethodDto dto) {
        String dir = String.format(".\\data\\%d\\", dto.getTimes());
        FileUtil.createDir(dir);
        String path = dir + String.format("methodDto%d.txt", dto.getTimes());
        writeMethodObject(dto, path);
        // write hits csv
        writeHITSCsv(dto, dir);
    }

    //"methodDto.txt"
    public static void writeMethodObject(MethodDto dto) {
        writeMethodObject(dto, "methodDto.txt");
        // writeMethodTimesObject(dto);
    }

    //private void
    public static void createDir(String path) {
        File file1 = new File(path);
        if (!file1.exists()) {
            file1.mkdirs();
        }
    }

    public static void writeMethodObject(MethodDto dto, String path) {
        try {
            FileOutputStream f = new FileOutputStream(new File(path));
            ObjectOutputStream o = new ObjectOutputStream(f);

            // Write objects to file
            o.writeObject(dto);

            o.close();
            f.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        }
    }


    public static MethodDto readMethodDtoObject() {
        return readMethodDtoObject("methodDto.txt");
    }

    public static MethodDto readMethodDtoObject(String path) {
        MethodDto dto = new MethodDto();
        try {
            FileInputStream fi = new FileInputStream(new File(path));
            ObjectInputStream oi = new ObjectInputStream(fi);

            // Read objects
            dto = (MethodDto) oi.readObject();

            oi.close();
            fi.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch blocks
            e.printStackTrace();
        }
        return dto;
    }

    private static void writeHITSCsv(MethodDto dto, String dir) {
        //write hub
        writeHITS(dto.getUserHub(), dir + String.format("userHub%d.csv", dto.getTimes()), dto.getTimes());
        //write auth
        writeHITS(dto.getProjectAuth(), dir + String.format("projectAuth%d.csv", dto.getTimes()), dto.getTimes());
    }

    static private void writeHITS(Map<Integer, MethodDto.HitsValue> hitsValueMap, String path, int times) {
        BufferedWriter writer = null;
        try {
            File file = new File(path);
            //如果没有文件就创建
            if (!file.isFile()) {
                file.createNewFile();
            }
            writer = new BufferedWriter(new FileWriter(path));
            writer.write(String.format("id,%s,%s,times\r\n", Parameter.HITSId, Parameter.HITSValue));
            Set<Integer> keySet = hitsValueMap.keySet();
            List<Integer> keys = new ArrayList<>(hitsValueMap.keySet());
            Collections.sort(keys, new Comparator<Integer>() {
                @Override
                public int compare(Integer first, Integer second) {
                    if (hitsValueMap.get(first).getValue() > hitsValueMap.get(second).getValue()) {
                        return 1;
                    } else if (hitsValueMap.get(first).getValue() == hitsValueMap.get(second).getValue()) {
                        return 0;
                    }
                    return -1;
                    // return (int) (hitsValueMap.get(first).getValue() - hitsValueMap.get(second).getValue());
                }
            });
            //  int times = 0;
            for (int i = 0; i < keys.size(); i++) {
                int key = keys.get(i);
                String data = String.format("%d,%d,%s,%d\r\n", i, key, hitsValueMap.get(key).getValue(), times);
                writer.write(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 绘制 hitsValue 的散点图
    static public void readCsvAndDrawHITS() {
        for (int i = 1; i < Parameter.iteratorTimes; i++) {
            String dir = String.format(".\\data\\%d\\", i);
            String hub = dir + String.format("userHub%d.csv", i);

            String auth = dir + String.format("projectAuth%d.csv", i);
            // drawCsv(hub, i);
            if (!drawCsv(hub, i, "hub")) {
                LogUtil.info("draw csv finish");
                return;
            }
            if (!drawCsv(auth, i, "auth")) {
                LogUtil.info("draw csv error");
                return;
            }
        }
    }

    // 绘制单次结果的csv图-hitsValue
    static private boolean drawCsv(String path, int times, String name) {
        Table hits = null;
        try {
            hits = Table.read().csv(path);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        hits = hits.dropWhere(hits.numberColumn(Parameter.HITSValue).isLessThan(Parameter.minHitsThresholdValue));

        Plot.show(
                ScatterPlot.create(String.format("hitsValue%d-%s", times, path),
                        hits, "id", Parameter.HITSValue), FileUtil.defaultFile(String.format("%d-%s.html", times, name)));
        return true;
    }

    // 依据单次结果，绘制迭代结果csv
    static public void IteratorTrend() throws IOException {
        Table[] tableArray = new Table[Parameter.iteratorTimes];
        // todo delete 1-0
        for (int i = 0; i < Parameter.iteratorTimes; i++) {
            String dir = String.format(".\\data\\%d\\", i + 1);
            String path = dir + String.format("userHub%d.csv", i + 1);
            tableArray[i] = Table.read().csv(path);
            tableArray[i] = tableArray[i].dropWhere(tableArray[i].numberColumn(Parameter.HITSValue).isLessThan(Parameter.minHitsThresholdValue));
            //tables[i].sortAscendingOn(Parameter.HITSId);
        }
        Table table = tableArray[0];
        for (int i = 1; i < Parameter.iteratorTimes; i++) {
            // tables[i].sortAscendingOn(Parameter.HITSId);
            table = table.append(tableArray[i]);
            //table = table.joinOn(Parameter.HITSId).fullOuter(tables[i]);
        }
        table.write().csv(new File(".\\data\\iteratorTrend.csv"));

        String title = "hits-trend", dateColX = "times", yCol = Parameter.HITSValue, groupCol = Parameter.HITSId;
        TableSliceGroup tables = table.splitOn(new CategoricalColumn[]{table.categoricalColumn(groupCol)});
        Layout layout = Layout.builder(title, dateColX, yCol).build();
        ScatterTrace[] traces = new ScatterTrace[tables.size()];
        List<Table> tableList = tables.asTableList();
        for (int i = 0; i < tables.size(); ++i) {
            Table tableTemp = tableList.get(i);

            Table t = tableTemp.sortOn(dateColX);
            traces[i] = ScatterTrace.builder(t.intColumn(dateColX), t.numberColumn(yCol)).showLegend(true).name(tableTemp.name()).mode(ScatterTrace.Mode.LINE).build();
        }
        Figure figure = new Figure(layout, traces);
        //traces
        //traces.
        Plot.show(figure, FileUtil.defaultFile(title + Parameter.minHitsThresholdValue + ".html"));
        // drawIteratorTrend(".\\data\\iteratorTrend.csv");
    }

    // 绘制最后迭代完的 收敛图
//    static public void drawIteratorTrend(String path) throws IOException {
//        Table table = Table.read().csv(path);
//        String title = "hits-trend", dateColX = "times", yCol = Parameter.HITSValue, groupCol = Parameter.HITSId;
//        TableSliceGroup tables = table.splitOn(new CategoricalColumn[]{table.categoricalColumn(groupCol)});
//        Layout layout = Layout.builder(title, dateColX, yCol).build();
//        ScatterTrace[] traces = new ScatterTrace[tables.size()];
//        List<Table> tableList = tables.asTableList();
//        for (int i = 0; i < tables.size(); ++i) {
//            Table tableTemp = tableList.get(i);
//            Table t = tableTemp.sortOn(dateColX);
//            traces[i] = ScatterTrace.builder(t.intColumn(dateColX), t.numberColumn(yCol)).showLegend(true).name(tableTemp.name()).mode(ScatterTrace.Mode.LINE).build();
//        }
//        Figure figure = new Figure(layout, traces);
//        Plot.show(figure);
//    }

    static public void writeUserFollow(MethodDto dto, String path) {
        if (path == null) {
            path = ".\\data\\userFollowDir";
            FileUtil.createDir(".\\data\\userFollowDir\\");
        }
        // Set<Integer> userSet = new HashSet<>(dto.getUserList());
        Map<Integer, List<Integer>> followerMap = dto.getUserFollowerMap();
        Map<Integer, MethodDto.HitsValue> userHub = dto.getUserHub();
        Set<Integer> userSet = new HashSet<>(dto.getUserHub().keySet());
        Set<Integer> userSetCopy = new HashSet<>(dto.getUserHub().keySet());

        //double min = 0;0.0001
        System.out.println("size: " + userSet.size());
        double limit = 0.01;
        for (Integer id : userSetCopy) {
            if (userHub.get(id).getValue() <= limit) {
                userSet.remove(id);
                System.out.println(String.format("remove id:%d. value:%f", id, userHub.get(id).getValue()));
            }
        }
        System.out.println("size: " + userSet.size());


        //userSet.removeIf(id -> userHub.get(id).getValue() == 0);


        BufferedWriter writer = null, userWriter = null;
        int index = 0;
        try {
            File file = new File(String.format(path + "\\userFollower%d.csv",userSet.size()));
            //如果没有文件就创建
            if (!file.isFile()) {
                file.createNewFile();
            }
            writer = new BufferedWriter(new FileWriter(String.format(path + "\\userFollower%d.csv",userSet.size())));
            writer.write("id,Target,Source\r\n");
//String.format(path + "\\usersHits%d.csv",userSet.size())
            File userFile = new File(String.format(path + "\\usersHits%d.csv",userSet.size()));
            if (!file.isFile()) {
                userFile.createNewFile();
            }
            userWriter = new BufferedWriter(new FileWriter(String.format(path + "\\usersHits%d.csv",userSet.size())));
            userWriter.write("id,hubValue,name\r\n");
            for (Integer userId : userSet) {
                List<Integer> followers = followerMap.get(userId);
                GitHubUser user = GitHubUserService.getUser(userId);
                String data = String.format("%d,%.20f,%s\r\n", userId, userHub.get(userId).getValue(), user.getName());
       System.out.println(data);
                userWriter.write(data);
                for (Integer follower : followers) {
                    if (userSet.contains(follower)) {
                        // 保证边的两个端点在user中
                        index++;
                        data = String.format("%d,%d,%d\r\n", index, userId, follower);
                        writer.write(data);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (userWriter != null) {
                try {
                    userWriter.flush();
                    userWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    static public File defaultFile(String name) {
        Path path = Paths.get("html", name);

        try {
            Files.createDirectories(path.getParent());
        } catch (IOException var2) {
            throw new UncheckedIOException(var2);
        }

        return path.toFile();
    }
}
