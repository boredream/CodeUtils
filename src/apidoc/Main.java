package apidoc;

import utils.FileUtils;
import utils.StringUtils;

import java.io.File;
import java.util.*;

public class Main {

    // 忽略解析的字段
    public static final List<String> ignoreFields = Arrays.asList("createDate", "updateDate", "curUserId");

    public static void main(String[] args) throws Exception {
        ArrayList<ApiInfo> apiInfos = SwaggerParse.parseDocFromUrl("http://localhost:8080/api/v2/api-docs", ignoreFields);
        genApiServiceCode(apiInfos);
    }

    private static void genApiServiceCode(ArrayList<ApiInfo> apiInfoList) {
        for (ApiInfo apiInfo : apiInfoList) {
            StringBuilder sbRequest = new StringBuilder();
            List<ApiField> requestParams = apiInfo.getRequestParams();
            List<ApiField> responseParams = apiInfo.getResponseParams();
            String responseType = "";
            // GET一般是query的参数，挨个拼接
            for (int i = 0; i < requestParams.size(); i++) {
                ApiField param = requestParams.get(i);
                if (i > 0) {
                    sbRequest.append(",");
                }
                if("Body".equalsIgnoreCase(param.in)) {
                    // POST一般是body的参数，则接口这里直接取对象
                    sbRequest.append("\n        @Body ").append(requestParams.get(0).schema).append(" info");
                } else {
                    sbRequest.append("\n        @").append(param.in).append("(\"").append(param.name).append("\") ").append(getType(param)).append(" ").append(param.name);
                }
            }

            // 清理response param，同时判断返回是否为array
            Iterator<ApiField> iterator = responseParams.iterator();
            for (; iterator.hasNext(); ) {
                ApiField param = iterator.next();
                if ("code".equals(param.name) || "msg".equals(param.name) || "success".equalsIgnoreCase(param.name)) {
                    iterator.remove();
                } else if ("data".equals(param.name)) {
                    responseType = getType(param);
                    iterator.remove();
                }
            }
            String api = "/**\n" +
                    " * %s\n" +
                    " */\n" +
                    "@%s(\"%s\")\n" +
                    "Observable<BaseResponse<%s>> %s(" +
                    "%s);";
            api = String.format(api, apiInfo.getName(), apiInfo.getMethod(), apiInfo.getUrl(), responseType, getApiMethodName(apiInfo), sbRequest.toString());
            System.out.println(api);
        }
    }

    private static String getApiMethodName(ApiInfo apiInfo) {
        String url = apiInfo.getUrl().replaceFirst("/api/", "");
        String[] strs = url.split("/");
        StringBuilder sb = new StringBuilder();
        sb.append(apiInfo.getMethod().toLowerCase());
        for (String str : strs) {
            if(str.contains("{")) {
                continue;
            }
            sb.append(StringUtils.firstToUpperCase(str));
        }
        return sb.toString();
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
