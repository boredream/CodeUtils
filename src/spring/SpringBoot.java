package spring;

import apidoc.ApiField;
import apidoc.ApiInfo;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import utils.FileUtils;
import utils.StringUtils;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpringBoot {

    public static void main(String[] args) {
        String info = "[\n" +
                "  {\n" +
                "    \"key\": \"customer_r#corporate_concern\",\n" +
                "    \"title\": \"客户等级\",\n" +
                "    \"necessary\": true,\n" +
                "    \"onlyRead\": true,\n" +
                "    \"type\": \"select\",\n" +
                "    \"selectModule\": \"customer\",\n" +
                "    \"selectItem\": \"customer_level\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"key\": \"customer_r#format\",\n" +
                "    \"title\": \"业态\",\n" +
                "    \"necessary\": true,\n" +
                "    \"type\": \"select\",\n" +
                "    \"selectModule\": \"customer_restaurant\",\n" +
                "    \"selectItem\": \"business_type\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"type\": \"divider\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"key\": \"customer_r#main_features\",\n" +
                "    \"title\": \"主营特色\",\n" +
                "    \"necessary\": true,\n" +
                "    \"type\": \"select\",\n" +
                "    \"selectModule\": \"customer_restaurant\",\n" +
                "    \"selectItem\": \"food_type\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"key\": \"customer_r#store_type\",\n" +
                "    \"title\": \"门店类型\",\n" +
                "    \"necessary\": true,\n" +
                "    \"onlyRead\": true,\n" +
                "    \"type\": \"select\",\n" +
                "    \"selectModule\": \"customer_restaurant\",\n" +
                "    \"selectItem\": \"store_type\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"key\": \"customer_r#atmosphere\",\n" +
                "    \"title\": \"氛围\",\n" +
                "    \"necessary\": true,\n" +
                "    \"type\": \"select\",\n" +
                "    \"selectModule\": \"customer_restaurant\",\n" +
                "    \"selectItem\": \"atmosphere\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"key\": \"customer_r#main_cuisine\",\n" +
                "    \"title\": \"酒店主营菜系\",\n" +
                "    \"necessary\": true,\n" +
                "    \"type\": \"select\",\n" +
                "    \"selectModule\": \"global\",\n" +
                "    \"selectItem\": \"main_cuisine\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"key\": \"customer_r#meal_classification\",\n" +
                "    \"title\": \"餐饮分类\",\n" +
                "    \"necessary\": true,\n" +
                "    \"type\": \"select\",\n" +
                "    \"selectModule\": \"customer_restaurant\",\n" +
                "    \"selectItem\": \"meal_type\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"key\": \"customer_r#customer_groups\",\n" +
                "    \"title\": \"顾客群体\",\n" +
                "    \"necessary\": true,\n" +
                "    \"type\": \"select\",\n" +
                "    \"selectModule\": \"customer_restaurant\",\n" +
                "    \"selectItem\": \"crowd_type\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"type\": \"divider\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"key\": \"customer_r#hotel_area\",\n" +
                "    \"title\": \"酒店总面积\",\n" +
                "    \"necessary\": true,\n" +
                "    \"type\": \"input\",\n" +
                "    \"inputFilter\": \"number\",\n" +
                "    \"unit\": \"㎡\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"key\": \"customer_r#table_quantity\",\n" +
                "    \"title\": \"餐位数量\",\n" +
                "    \"necessary\": true,\n" +
                "    \"type\": \"input\",\n" +
                "    \"inputFilter\": \"number\",\n" +
                "    \"unit\": \"个\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"key\": \"customer_r#cooker_quantity\",\n" +
                "    \"title\": \"厨师总数\",\n" +
                "    \"necessary\": true,\n" +
                "    \"type\": \"input\",\n" +
                "    \"inputFilter\": \"number\",\n" +
                "    \"unit\": \"人\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"type\": \"divider\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"key\": \"customer_r#turnover\",\n" +
                "    \"title\": \"日均餐饮营业额\",\n" +
                "    \"necessary\": true,\n" +
                "    \"type\": \"input\",\n" +
                "    \"inputFilter\": \"number\",\n" +
                "    \"unit\": \"元\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"key\": \"customer_r#spices_month\",\n" +
                "    \"title\": \"全部调味品月开销金额\",\n" +
                "    \"necessary\": false,\n" +
                "    \"type\": \"input\",\n" +
                "    \"inputFilter\": \"number\",\n" +
                "    \"unit\": \"元\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"key\": \"customer_r#spices_limt_time\",\n" +
                "    \"title\": \"调味品采购周期\",\n" +
                "    \"necessary\": false,\n" +
                "    \"type\": \"input\",\n" +
                "    \"inputFilter\": \"number\",\n" +
                "    \"unit\": \"天\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"key\": \"customer_r#consumption\",\n" +
                "    \"title\": \"人均消费\",\n" +
                "    \"necessary\": true,\n" +
                "    \"type\": \"input\",\n" +
                "    \"inputFilter\": \"number\",\n" +
                "    \"unit\": \"元\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"key\": \"customer_r#set_occupancy\",\n" +
                "    \"title\": \"上座率\",\n" +
                "    \"necessary\": true,\n" +
                "    \"type\": \"input\",\n" +
                "    \"inputFilter\": \"number\",\n" +
                "    \"unit\": \"％\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"key\": \"customer_r#customer_relationship\",\n" +
                "    \"title\": \"客情关系分类\",\n" +
                "    \"necessary\": false,\n" +
                "    \"type\": \"select\",\n" +
                "    \"selectModule\": \"customer\",\n" +
                "    \"selectItem\": \"customer_relationship\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"type\": \"divider\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"key\": \"customer_r#ming_display\",\n" +
                "    \"title\": \"明档陈列\",\n" +
                "    \"necessary\": false,\n" +
                "    \"type\": \"input\",\n" +
                "    \"inputFilter\": \"number\",\n" +
                "    \"unit\": \"个\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"key\": \"customer_r#variety_display\",\n" +
                "    \"title\": \"菜品展示区陈列\",\n" +
                "    \"necessary\": false,\n" +
                "    \"type\": \"input\",\n" +
                "    \"inputFilter\": \"number\",\n" +
                "    \"unit\": \"个\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"key\": \"customer_r#special_display\",\n" +
                "    \"title\": \"特殊形象陈列\",\n" +
                "    \"necessary\": false,\n" +
                "    \"type\": \"input\",\n" +
                "    \"inputFilter\": \"number\",\n" +
                "    \"unit\": \"个\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"key\": \"customer_r#table_display\",\n" +
                "    \"title\": \"桌上陈列\",\n" +
                "    \"necessary\": false,\n" +
                "    \"type\": \"input\",\n" +
                "    \"inputFilter\": \"number\",\n" +
                "    \"unit\": \"个\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"key\": \"customer_r#shelf_display\",\n" +
                "    \"title\": \"货架陈列\",\n" +
                "    \"necessary\": false,\n" +
                "    \"type\": \"input\",\n" +
                "    \"inputFilter\": \"number\",\n" +
                "    \"unit\": \"个\"\n" +
                "  }\n" +
                "]";
        List<HashMap<String, String>> list = new Gson().fromJson(info, new TypeToken<List<HashMap<String, String>>>() {
        }.getType());
        for (HashMap<String, String> map : list) {
            if(!map.containsKey("title")) continue;
            String necessary = map.getOrDefault("necessary", "false");
            System.out.println(("true".equals(necessary)?"必填":"非必填") + "\t\t" + map.get("title"));
        }
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
