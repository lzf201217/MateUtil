import JsonData.UserSort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.util.regex.Pattern;


/**
 * 账号名匹配度排序
 *
 * ver: 1.0
 * author: LZF
 *
 * date: 2019/10/21
 *
 */
public class MateService {



    /**
     * 计算账号匹配度并排序
     *
     * 引入参数：　weight　临时匹配度
     * 引入数组：　similarity对应用户账号列表相似度
     *
     * 参数key : 搜索关键字
     * 参数user: 带匹配度的sql语句查出表单中的账号名列表
     *
     */
    public List<UserSort> mateWeight(String key, List<UserSort> user){

        String fristname = key.split("_")[0];
        String lastname = new String();
        if (key.split("_").length != 1){
            lastname = key.split("_")[1];
        }else lastname = "";

        String pattern = fristname + ".*";
        String pattern1 = ".*" + fristname + ".*";

        String pattern2 = lastname + ".*";
        String pattern3 = ".*" + lastname + ".*";
        String pattern4 = lastname + "\\d";

        for (int i = 0; i < user.size(); i++){

            Double weight = 0.0;
            user.get(i).setSimilarity(0);

            if (fristname.equals(user.get(i).getUsername().split("_")[0])){
                weight = weight + 8;
                user.get(i).setSimilarity(weight);
            }else if (Pattern.matches(pattern, user.get(i).getUsername().split("_")[0])){
                weight = weight + 3 - (double) Math.abs(user.get(i).getUsername().split("_")[0].length() - fristname.length())/10;
                user.get(i).setSimilarity(weight);
            }else if (Pattern.matches(pattern1, user.get(i).getUsername().split("_")[0])){
                weight = weight + 1 - (double) Math.abs(user.get(i).getUsername().split("_")[0].length() - fristname.length())/10;
                user.get(i).setSimilarity(weight);
            }

            if (user.get(i).getUsername().split("_").length != 1){
                if (lastname.equals(user.get(i).getUsername().split("_")[1])){
                    weight = weight + 3;
                    user.get(i).setSimilarity(weight);
                } else if(Pattern.matches(pattern4, user.get(i).getUsername().split("_")[1])){
                    weight = weight + 2;
                    user.get(i).setSimilarity(weight);
                }else if (Pattern.matches(pattern2, user.get(i).getUsername().split("_")[1])){
                    weight = weight + 1 - (double) Math.abs(user.get(i).getUsername().split("_")[1].length() - lastname.length())/20;
                    user.get(i).setSimilarity(weight);
                } else if (Pattern.matches(pattern3, user.get(i).getUsername().split("_")[1])){
                    weight = weight + 0.5 - (double) Math.abs(user.get(i).getUsername().split("_")[1].length() - lastname.length())/20;
                    user.get(i).setSimilarity(weight);
                }
            }
        }

        return user;
    }


    /**
     * 测试与实例化
     */

    public void testMate(){

        //查询关键字
        String key = "li_wei";

        //用户名表单
        List<String> user = new ArrayList<>();

        //带排序的用户名表单
        List<UserSort> userSorts = new ArrayList<UserSort>();

        //测试数据
        user.add("liu_dongwei");
        user.add("li_dawei1");
        user.add("li_weijun");
        user.add("li_mingwei3");
        user.add("li_weiliang2");
        user.add("li_wei");
        user.add("li_wei2");
        user.add("liu_wei1");

        for (int i = 0; i < user.size(); i++){
            userSorts.add(new UserSort(user.get(i)));
        }

        MateService mateService = new MateService();
        mateService.mateWeight(key, userSorts);

        Collections.sort(userSorts, (UserSort s, UserSort t1) -> {
            int diff = (int) Math.round((s.getSimilarity() - t1.getSimilarity()) * 1000);
            if (diff > 0){
                return -1;
            }else if (diff < 0){
                return 1;
            }
            return 0;
        });

        for (int i = 0; i < user.size(); i++){
            System.out.println(userSorts.get(i).getUsername());
        }
    }

    public static void main(String[] args) {

        MateService mateService = new MateService();
        mateService.testMate();
    }
}
