package spring;

import utils.FileUtils;
import utils.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SpringBoot {

    public static void main(String[] args) {
        genControllerByEntity();
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
