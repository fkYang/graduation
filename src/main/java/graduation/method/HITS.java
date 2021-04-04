package graduation.method;

import com.sun.deploy.security.SecureStaticVersioning;
import graduation.dao.IProjectDao;
import graduation.dao.IUserDao;
import graduation.dto.MethodDto;
import graduation.entity.GitHubUser;
import graduation.entity.Project;
import graduation.util.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * 类描述
 *
 * @author yfk
 * @date 2021/4/4
 */
public class HITS {
    // user,staticc参数，方法名称
    static final MySQL mySQL = new MySQL();
    public static MethodDto dto = FileUtil.readMethodDtoObject();

    static public void hits() {
        PrepareDto prepare = new PrepareDto(dto);
        prepare.initHITSSDTO();
//        CaculateHITS hits = new CaculateHITS(dto);
//        hits.Hits();

//        initHITSSDTO();
//        initHits();
//        iterator();
    }

}
