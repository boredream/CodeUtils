package apidoc;

import utils.FileUtils;
import utils.StringUtils;

import java.io.File;
import java.util.*;

public class ApiDocUtils {

    // 忽略解析的字段
    public static final List<String> ignoreFields = Arrays.asList("createDate", "updateDate", "curUserId");

    public static void main(String[] args) throws Exception {
        TreeMap<String, ArrayList<ApiField>> apiParamsMap = parseApiCode();
        genClass(apiParamsMap);
//        genIosClass(apiParamsMap);
    }

    // 解析接口数据
    private static TreeMap<String, ArrayList<ApiField>> parseApiCode() {
        // 读取接口信息
        String str = FileUtils.readToString(new File("temp/apidoc/apidoc.txt"), "utf-8");

        // 解析字符串，提取接口具体信息（名称、地址、method、request字段、response字段等）
        String title = "";
        String url = "";
        String httpMethod = "";
        boolean request = false;
        boolean response = false;
        List<ApiField> requestParams = new ArrayList<>();
        List<ApiField> responseParams = new ArrayList<>();
        for (String line : str.split("\n")) {
            line = line.trim();
            if (line.startsWith("## ")) {
                title = line.substring(2).trim();
                continue;
            } else if (line.contains("**接口地址**:")) {
                request = false;
                response = false;
                url = line.replace("**接口地址**:", "").replace("`", "").trim();
                url = url.replaceFirst("/sfa-app/", "");
                continue;
            } else if (line.contains("**请求方式**:")) {
                httpMethod = line.replace("**请求方式**:", "").replace("`", "").trim();
                continue;
            } else if (line.contains("**请求参数**:")) {
                request = true;
            } else if (line.contains("**响应参数**:")) {
                request = false;
                response = true;
            }

            if (request) {
                ApiField param = parseParam(false, line);
                if (param != null) {
                    requestParams.add(param);
                }
            } else if (response) {
                ApiField param = parseParam(true, line);
                if (param != null) {
                    responseParams.add(param);
                }
            }
        }

        // 开始拼接代码

        // Api
        String apiMethodName = url.split("/")[url.split("/").length - 1];
        String apiClassName = StringUtils.firstToUpperCase(apiMethodName
                .replaceFirst("submit", "")
                .replaceFirst("get", ""));
        String responseType = apiClassName;
        StringBuilder sbRequest = new StringBuilder();
        if ("POST".equals(httpMethod)) {
            // POST一般是body的参数，则接口这里直接取对象
            responseType = "String";
            sbRequest.append("\n        @Body ").append(apiClassName).append(" info");
        } else {
            // GET一般是query的参数，挨个拼接
            for (int i = 0; i < requestParams.size(); i++) {
                ApiField param = requestParams.get(i);
                if (i > 0) sbRequest.append(",");
                sbRequest.append("\n        @Query(\"").append(param.name).append("\") ").append(getType(param)).append(" ").append(param.name);
            }

            // 清理response param，同时判断返回是否为array
            Iterator<ApiField> iterator = responseParams.iterator();
            for (; iterator.hasNext(); ) {
                ApiField param = iterator.next();
                if ("code".equals(param.name) || "msg".equals(param.name)) {
                    iterator.remove();
                } else if ("data".equals(param.name)) {
                    responseType = getType(param);
                    iterator.remove();
                }
            }
        }
        String api = "/**\n" +
                " * %s\n" +
                " */\n" +
                "@%s(\"%s\")\n" +
                "Observable<BaseResponse<%s>> %s(" +
                "%s);";
        api = String.format(api, title, httpMethod, url, responseType, apiMethodName, sbRequest.toString());
        System.out.println(api);

        // request bean
        TreeMap<String, ArrayList<ApiField>> apiParamsMap = new TreeMap<>();
        if ("POST".equals(httpMethod)) {
            // body的一般是对象，需要对应创建
            Stack<String> preClassNameStack = new Stack<>();
            String className = apiClassName;
            // 当前深度，根据前面 &emsp; 数量判断
            int currentDeep = 0;
            for (ApiField param : requestParams) {
                int deep = 0;
                String name = param.name;
                while (name.startsWith("&emsp;")) {
                    deep++;
                    name = name.replaceFirst("&emsp;", "");
                }
                param.name = name;

                // 如果深度变小了，代表一个对象遍历结束了。取上一个遍历的对象
                if (deep < currentDeep) {
                    className = preClassNameStack.pop();
                }
                currentDeep = deep;

                // 获取某个类型下params集合
                ArrayList<ApiField> apiFields = apiParamsMap.computeIfAbsent(className, k -> new ArrayList<>());

                // 遇到对象，创建新的加入map
                if (param.schema != null) {
                    preClassNameStack.push(className);
                    className = param.schema;
                }

                // 记录params字段
                apiFields.add(param);
            }
        }

        // response bean
        if ("GET".equals(httpMethod)) {
            // response的一般是对象/array，需要对应创建
            Stack<String> preClassNameStack = new Stack<>();
            String className = apiClassName;

            // 当前深度，根据前面 &emsp; 数量判断
            // data 内容起始深度2
            int currentDeep = 2;
            for (ApiField param : responseParams) {
                int deep = 0;
                String name = param.name;
                while (name.startsWith("&emsp;")) {
                    deep++;
                    name = name.replaceFirst("&emsp;", "");
                }
                param.name = name;

                // 如果深度变小了，代表一个对象遍历结束了。取上一个遍历的对象
                if (deep < currentDeep) {
                    className = preClassNameStack.pop();
                }
                currentDeep = deep;

                // 获取某个类型下params集合
                ArrayList<ApiField> apiFields = apiParamsMap.computeIfAbsent(className, k -> new ArrayList<>());

                // 遇到对象，创建新的加入map
                if (param.schema != null) {
                    preClassNameStack.push(className);
                    className = param.schema;
                }

                // 记录params字段
                if (!apiFields.contains(param)) {
                    // 可能多个字段对应同一个class，会有重复
                    apiFields.add(param);
                }
            }
        }

        return apiParamsMap;
    }

    // 生成实体类
    private static void genClass(TreeMap<String, ArrayList<ApiField>> apiParamsMap) {
        for (Map.Entry<String, ArrayList<ApiField>> entry : apiParamsMap.entrySet()) {
            String className = entry.getKey();
            StringBuilder sbClass = new StringBuilder();
            sbClass.append("package com.archex.core.entity;\n")
                    .append("\n")
                    .append("import java.io.Serializable;\n")
                    .append("import java.util.ArrayList;\n")
                    .append("\n")
                    .append("public class ").append(className).append(" implements Serializable {\n\n");
            for (ApiField field : entry.getValue()) {
                if (ignoreFields.contains(field.name)) {
                    continue;
                }

                String type = getType(field);
                if (StringUtils.hasChinese(field.desc)) {
                    sbClass.append("\t// ").append(field.desc).append("\n");
                }
                sbClass.append("\tprivate ").append(type).append(" ").append(field.name).append(";\n\n");
            }
            sbClass.append("}");

            FileUtils.writeString2File(sbClass.toString(), new File("temp/apidoc/clazz/" + className + ".java"), "utf-8");
        }
    }

    // 生成 iOS实体类
    private static void genIosClass(TreeMap<String, ArrayList<ApiField>> apiParamsMap) {
        StringBuilder sbClass = new StringBuilder();
        for (Map.Entry<String, ArrayList<ApiField>> entry : apiParamsMap.entrySet()) {
            String className = entry.getKey();
            sbClass.append("class " + className + ": NSObject, Codable {\n");
            for (ApiField field : entry.getValue()) {
                if (ignoreFields.contains(field.name)) {
                    continue;
                }

                String type = getIosType(field);
                if (StringUtils.hasChinese(field.desc)) {
                    sbClass.append("\t/// ").append(field.desc).append("\n");
                }
                sbClass.append("\tvar ").append(field.name).append(": ").append(type).append("?\n\n");
            }
            sbClass.append("}\n\n");
        }

        FileUtils.writeString2File(sbClass.toString(), new File("temp/apidoc/iosClass/classTxt.txt"), "utf-8");
    }

    // 获取类型
    private static String getType(ApiField field) {
        String type = field.type;
        if (type.contains("string") || field.name.contains("uid") || field.name.contains("Uid")) {
            type = "String";
        } else if ("integer(int64)".equals(type)) {
            type = "long";
        } else if (type.contains("integer")) {
            type = "int";
        } else if ("number".equals(type)) {
            type = "double";
        }

        if (field.schema != null) {
            type = field.schema;
            if ("array".equals(field.type)) {
                type = "ArrayList<" + type + ">";
            } else if ("ListResponse".equals(field.type)) {
                type = "ListResponse<" + type + ">";
            }
        }

        return type;
    }

    // 获取iOS类型
    private static String getIosType(ApiField field) {
        String type = field.type;
        if (type.contains("string") || field.name.contains("uid") || field.name.contains("Uid")) {
            type = "String";
        } else if ("integer(int64)".equals(type)) {
            type = "Int";
        } else if (type.contains("integer")) {
            type = "Int";
        } else if ("number".equals(type)) {
            type = "Double";
        }

        if (field.schema != null) {
            type = field.schema.replace("对象", "").trim();
            if ("array".equals(field.type)) {
                type = "[" + type + "]";
            }
        }

        // TODO: chunyang 2021/7/28  ListResponse

        return type;
    }

    // 解析接口参数
    private static ApiField parseParam(boolean response, String line) {
        ApiField param = null;
        if (line.startsWith("|") &&
                !line.contains("参数名称") &&
                !line.contains("token|header") &&
                !line.contains(" -------- ")) {
            // 先记录所有参数
            line = line.substring(1, line.length() - 1).trim();
            String[] strs = line.split("\\|");
            if (response) {
                if (strs.length >= 3) {
                    String name = strs[0];
                    String desc = strs[1];
                    String type = strs[2];
                    String schema = strs.length >= 4 ? strs[3] : null;
                    param = new ApiField(name, desc, type, schema);
                }
            } else {
                if (strs.length >= 5) {
                    String name = strs[0];
                    String desc = strs[1];
                    String requestType = strs[2];
                    String type = strs[4];
                    String schema = strs.length >= 6 ? strs[5] : null;
                    param = new ApiField(name, desc, type, schema);
                }
            }
        }

        // 初步加工参数
        if (param != null) {
            if (param.type.contains("Page«")) {
                param.type = "ListResponse";
            }

            if (param.schema != null) {
                param.schema = param.schema
                        .replace("对象", "")
                        .replace("Page«", "")
                        .replace("»", "")
                        .trim();
            }
        }

        return param;
    }

}
