package reptile;

import com.sun.istack.internal.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import utils.FileUtils;
import utils.HttpUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HXSD {

    public static void main(String[] args) throws Exception {
        parse("m4", false);
    }

    private static void parse(@NotNull String pre, boolean justPrint) throws Exception {
         String string = FileUtils.readToString(new File("temp/html/hxsd.html"), "utf-8");
//        String string = HttpUtils.getString("http://me.hxsd.com/", getHeader());

        Document parse = Jsoup.parse(string);
        HashMap<String, String> nameUrlMap = new HashMap<>();

        // 五星作品榜
        Element element5x = parse.getElementsByAttributeValueMatching("class", "banner_wrap").get(0);
        for (Element element : element5x.getElementsByAttributeValueMatching("class", "ibox")) {
            String name = element.getElementsByTag("p").get(0).text();
            String link = element.getElementsByTag("a").get(0).attr("href");
            name = pre + "-五星-" + name;
            nameUrlMap.put(name, link);
        }

        // 优秀作品榜
        Element elementGood = parse.getElementsByAttributeValueMatching("class", "banner_wrap").get(1);
        for (Element element : elementGood.getElementsByAttributeValueMatching("class", "ibox")) {
            String name = element.getElementsByTag("p").get(0).text();
            String link = element.getElementsByTag("a").get(0).attr("href");
            name = pre + "-优秀-" + name;
            nameUrlMap.put(name, link);
        }

        for (Map.Entry<String, String> entry : nameUrlMap.entrySet()) {
            String url = entry.getValue();
            String html = HttpUtils.getString(url, getHeader());

            // <a style="font-size:12px; color:#0066cc;" href="https://newstudent-public.oss-cn-beijing.aliyuncs.com/ueditor/application/162504998579453628.pdf" title="162504998579453628.pdf">162504998579453628.pdf</a>
            String regex = "href=\"(.*?pdf)\" title";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(html);
            if (matcher.find()) {
                // 下载
                String name = entry.getKey() + ".pdf";
                String fileUrl = matcher.group(1);
                if(justPrint) {
                    System.out.println(name + " = " + fileUrl);
                } else {
                    downloadFile(name, fileUrl);
                }
            }
        }
    }

    private static HashMap<String, String> getHeader() {
        HashMap<String, String> header = new HashMap<>();
        header.put("Cookie", "UM_distinctid=1796a0399071f4-03fcf35e1a40ac-37607201-384000-1796a039908b85; PHPSESSID=asil7n0ct270ts4fun8ne42hd6; user_token_passport=a:1:{s:5:\"token\";s:45:\"f75qo9og554wsa9446c8a2aae91ed9cf2684cada55405\";}; student_info=a:4:{s:10:\"student_id\";i:2523195;s:12:\"student_name\";s:9:\"倪永恒\";s:14:\"student_number\";s:13:\"0021121070194\";s:5:\"token\";s:45:\"f75qo9og554wsa9446c8a2aae91ed9cf2684cada55405\";}; Hm_lvt_7c236d6a92ae1cc0a68da9d174500895=1629699758; Hm_lpvt_7c236d6a92ae1cc0a68da9d174500895=1629699904; pt_1d5c43ca=uid=F91HWSJoQ3R0Lcge-gdmow&nid=0&vid=UnyJpDP1k6U7YzKx9ayd6w&vn=5&pvn=1&sact=1630984733930&to_flag=0&pl=8bfZ9mfokuUF0fCvAbyN8A*pt*1630984733930; pt_s_1d5c43ca=vt=1630984733930&cad=; acw_tc=7b39758316359334899614916e22409c87fdf26a0583ea2a981d7fcef08acc; csrf_cookie_name=dd0c92a924da9b7c39803cd34186e7e9; ci_session=a:5:{s:10:\"session_id\";s:32:\"0b3fae25bb69e1f87fc7462dd3904378\";s:10:\"ip_address\";s:14:\"123.57.117.131\";s:10:\"user_agent\";s:120:\"Mozilla/5.0+(Macintosh;+Intel+Mac+OS+X+10_15_7)+AppleWebKit/537.36+(KHTML,+like+Gecko)+Chrome/94.0.4606.81+Safari/537.36\";s:13:\"last_activity\";i:1635933492;s:9:\"user_data\";s:0:\"\";}e6acc9545d1f0355032f6ca93e0272d025677026");
        header.put("Host", "me.hxsd.com");
        return header;
    }

    private static void downloadFile(String name, String fileUrl) throws Exception {
        File file = new File("temp/html", name);

        byte[] bytes = HttpUtils.getOrPostFile(HttpUtils.Method.GET, fileUrl, null, null);
        FileUtils.writeBytes2File(bytes, file);

        System.out.println("download success " + name);
    }
}
