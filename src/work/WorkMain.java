package work;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utils.FileUtils;
import utils.ShUser;
import utils.HttpUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class WorkMain {

    static class TaskInfo {
        String story;
        String storyName;
        String taskName;
        String person;
        String personNumber;
        int sp;

        @Override
        public String toString() {
            return "TaskInfo{" +
                    "story='" + story + '\'' +
                    ", taskName='" + taskName + '\'' +
                    ", person='" + person + '\'' +
                    ", personNumber='" + personNumber + '\'' +
                    ", sp=" + sp +
                    '}';
        }
    }

    public static void main(String[] args) {
        // 从网站获取HTML内容
        String html = getHtmlFromWeb();
        if (html == null) {
            System.out.println("获取网页内容失败");
            return;
        }
        
        Document doc = Jsoup.parse(html);
        
        List<TaskInfo> taskList = new ArrayList<>();
        String story = null;
        String storyName = null;
        String storyUser = null;

        // 获取表格内容
        Elements rows = doc.select("table.confluenceTable tr");
        for (Element row : rows) {
            // 跳过表头
            if (row.select("th").size() > 0) {
                continue;
            }

            Elements cells = row.select("td");
            if (cells.isEmpty()) {
                continue;
            }

            Element firstCell = cells.first();
            String firstValue = firstCell.text().trim();
            
            // 判断是否是故事行(灰色背景且包含JIRA编号)
            if (firstCell.hasClass("highlight-grey") && firstValue.contains("A2-")) {
                // 解析故事编号和名称
                story = firstValue.split(" - ")[0].trim();
                storyName = firstValue.split(" - ").length > 1 ? firstValue.split(" - ")[1].trim() : "";
                
                // 获取故事行的负责人
                Element lastCell = cells.last();
                if (lastCell != null && !lastCell.text().trim().isEmpty()) {
                    storyUser = lastCell.text().trim();
                }
                continue;
            }

            // 跳过空行
            if (firstValue.isEmpty()) {
                continue;
            }

            // 解析任务行
            String taskName = firstValue;
            
            // 获取SP值(倒数第二列)
            String spStr = cells.size() >= 2 ? cells.get(cells.size() - 2).text().trim() : "0";
            int sp = 0;
            try {
                sp = Integer.parseInt(spStr);
            } catch (NumberFormatException e) {
                // SP不是数字则跳过
                continue;
            }

            // 获取负责人(最后一列)
            String taskUser = cells.last().text().trim();
            String personStr = taskUser.isEmpty() ? storyUser : taskUser;
            
            // 如果没有指定负责人则跳过
            if (personStr == null || personStr.isEmpty()) {
                continue;
            }

            // 处理多个负责人的情况
            for (String person : personStr.split("[ ,]")) {
                if (person.trim().isEmpty()) {
                    continue;
                }
                TaskInfo taskInfo = new TaskInfo();
                taskInfo.story = story;
                taskInfo.storyName = storyName;
                taskInfo.taskName = taskName;
                taskInfo.person = person;
                taskInfo.sp = sp;
                taskInfo.personNumber = ShUser.getUserNumber(person);
                taskList.add(taskInfo);
            }
        }

        // 按人员统计数据
        HashMap<String, HashSet<String>> userStoryMap = new HashMap<>();
        for (TaskInfo taskInfo : taskList) {
            HashSet<String> stories = userStoryMap.getOrDefault(taskInfo.person, new HashSet<>());
            stories.add(taskInfo.story);
            userStoryMap.put(taskInfo.person, stories);
        }

        HashMap<String, Integer> userTotalSpMap = new HashMap<>();
        for (TaskInfo taskInfo : taskList) {
            userTotalSpMap.put(taskInfo.person, userTotalSpMap.getOrDefault(taskInfo.person, 0) + taskInfo.sp);
            System.out.println(taskInfo.taskName +
                    "\t" + taskInfo.story +
                    "\t" + taskInfo.storyName +
                    "\t" + taskInfo.person +
                    "\t" + taskInfo.personNumber +
                    "\t" + taskInfo.sp +
                    "\t" + // 开始时间
                    "\t" + // 结束时间
                    "\t" + "前端");
        }

        // 输出统计结果
        userTotalSpMap.forEach((name, sp) -> {
            System.out.println("姓名：" + name);
            System.out.println("SP：" + sp);
            System.out.println("故事：" + userStoryMap.get(name).size());
            System.out.println();
        });
    }

    private static String getHtmlFromWeb() {
        try {
            System.out.println("开始获取网页内容...");
            
            // 从配置文件读取Cookie
            Properties props = new Properties();
            String configPath = "src/work/config.properties";
            System.out.println("正在读取配置文件: " + configPath);
            
            props.load(new FileInputStream(configPath));
            String cookie = props.getProperty("confluence.cookie");
            System.out.println("Cookie长度: " + (cookie != null ? cookie.length() : 0));
            
            Map<String, String> headers = new HashMap<>();
            headers.put("Cookie", cookie);
            headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
            headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            headers.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
            
            String url = "http://confluence.shinho.net.cn/pages/viewpage.action?pageId=117943117";
            System.out.println("正在请求URL: " + url);
            
            // 设置超时时间
            System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
            System.setProperty("sun.net.client.defaultReadTimeout", "10000");
            
            String result = HttpUtils.getString(url, headers);
            System.out.println("请求完成，返回内容长度: " + (result != null ? result.length() : 0));
            
            return result;
        } catch (Exception e) {
            System.out.println("获取网页内容出错: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

}
