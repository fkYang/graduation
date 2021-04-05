package graduation.util;

import graduation.dto.MethodDto;
import graduation.dto.PrepareFigureDto;

import java.io.*;
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
            writer.write("num,times\r\n");
            Set<Integer> keySet = map.keySet();
            List<Integer> keys = new ArrayList<>(map.keySet());
            Collections.sort(keys, new Comparator<Integer>() {
                @Override
                public int compare(Integer first, Integer second) {
                    return map.get(first) - map.get(second);
                    // return  first - second;
                }
            });
            //  int times = 0;
            for (int i = 0; i < keys.size(); i++) {
                int key = keys.get(i);
                String data = String.format("%d,%d\r\n", key, map.get(key));
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
        String path = String.format("methodDto%d.txt", dto.getTimes());
        writeMethodObject(dto, path);
    }

    //"methodDto.txt"
    public static void writeMethodObject(MethodDto dto) {
        writeMethodObject(dto, "methodDto.txt");
        writeMethodTimesObject(dto);
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

    private static MethodDto readMethodDtoObject(String path) {
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
}
