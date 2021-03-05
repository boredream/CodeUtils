package archex;

import utils.FileUtils;
import utils.StringUtils;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;

public class ApiDocUtils {

    static class ApiField {
        String name;
        String desc;
        String type;
        String scheme;

        public ApiField(String name, String desc, String type, String scheme) {
            this.name = name;
            this.desc = desc;
            this.type = type;
            this.scheme = scheme;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public static void main(String[] args) throws Exception {
//        docApi();
    }

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
        return param;
    }

    private static void docApi() {
        String str = FileUtils.readToString(new File("temp/apidoc/apidoc.txt"), "utf-8");

        // 逐行解析，保存数据
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
        String apiClassName = apiMethodName.replaceFirst("submit", "").replaceFirst("get", "");
        String requestType = apiClassName;
        String responseType = apiClassName;
        StringBuilder sbRequest = new StringBuilder();
        if ("POST".equals(httpMethod)) {
            // POST一般是body的参数，则接口这里直接取对象
            responseType = "String";
            requestType = apiClassName + "Request";
            sbRequest.append("\n        @Body ").append(requestType).append(" info");
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
                    responseType = param.scheme.replace("对象", "").trim();
                    if ("array".equals(param.type)) {
                        responseType = "ArrayList<" + responseType + ">";
                    }
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
            String className = requestType;
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

                // 获取某个类型下params集合
                ArrayList<ApiField> apiFields = apiParamsMap.computeIfAbsent(className, k -> new ArrayList<>());

                // 遇到对象，创建新的加入map
                if (param.scheme != null && param.scheme.contains("对象")) {
                    preClassNameStack.push(className);
                    className = param.scheme.replace("对象", "").trim();
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

                // 获取某个类型下params集合
                ArrayList<ApiField> apiFields = apiParamsMap.computeIfAbsent(className, k -> new ArrayList<>());

                // 遇到对象，创建新的加入map
                if (param.scheme != null && param.scheme.contains("对象")) {
                    preClassNameStack.push(className);
                    className = param.scheme.replace("对象", "").trim();
                }

                // 记录params字段
                apiFields.add(param);
            }
        }

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
                String type = getType(field);

                if (field.scheme != null && field.scheme.contains("对象")) {
                    type = field.scheme.replace("对象", "").trim();
                    if ("array".equals(field.type)) {
                        type = "ArrayList<" + type + ">";
                    }
                }

                if (StringUtils.hasChinese(field.desc)) {
                    sbClass.append("\t// ").append(field.desc).append("\n");
                }
                sbClass.append("\tprivate ").append(type).append(" ").append(field.name).append(";\n\n");
            }
            sbClass.append("}");

            FileUtils.writeString2File(sbClass.toString(), new File("temp/apidoc/clazz/" + className + ".java"), "utf-8");
        }
    }

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
        return type;

        // integer(int32)
    }

    private static void swaggerApi() {
        String str = "bodyStyleCdSh\tstring\n" +
                "allowEmptyValue: false\n" +
                "产品类型\n" +
                "\n" +
                "bottlesStockQty\tinteger($int32)\n" +
                "allowEmptyValue: false\n" +
                "库存量(瓶）\n" +
                "\n" +
                "boxStockQty\tinteger($int32)\n" +
                "allowEmptyValue: false\n" +
                "库存量(箱）\n" +
                "\n" +
                "createDate\tstring($date-time)\n" +
                "allowEmptyValue: false\n" +
                "创建日期\n" +
                "\n" +
                "flag\tinteger($int32)\n" +
                "allowEmptyValue: false\n" +
                "状态 1 使用 0 未使用\n" +
                "\n" +
                "maximumUnit\tstring\n" +
                "allowEmptyValue: false\n" +
                "基本单位- 大\n" +
                "\n" +
                "mfcStockHeaderUid\tinteger($int64)\n" +
                "allowEmptyValue: false\n" +
                "库存盘点UID\n" +
                "\n" +
                "minimumUnit\tstring\n" +
                "allowEmptyValue: false\n" +
                "基本单位 - 小\n" +
                "\n" +
                "saleSiebleUid\tstring\n" +
                "allowEmptyValue: false\n" +
                "工号\n" +
                "\n" +
                "skuName\tstring\n" +
                "allowEmptyValue: false\n" +
                "sku名称\n" +
                "\n" +
                "skuSiebleUid\tstring\n" +
                "allowEmptyValue: false\n" +
                "sku编码\n" +
                "\n" +
                "storeSiebleUid\tstring\n" +
                "allowEmptyValue: false\n" +
                "门店\n" +
                "\n" +
                "uid\tinteger($int64)\n" +
                "allowEmptyValue: false\n" +
                "主键\n" +
                "\n" +
                "unitConversion\tinteger($int64)\n" +
                "allowEmptyValue: false\n" +
                "单位换算\n" +
                "\n" +
                "updateDate\tstring($date-time)\n" +
                "allowEmptyValue: false\n" +
                "更新日期\n" +
                "\n" +
                "visitUid\tinteger($int64)\n" +
                "allowEmptyValue: false\n" +
                "拜访uid";
        str = str.replace("allowEmptyValue: false\n", "");
        List<String> list = new ArrayList<>();
        for (String line : str.split("\n")) {
            if (line.contains("\tstring")
                    || line.contains("\tnumber")
                    || line.contains("\tnumber")
                    || line.contains("\tinteger")
                    || line.contains("\tbool")) {
                String name = line.split("\t")[0];
                String type = line.split("\t")[1]
                        .replace("string", "String")
                        .replace("number", "double")
                        .replace("integer($int64)", "int")
                        .replace("integer($int32)", "String");
//                type = "String";
                list.add("private " + type + " " + name + ";");
            } else if (line.trim().length() > 0) {
                list.add("// " + line.trim());
            } else {
                list.add("");
            }
        }
        Collections.reverse(list);
        for (String s : list) {
            System.out.println(s);
        }
    }

}
