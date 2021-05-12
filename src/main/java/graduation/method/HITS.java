package graduation.method;



import graduation.dto.MethodDto;
import graduation.util.*;


/**
 * 类描述
 *
 * @author yfk
 * @date 2021/4/4
 */
public class HITS {
    // user,staticc参数，方法名称
    //static final MySQL mySQL = new MySQL();
    public static MethodDto dto = FileUtil.readMethodDtoObject();

    static public void hits() {
//        PrepareDto prepare = new PrepareDto(dto);
//        prepare.initHITSSDTO();
        CaculateHITS hits = new CaculateHITS(dto);
        hits.Hits();

//        initHITSSDTO();
//        initHits();
//        iterator();
    }

}
