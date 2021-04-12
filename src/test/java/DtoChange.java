import com.sun.org.apache.xpath.internal.functions.FuncFalse;
import graduation.dto.MethodDto;
import graduation.util.FileUtil;

import java.sql.Time;

/**
 * 类描述
 *
 * @author yfk
 * @date 2021/4/4
 */
public class DtoChange {
    public static void main(String[] args) {
        long old = System.currentTimeMillis();

        MethodDto dto = FileUtil.readMethodDtoObject();
        dto.setInitDTOEnd(false);
        FileUtil.writeMethodObject(dto);
        System.out.println(System.currentTimeMillis()-old);

    }
}
