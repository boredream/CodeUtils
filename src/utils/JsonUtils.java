package utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import entity.ArrayType;
import entity.Json2JavaElement;

public class JsonUtils {

    public static void main(String[] args) {
        /// 读取json字符串
        String json = FileUtils.readToString(new File(
                "temp" + File.separator + "Json" + File.separator + "JsonString.json"), "UTF-8");
        parseJson2Java(json);
    }

    /**
     * 将json字符串转换为对应的javabean
     * <p>
     * <p>
     * 用法:<br>
     * 将json字符串拷贝至本项目中/Json/JsonString.txt 文件中去,然后调用该方法,<br>
     * 就会在本项目中/Json/JsonBean.java中生成一个对应的JavaBean类<br><br>
     * 注意:<br>
     * 如果json字符串中有null或者空集合[]这种无法判断类型的,会统一使用Object类型
     */
    public static void parseJson2Java(String jsonStr) {
        // 解析获取整个json结构集合
        List<Json2JavaElement> jsonBeanTree = getJsonBeanTree(jsonStr);

        // 利用获取到的json结构集合,创建对应的javabean文件内容
        String javaBeanStr = createJavaBean(jsonBeanTree);

        // 将生成的内容写入到文件中去
        FileUtils.writeString2File(javaBeanStr, new File(
                "temp" + File.separator + "Json" + File.separator + "JsonBean.java"));
    }

    /**
     * 将json字符串转换为对应的javabean
     *
     * @return 生成的javabean代码
     */
    public static String getJavaFromJson(String jsonStr) {
        // 解析获取整个json结构集合
        List<Json2JavaElement> jsonBeanTree = getJsonBeanTree(jsonStr);

        // 利用获取到的json结构集合,创建对应的javabean文件内容
        String javaBeanStr = createJavaBean(jsonBeanTree);

        return javaBeanStr;
    }

    /**
     * 根据解析好的数据创建生成对应的javabean类字符串
     *
     * @param jsonBeanTree 解析好的数据集合
     * @return 生成的javabean类字符串
     */
    public static String createJavaBean(List<Json2JavaElement> jsonBeanTree) {
        StringBuilder sb = new StringBuilder();
        StringBuilder sbGetterAndSetter = new StringBuilder();
        sb.append("\n");

        // 是否包含自定义子类
        boolean hasCustomeClass = false;
        List<String> customClassNames = new ArrayList<String>();

        // 由于在循环的时候有移除操作,所以使用迭代器遍历
        Iterator<Json2JavaElement> iterator = jsonBeanTree.iterator();
        while (iterator.hasNext()) {
            Json2JavaElement j2j = iterator.next();

            // 保存自定义类名称至集合中,注意已经包含的不再添加
            if (j2j.getCustomClassName() != null && !customClassNames.contains(j2j.getCustomClassName())) {
                customClassNames.add(j2j.getCustomClassName());
            }

            if (j2j.getParentJb() != null) {
                // 如果有parent,则为自定义子类,设置标识符不做其他操作
                hasCustomeClass = true;
            } else {
                // 如果不是自定义子类,则根据类型名和控件对象名生成变量申明语句
                genFieldd(sb, sbGetterAndSetter, j2j, 0);

                // 已经使用的数据会移除,则集合中只会剩下自定义子类相关的元素数据,将在后续的循环中处理
                iterator.remove();
            }
        }

        // 设置所有自定义类
        if (hasCustomeClass) {
            for (String customClassName : customClassNames) {
                // 根据名称申明子类

                // public class CustomClass {
                sb.append("\n");
                sb.append(StringUtils.formatSingleLine(1, "public static class " + customClassName + " {"));

                StringBuilder sbSubGetterAndSetter = new StringBuilder();
                // 循环余下的集合
                Iterator<Json2JavaElement> customIterator = jsonBeanTree.iterator();
                while (customIterator.hasNext()) {
                    Json2JavaElement j2j = customIterator.next();

                    // 根据当前数据的parent名称,首字母转为大写生成parent的类名
                    String parentClassName = StringUtils.firstToUpperCase(j2j.getParentJb().getName());

                    // 如果当前数据属于本次外层循环需要处理的子类
                    if (parentClassName.equals(customClassName)) {
                        // 根据类型名和控件对象名生成变量申明语句
                        genFieldd(sb, sbSubGetterAndSetter, j2j, 1);

                        // 已经使用的数据会移除,减少下一次外层循环的遍历次数
                        customIterator.remove();
                    }
                }

                sb.append(sbSubGetterAndSetter.toString());
                sb.append(StringUtils.formatSingleLine(1, "}"));
            }
        }

        sb.append(sbGetterAndSetter.toString());
        sb.append("\n");
        return sb.toString();
    }

    /**
     * 生成变量相关代码
     *
     * @param sb                添加申明变量部分
     * @param sbGetterAndSetter 添加getter和setter方法部分
     * @param j2j               变量信息
     * @param extraTabNum       额外缩进量\t
     */
    private static void genFieldd(StringBuilder sb, StringBuilder sbGetterAndSetter,
                                  Json2JavaElement j2j, int extraTabNum) {
        // 先判断是否有注释,有的话添加之
        // /**
        //  * 姓名
        //  */
        String des = j2j.getDes();
        if (des != null && des.length() > 0) {
            sb.append(StringUtils.formatSingleLine(1 + extraTabNum, "/**"));
            sb.append(StringUtils.formatSingleLine(1 + extraTabNum, " * " + des));
            sb.append(StringUtils.formatSingleLine(1 + extraTabNum, " */"));
        }

        // 申明变量
        // private String path;
        sb.append(StringUtils.formatSingleLine(1 + extraTabNum,
                "private " + getTypeName(j2j) + " " + j2j.getName() + ";"));

        // 生成变量对应的getter和setter方法
        // public String getName() {
        //     return path;
        // }
        sbGetterAndSetter.append("\n");
        sbGetterAndSetter.append(StringUtils.formatSingleLine(1 + extraTabNum,
                "public " + getTypeName(j2j) + " get" + StringUtils.firstToUpperCase(j2j.getName()) + "() {"));
        sbGetterAndSetter.append(StringUtils.formatSingleLine(2 + extraTabNum, "return " + j2j.getName() + ";"));
        sbGetterAndSetter.append(StringUtils.formatSingleLine(1 + extraTabNum, "}"));

        // public void setName(String path) {
        //     this.path = path;
        // }
        sbGetterAndSetter.append("\n");
        sbGetterAndSetter.append(StringUtils.formatSingleLine(1 + extraTabNum,
                "public void set" + StringUtils.firstToUpperCase(j2j.getName()) +
                        "(" + getTypeName(j2j) + " " + j2j.getName() + ") {"));
        sbGetterAndSetter.append(StringUtils.formatSingleLine(2 + extraTabNum,
                "this." + j2j.getName() + " = " + j2j.getName() + ";"));
        sbGetterAndSetter.append(StringUtils.formatSingleLine(1 + extraTabNum, "}"));
    }

    /**
     * 递归遍历整个json数据结构,保存至jsonBeans集合中
     *
     * @param jsonStr json字符串
     * @return 解析好的数据集合
     */
    public static List<Json2JavaElement> getJsonBeanTree(String jsonStr) {
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(jsonStr);

        // 根element可能是对象也可能是数组
        JsonObject rootJo = null;
        if (element.isJsonObject()) {
            rootJo = element.getAsJsonObject();
        } else if (element.isJsonArray()) {
            // 集合中如果有数据,则取第一个解析
            JsonArray jsonArray = element.getAsJsonArray();
            if (jsonArray.size() > 0) {
                rootJo = jsonArray.get(0).getAsJsonObject();
            }
        }

        jsonBeans = new ArrayList<Json2JavaElement>();
        recursionJson(rootJo, null);
        return jsonBeans;
    }

    /**
     * 保存递归获取到数据的集合
     */
    private static List<Json2JavaElement> jsonBeans = new ArrayList<Json2JavaElement>();

    /**
     * 递归获取json数据
     *
     * @param jo     当前递归解析的json对象
     * @param parent 已经解析好的上一级数据,无上一级时传入null
     */
    private static void recursionJson(JsonObject jo, Json2JavaElement parent) {
        if (jo == null) {
            return;
        }

        // 循环整个json对象的键值对
        for (Entry<String, JsonElement> entry : jo.entrySet()) {
            // json对象的键值对建构为 {"key":value}
            // 其中,值可能是基础类型,也可能是集合或者对象,先解析为json元素
            String name = entry.getKey();
            JsonElement je = entry.getValue();

            Json2JavaElement j2j = new Json2JavaElement();
            j2j.setName(name);
            if (parent != null) {
                j2j.setParentJb(parent);
            }

            // 获取json元素的类型,可能为多种情况,如下
            Class<?> type = getJsonType(je);
            if (type == null) {
                // 自定义类型

                // json键值的首字母转为大写,作为自定义类名
                j2j.setCustomClassName(StringUtils.firstToUpperCase(name));
                // ?
                j2j.setSouceJo(je.getAsJsonObject());
                jsonBeans.add(j2j);

                // 自定义类需要继续递归,解析自定义类中的json结构
                recursionJson(je.getAsJsonObject(), j2j);
            } else if (type.equals(JsonArray.class)) {
                // 集合类型

                // 重置集合数据,并获取当前json元素的集合类型信息
                deepLevel = 0;
                arrayType = new ArrayType();
                getJsonArrayType(je.getAsJsonArray());

                j2j.setArray(true);
                j2j.setArrayDeep(deepLevel);

                if (arrayType.getJo() != null) {
                    j2j.setCustomClassName(StringUtils.firstToUpperCase(name));
                    // 集合内的末点元素类型为自定义类, 递归
                    recursionJson(arrayType.getJo(), j2j);
                } else {
                    j2j.setType(arrayType.getType());
                }
                jsonBeans.add(j2j);
            } else {
                // 其他情况,一般都是String,int等基础数据类型

                j2j.setType(type);
                jsonBeans.add(j2j);
            }
        }
    }

    /**
     * 集合深度,如果是3则为ArrayList<ArrayList<ArrayList<>>>
     */
    private static int deepLevel = 0;
    /**
     * 集合类型数据,用于保存递归获取到的集合信息
     */
    private static ArrayType arrayType = new ArrayType();

    /**
     * 递归获取集合的深度和类型等信息
     *
     * @param jsonArray json集合数据
     */
    private static void getJsonArrayType(JsonArray jsonArray) {
        // 每次递归,集合深度+1
        deepLevel++;

        if (jsonArray.size() == 0) {
            // 如果集合为空,则集合内元素类型无法判断,直接设为Object
            arrayType.setArrayDeep(deepLevel);
            arrayType.setType(Object.class);
        } else {
            // 如果集合非空则取出第一个元素进行判断
            JsonElement childJe = jsonArray.get(0);

            // 获取json元素的类型
            Class<?> type = getJsonType(childJe);

            if (type == null) {
                // 自定义类型

                // 设置整个json对象,用于后续进行进一步解析处理
                arrayType.setJo(childJe.getAsJsonObject());
                arrayType.setArrayDeep(deepLevel);
            } else if (type.equals(JsonArray.class)) {
                // 集合类型

                // 如果集合里面还是集合,则递归本方法
                getJsonArrayType(childJe.getAsJsonArray());
            } else {
                // 其他情况,一般都是String,int等基础数据类型

                arrayType.setArrayDeep(deepLevel);
                arrayType.setType(type);
            }
        }
    }

    /**
     * 获取json元素的类型
     *
     * @param je json元素
     * @return 类型
     */
    private static Class<?> getJsonType(JsonElement je) {
        Class<?> clazz = null;

        if (je.isJsonNull()) {
            // 数据为null时,无法获取类型,则视为object类型
            clazz = Object.class;
        } else if (je.isJsonPrimitive()) {
            // primitive类型为基础数据类型,如String,int等
            clazz = getJsonPrimitiveType(je);
        } else if (je.isJsonObject()) {
            // 自定义类型参数则返回null,让json的解析递归进行进一步处理
            clazz = null;
        } else if (je.isJsonArray()) {
            // json集合类型
            clazz = JsonArray.class;
        }
        return clazz;
    }

    /**
     * 将json元素中的json基础类型,转换为String.class,int.class等具体的类型
     *
     * @param je json元素
     * @return 具体数据类型, 无法预估的类型统一视为Object.class类型
     */
    private static Class<?> getJsonPrimitiveType(JsonElement je) {
        Class<?> clazz = Object.class;
        JsonPrimitive jp = je.getAsJsonPrimitive();
        // json中的类型会将数字集合成一个总的number类型,需要分别判断
        if (jp.isNumber()) {
            String num = jp.getAsString();
            if (num.contains(".")) {
                // 如果包含"."则为小数,先尝试解析成float,如果失败则视为double
                try {
                    Float.parseFloat(num);
                    clazz = float.class;
                } catch (NumberFormatException e) {
                    clazz = double.class;
                }
            } else {
                // 如果不包含"."则为整数,先尝试解析成int,如果失败则视为long
                try {
                    Integer.parseInt(num);
                    clazz = int.class;
                } catch (NumberFormatException e) {
                    clazz = long.class;
                }
            }
        } else if (jp.isBoolean()) {
            clazz = boolean.class;
        } else if (jp.isString()) {
            clazz = String.class;
        }
        // json中没有其他具体类型如byte等
        return clazz;
    }

    /**
     * 获取类型名称字符串
     *
     * @param j2j 转换数据元素
     * @return 类型名称, 无法获取时, 默认Object
     */
    private static String getTypeName(Json2JavaElement j2j) {
        String name = "Object";

        Class<?> type = j2j.getType();
        if (j2j.getCustomClassName() != null && j2j.getCustomClassName().length() > 0) {
            // 自定义类,直接用自定义的名称customClassName
            name = j2j.getCustomClassName();
        } else {
            // 非自定义类即可以获取类型,解析类型class的名称,如String.class就对应String
            name = type.getName();
            int lastIndexOf = name.lastIndexOf(".");
            if (lastIndexOf != -1) {
                name = name.substring(lastIndexOf + 1);
            }
        }

        // 如果集合深度大于0,则为集合数据,根据深度进行ArrayList嵌套
        // 深度为3就是ArrayList<ArrayList<ArrayList<type>>>
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < j2j.getArrayDeep(); i++) {
            sb.append("ArrayList<");
        }
        sb.append(name);
        for (int i = 0; i < j2j.getArrayDeep(); i++) {
            sb.append(">");
        }
        return sb.toString();
    }
}
