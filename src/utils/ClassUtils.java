package utils;

import entity.ClassInfo;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClassUtils {
    public static ClassInfo readClassInfo(File file) {
        ClassInfo info = new ClassInfo();
        String fileContent = FileUtils.readToString(file, "UTF-8");

        Matcher matcherAnotation = Pattern.compile(
                "(/\\*\\*[\\s\\S]+?\\*/)?[\\s]*public class ([\\S]+)").matcher(fileContent);
        if (matcherAnotation.find()) {
            if (matcherAnotation.group(1) != null) {
                info.annotation = matcherAnotation.group(1)
                        .replace("/**", "")
                        .replace("*/", "")
                        .replace("*", "")
                        .trim();
                info.annotation = info.annotation.split("\\s")[0];
            } else {
                info.annotation = null;
            }
            info.className = matcherAnotation.group(2).trim();

            fileContent = fileContent.replace(matcherAnotation.group(), "");
        }

        Matcher matcherFields = Pattern.compile(
                "(/\\*\\*[\\s\\S]+?\\*/)?[\\s]*(private|public) ([a-zA-Z_0-9]+) ([a-zA-Z_0-9]+)?;").matcher(fileContent);
        List<ClassInfo.ClassField> fields = new ArrayList<>();
        while (matcherFields.find()) {
            ClassInfo.ClassField cf = new ClassInfo.ClassField();
            if (matcherFields.group(1) != null) {
                cf.annotation = matcherFields.group(1)
                        .replace("/**", "")
                        .replace("*/", "")
                        .replace("*", "")
                        .trim();
                // // TODO: 2016/11/19 应该支持更多类型标签
                if (cf.annotation.contains("[where]")) {
                    cf.isWhere = true;
                    cf.annotation = cf.annotation.replace("[where]", "").trim();
                }

                if(cf.annotation.contains("[id]")) {
                    cf.isPrimaryKey = true;
                    cf.annotation = cf.annotation.replace("[id]", "").trim();
                }
            }
            cf.scope = matcherFields.group(2);
            cf.type = matcherFields.group(3);
            cf.name = matcherFields.group(4);

            fields.add(cf);
        }
        info.fields = fields;

        return info;
    }

    /**
     * 成员变量是否为可以存至数据库的类型
     * <p>
     * <p>支持八大基础类型,日期类型,String型
     *
     * @param field
     * @return
     */
    public static boolean isDBableType(Field field) {
        boolean isDBableType = false;
        Class<?> clazz = field.getType();
        // 注意:isPrimitive返回true的条件是八大基础类型 boolean, byte, char, short,
        // int, long, float, double 加上 void.
        // 这里排除void类型
        if (clazz.equals(void.class) || clazz.equals(Void.class)) {
            isDBableType = false;
        } else {
            isDBableType = clazz.isPrimitive()
                    // String不属于八大基础类型
                    || clazz.equals(String.class)
                    || clazz.equals(java.util.Date.class)
                    || clazz.equals(java.sql.Date.class);
        }
        return isDBableType;
    }

    /**
     * 将八大基础类型以及String和Date类型转为数据库对应参数类型(boolean和date都作为字符类型处理)
     */
    public static String parsePri2DBType(ClassInfo.ClassField field) {
        String type;
        switch (field.type) {
            case "int":
            case "Integer":
            case "long":
            case "Long":
                type = "INTEGER";
                break;
            case "float":
            case "Float":
            case "double":
            case "Double":
                type = "REAL";
                break;
            case "boolean":
            case "Boolean":
                type = "INTEGER";
                break;
            default:
                type = "TEXT";
                break;
        }
        return type;
    }
}
