package work;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utils.FileUtils;
import utils.ShUser;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

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
        String html = FileUtils.readToString(new File("src/work/confluence.html"), "UTF-8");
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

}
