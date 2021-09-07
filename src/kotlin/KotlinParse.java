package kotlin;

import utils.FileUtils;

import java.io.File;
import java.util.List;

public class KotlinParse {

    public static void main(String[] args) {

    }

    private static void kotlin2javabean() {
        List<File> files = FileUtils.getAllFiles("/Users/lcy/Documents/code/LoveCookBook/lib/entity");
        for (File file : files) {
            if (file.getAbsolutePath().contains(".g.dart")) {
                continue;
            }

            StringBuilder sb = new StringBuilder();
            String className = FileUtils.getName(file);

            sb.append("package com.boredream.springbootdemo.entity;\n");
            sb.append("\n");
            sb.append("import javax.persistence.Entity;\n");
            sb.append("\n");
            sb.append("@Entity\n");
            sb.append("public class ").append(className).append(" extends BaseEntity {\n");
            sb.append("\n");

            for (String line : FileUtils.readToStringLines(file)) {
                if (line.split(" ").length == 4) {
                    line = line.trim();
                    String type = line.split(" ")[0];
                    String name = line.split(" ")[1];

                    String javaType = null;
                    switch (type) {
                        case "double":
                        case "String":
                        case "int":
                            javaType = type;
                            break;
                        case "bool":
                            type = "boolean";
                            break;
                    }
                    if (type.startsWith("List<")) {
                        javaType = type;
                    }
                    if (javaType == null) {
                        continue;
                    }

                    sb.append("\tprivate ").append(javaType).append(" ").append(name).append("\n");
                }
            }
            sb.append("\n\n}\n");
            // System.out.println(sb.toString());
            FileUtils.writeString2File(sb.toString(), new File("temp/bean/" + className + ".java"), "utf-8");
        }
    }
}
