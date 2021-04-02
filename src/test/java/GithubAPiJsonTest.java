
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.kevinsawicki.http.HttpRequest;
import com.mysql.cj.util.LogUtils;
import graduation.util.LogUtil;
import org.apache.log4j.Logger;

import java.util.List;


/**
 * 类描述
 *
 * @author yfk
 * @date 2021/3/9
 */
public class GithubAPiJsonTest {
    private static Logger logger = Logger.getLogger(GithubAPiJsonTest.class);
    public static void main(String[] args) {
        String name ="jmettraux";
        LogUtil.info(name);
        logger.info(name);
        int page = 1;
        List<Repos> repos = getRepos(name, page);
    }

   public static List<Repos> getRepos(String name, int page){
       System.out.print(String.format("https://api.github.com/users/%s/repos?page=%d", name, page));
      // HttpRequest request = HttpRequest.get(String.format("https://api.github.com/users/%s/repos?page=%d", name, page));
       //https://api.github.com/repos/tosch/ruote-kit
      // HttpRequest request = HttpRequest.get("https://api.github.com/repos/tosch/ruote-kit");
       HttpRequest request = HttpRequest.get("https://github.com/tosch/ruote-kit");

       String response = request.body();
      // Repos repos = JSONObject.parseObject(response, Repos.class);
       System.out.printf("data :%s",  response);
//       List<Repos> repoList = JSONArray.parseArray(response, Repos.class);
//       System.out.printf("data :%s",  response);
//       return repoList;
       return null;
   }
}
