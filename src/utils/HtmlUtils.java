package utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

public class HtmlUtils {

    public static void main(String[] args) {
        File dir = new File("temp/jira/work/front");
        HashMap<String, Integer> userPointMap = new HashMap<>();
        for (File file : dir.listFiles()) {
            String content = FileUtils.readToString(file, "UTF-8");
            Document document = Jsoup.parse(content);
            parseHtml(document, userPointMap);
        }
        TreeMap<Integer, String> pointUserMap = new TreeMap<>();
        for (Map.Entry<String, Integer> entry : userPointMap.entrySet()) {
            pointUserMap.put(entry.getValue(), entry.getKey());
        }
        LinkedList<String> list = new LinkedList<>();
        for (Map.Entry<Integer, String> entry : pointUserMap.entrySet()) {
            list.addFirst(entry.getValue() + " : " + entry.getKey());
        }
        for (String s : list) {
            System.out.println(s);
        }
    }

    private static void parseHtml(Document document, HashMap<String, Integer> userPointMap) {
        // 接口类型总称 api-Search
        Elements trElements = document.getElementsByAttributeValue("class", "issuerow");
        // 全部类型接口
        for (Element tr : trElements) {
            try {
                Elements tdElements = tr.getElementsByTag("td");
                String user = tdElements.get(5).text().trim();
                int point = Integer.parseInt(tdElements.get(7).text().trim());
                Integer oldPoint = userPointMap.getOrDefault(user, 0);
                userPointMap.put(user, point + oldPoint);
            } catch (Exception e) {
                System.out.println(tr.text());
            }
        }
    }

}
