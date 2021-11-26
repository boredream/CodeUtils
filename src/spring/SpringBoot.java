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
