import graduation.dto.MethodDto;
import graduation.method.CaculateHITS;
import graduation.service.Figure;
import graduation.util.FileUtil;
import org.junit.Test;
import org.junit.validator.PublicClassValidator;

import java.io.IOException;

import static graduation.service.Figure.hitsFigure;
import static graduation.util.FileUtil.IteratorTrend;


/**
 * 类描述
 *
 * @author yfk
 * @date 2021/4/6
 */
public class HitsIteratorTest {
    @Test
    public void testHits() {
        MethodDto dto = FileUtil.readMethodDtoObject("D:\\31577\\桌面\\毕设\\data\\temp\\methodDto0.txt");
        CaculateHITS hits = new CaculateHITS(dto);
        hits.Hits();
    }

    @Test
    public void testDouble() {
        double hits = -1;
        String value = String.format("value:%f", hits);
        System.out.println(value);
    }

    @Test
    public void testDraw() {
//        String name = "asd" + 5;
//        System.out.println(name);
//        String data = String.format("%d,%s\r\n", 1, 0.0000000000157);
//        System.out.println(data);
//        data = String.format("%d,%f\r\n", 1, 0.0000000000157);
//        System.out.println(data);
           FileUtil.readCsvAndDrawHITS();
    }

    @Test
    public void run() throws IOException {
//        MethodDto dto = FileUtil.readMethodDtoObject("D:\\31577\\桌面\\毕设\\data\\temp\\methodDto0.txt");
//        CaculateHITS hits = new CaculateHITS(dto);
//        hits.Hits();

        // draw
        IteratorTrend();
        //drawIteratorTrend(".\\data\\iteratorTrend.csv");
        // hitsFigure();
        //figure
    }
}
