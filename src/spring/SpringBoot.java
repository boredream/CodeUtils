package spring;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import utils.FileUtils;
import utils.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SpringBoot {

    public static void main(String[] args) {

    }

    private static void csvReorder() {
        ArrayList<String> lines = FileUtils.readToStringLines(new File("temp/db/diary.txt"));
        StringBuilder sb;
        for (String line : lines) {
            sb = new StringBuilder();
            String[] tabs = line.replace("NULL", "").split(",");

            sb.append("\"").append(tabs[0]).append("\"").append(",");
            sb.append("\"").append(tabs[1]).append("\"").append(",");
            sb.append("\"").append(tabs[2]).append("。").append(tabs[5]).append("\"").append(",");
            sb.append("\"").append(tabs[3]).append("\"").append(",");

            // tabs 6 ~ x 可能是图片被拆分了
            if (tabs[6].contains("\"")) {
                sb.append("\"");
                for (int i = 6; i < tabs.length - 2; i++) {
                    if (i > 6) {
                        sb.append(",");
                    }
                    sb.append(tabs[i].replace("\"", ""));
                }
                sb.append("\"").append(",");
            } else {
                sb.append("\"").append(tabs[6]).append("\"").append(",");
            }

            sb.append("\"").append(tabs[tabs.length - 2]).append("\"").append(",");
            sb.append("\"").append(tabs[tabs.length - 1]).append("\"");
            System.out.println(sb);
        }

        // 源数据格式
        // 209,2,在一起！!,2020-02-05,NULL,成功入社！百年企业第一步！,,2021-09-30 09:51:49,2021-09-30 14:05:32,
        // 222,2,考拉第一次带我去针灸！,2020-08-29,NULL,之前吃消炎药效果都一般，找了个厉害的针灸！第一次体验！神奇！感觉有点效果！,"https://6c6f-lovecookbook-7gjn846l3db07924-1253175673.tcb.qcloud.la/flutter/image_picker_522A9DF0-C088-479E-A930-E179317F972A-15901-00004EA48953D48D.jpg,https://6c6f-lovecookbook-7gjn846l3db07924-1253175673.tcb.qcloud.la/flutter/image_picker_8645BBF2-D725-4EE5-ACD6-927F6FDE672B-15901-00004EA48DDB725D.jpg",2021-09-30 09:51:49,2021-09-30 09:51:49

        // 目标格式
        // "id","user_id","content","diary_date","images","create_time","update_time"
        // "1","1","啊啊啊","2022-06-27","https://file.papikoala.cn/image1656323246048.jpg","2022-06-27 17:47:27","2022-06-27 17:47:27"
    }

    private static void method() {
        ArrayList<String> lines = FileUtils.readToStringLines(new File("temp/database2.json"));
        for (String line : lines) {
            JsonParser parser = new JsonParser();
            JsonElement je = parser.parse(line);
            JsonObject jo = je.getAsJsonObject();
            // done,type,name,detail,todo_date,images
            int done = 0;
            if (jo.has("done") && !jo.get("done").isJsonNull()) {
                done = jo.get("done").getAsBoolean() ? 1 : 0;
            }

            String type = jo.get("type").getAsString();
            String name = jo.get("name").getAsString();
            String detail = jo.get("desc").getAsString();

            String todo_date = "";
            if (jo.has("todoDate") && !jo.get("todoDate").isJsonNull()) {
                todo_date = jo.get("todoDate").getAsString();
            }

            if (jo.has("doneDate") && !jo.get("doneDate").isJsonNull()) {
                String done_date = jo.get("doneDate").getAsString();
                if (done_date != null) todo_date = done_date;
            }

            String images = "";
            StringBuilder sbImages = new StringBuilder();
            if (jo.has("images")) {
                for (JsonElement jeImage : jo.get("images").getAsJsonArray()) {
                    String image = jeImage.getAsString();
                    sbImages.append(",").append(image);
                }
            }
            if (sbImages.length() > 0) {
                images = sbImages.substring(1);
            }
            System.out.printf("%d#%s#%s#%s#%s#%s\n", done, type, name, detail, todo_date, images);
        }
    }

    private static void parseCloudBase2CSV() {
        ArrayList<String> lines = FileUtils.readToStringLines(new File("temp/database.json"));
        for (String line : lines) {
            JsonParser parser = new JsonParser();
            JsonElement je = parser.parse(line);
            JsonObject jo = je.getAsJsonObject();
            // name,detail,the_day_date,images
            String name = jo.get("name").getAsString();
            String detail = jo.get("desc").getAsString();
            String the_day_date = jo.get("theDayDate").getAsString();
            String images = "";
            StringBuilder sbImages = new StringBuilder();
            if (jo.has("images")) {
                for (JsonElement jeImage : jo.get("images").getAsJsonArray()) {
                    String image = jeImage.getAsString();
                    sbImages.append(",").append(image);
                }
            }
            if (sbImages.length() > 0) {
                images = sbImages.substring(1);
            }
            System.out.printf("%s#%s#%s#%s\n", name, detail, the_day_date, images);
        }
    }

    private static List<String> getEntityNames() {
        String path = "/Users/lcy/Documents/code/SpringBootDemo/src/main/java/com/boredream/springbootdemo/entity";
        List<File> files = FileUtils.getAllFiles(path);
        List<String> nameList = new ArrayList<>();
        for (File file : files) {
            String name = FileUtils.getName(file);
            nameList.add(name);
        }
        return nameList;
    }

    private static void genControllerByEntity() {
        String code = "" +
                "package com.boredream.springbootdemo.controller;\n" +
                "\n" +
                "import com.boredream.springbootdemo.entity.#{bean};\n" +
                "import com.boredream.springbootdemo.repo.#{bean}Repository;\n" +
                "import org.springframework.beans.factory.annotation.Autowired;\n" +
                "import org.springframework.web.bind.annotation.*;\n" +
                "\n" +
                "@RestController\n" +
                "public class #{bean}Controller {\n" +
                "\n" +
                "    @Autowired\n" +
                "    private #{bean}Repository repository;\n" +
                "\n" +
                "    @PostMapping(path = \"/#{url}\")\n" +
                "    public @ResponseBody\n" +
                "    String addNew#{bean}(@RequestBody #{bean} info) {\n" +
                "        repository.save(info);\n" +
                "        return \"Saved\";\n" +
                "    }\n" +
                "\n" +
                "    @GetMapping(path = \"/#{url}\")\n" +
                "    public @ResponseBody\n" +
                "    Iterable<#{bean}> getAll#{bean}s() {\n" +
                "        return repository.findAll();\n" +
                "    }\n" +
                "\n" +
                "}";

        for (String name : getEntityNames()) {
            String fileStr = code.replace("#{bean}", name)
                    .replace("#{url}", StringUtils.firstToLowerCase(name));
            FileUtils.writeString2File(fileStr, new File("temp/spring/controller/" + name + "Controller.java"), "utf-8");
        }
    }

    private static void genRepoByEntity() {
        String code = "" +
                "package com.boredream.springbootdemo.repo;\n" +
                "\n" +
                "import com.boredream.springbootdemo.entity.#{bean};\n" +
                "import org.springframework.data.repository.CrudRepository;\n" +
                "\n" +
                "public interface #{bean}Repository extends CrudRepository<#{bean}, Integer> {\n" +
                "\n" +
                "}";

        for (String name : getEntityNames()) {
            String fileStr = code.replace("#{bean}", name);
            FileUtils.writeString2File(fileStr, new File("temp/spring/repo/" + name + "Repository.java"), "utf-8");
        }
    }


}
