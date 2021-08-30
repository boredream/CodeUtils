package reptile;

import test.JsoupUtils;
import utils.FileUtils;
import utils.HttpUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HXSD {

    public static void main(String[] args) throws Exception {
        parse(true);
    }

    private static void parse(boolean justPrint) throws Exception {
        // String string = FileUtils.readToString(new File("temp/html/hxsd.html"), "utf-8");
        String string = HttpUtils.getString("http://me.hxsd.com/", getHeader());




        HashMap<String, String> nameUrlMap = new HashMap<>();
        for (String p : string.split(" <div class=\"ibox\">")) {
            String url;
            String name = "name" + nameUrlMap.size();

            String regex = "href=\"(.*?)\">";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(p);
            if (matcher.find()) {
                url = matcher.group(1);
            } else {
                continue;
            }

            String regex2 = "姓名：</strong--><span>(.*?)</span>";
            Pattern pattern2 = Pattern.compile(regex2);
            Matcher matcher2 = pattern2.matcher(p);
            if (matcher2.find()) {
                name = matcher2.group(1);
            }

            nameUrlMap.put(name, url);
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
                String name = "m1优秀作业-" + entry.getKey() + ".pdf";
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
        header.put("Cookie", "UM_distinctid=1796a0399071f4-03fcf35e1a40ac-37607201-384000-1796a039908b85; Hm_lvt_7c236d6a92ae1cc0a68da9d174500895=1620981310; Hm_lpvt_7c236d6a92ae1cc0a68da9d174500895=1620981352; pt_s_1d5c43ca=vt=1620981352710&cad=; pt_1d5c43ca=uid=F91HWSJoQ3R0Lcge-gdmow&nid=0&vid=baFYvgEQGPcknPdpV3lU9g&vn=2&pvn=1&sact=1620981994332&to_flag=1&pl=tzPy4kOK3YfaoFsgYZ0d6A*pt*1620981923713; PHPSESSID=asil7n0ct270ts4fun8ne42hd6; user_token_passport=a%3A1%3A%7Bs%3A5%3A%22token%22%3Bs%3A45%3A%22f75qo9og554wsa9446c8a2aae91ed9cf2684cada55405%22%3B%7D; student_info=a%3A4%3A%7Bs%3A10%3A%22student_id%22%3Bi%3A2523195%3Bs%3A12%3A%22student_name%22%3Bs%3A9%3A%22%E5%80%AA%E6%B0%B8%E6%81%92%22%3Bs%3A14%3A%22student_number%22%3Bs%3A13%3A%220021121070194%22%3Bs%3A5%3A%22token%22%3Bs%3A45%3A%22f75qo9og554wsa9446c8a2aae91ed9cf2684cada55405%22%3B%7D; csrf_cookie_name=898df323c1b0a204d9d8590a6c1a8e67; acw_tc=7b39758516274426377794889eeea52aedae727c8666a298f77f8be7e9a5d7; ci_session=a%3A5%3A%7Bs%3A10%3A%22session_id%22%3Bs%3A32%3A%22c38a8c38ba0c32c0f56fb2189d733f93%22%3Bs%3A10%3A%22ip_address%22%3Bs%3A14%3A%22123.57.117.133%22%3Bs%3A10%3A%22user_agent%22%3Bs%3A120%3A%22Mozilla%2F5.0+%28Macintosh%3B+Intel+Mac+OS+X+10_15_7%29+AppleWebKit%2F537.36+%28KHTML%2C+like+Gecko%29+Chrome%2F92.0.4515.107+Safari%2F537.3%22%3Bs%3A13%3A%22last_activity%22%3Bi%3A1627442638%3Bs%3A9%3A%22user_data%22%3Bs%3A0%3A%22%22%3B%7D06a1c5f403eaa100caa744c07b2d6470a3fe8ec7");
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
