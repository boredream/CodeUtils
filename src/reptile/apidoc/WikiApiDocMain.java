package reptile.apidoc;

import entity.RequestInfo;
import entity.RequestParam;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utils.FileUtils;
import utils.StringUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WikiApiDocMain {

    public static void main(String[] args) {
        String path = "temp" + File.separator + "apidoc" + File.separator
                + "wikiapi.txt";
        ArrayList<RequestInfo> infos = parseApiDoc(path);
        genRetrofitCode(infos);
    }

    public static ArrayList<RequestInfo> parseApiDoc(String path) {
        File file = new File(path);
        String response = FileUtils.readToString(file, "UTF-8");
        Document parse = Jsoup.parse(response);
        ArrayList<RequestInfo> requestInfos = new ArrayList<>();

        Elements names = parse.getElementsByTag("h2");
        ArrayList<String> titleList = new ArrayList<>();
        for(Element nameElement : names) {
            String title = nameElement.text();
            Pattern pattern = Pattern.compile("[\\d]{1,2}");
            Matcher matcher = pattern.matcher(title);
            if(matcher.find(0)) {
                titleList.add(title);
            }
        }

        Elements rootElements = parse.getElementsByTag("table");
        for (Element rootElement : rootElements) {
            if (!rootElement.attr("class").contains("confluenceTable")) {
                continue;
            }

            if (rootElement.child(0).childNodeSize() < 2) {
                continue;
            }

            Elements tds = rootElement.getElementsByTag("td");
            if (tds.size() == 0) {
                continue;
            }

            RequestInfo requestInfo = new RequestInfo();

            // url
            Element urlElement = tds.get(0);
            String url = urlElement.text();

            List<String> paramsFromUrl = new ArrayList<>();
            // get类型url可能包含?
            if(url.contains("?")) {
                int getIndex = url.indexOf("?");
                for (String s : url.substring(getIndex+1).split("&")) {
                    paramsFromUrl.add(s.split("=")[0]);
                }
                url = url.substring(0, getIndex);
            }
            requestInfo.setUrl(url);

            // name
            requestInfo.setName("");

            // method
            String method = tds.get(1).text();
            requestInfo.setMethod(method);

            // params
            ArrayList<RequestParam> params = new ArrayList<>();
            Elements paramsElements = tds.get(2).getElementsByTag("tr");
            for (int i = 1; i < paramsElements.size(); i++) {
                Element paramElement = paramsElements.get(i);
                String name = paramElement.child(0).text();
                String type = "String";
                String desc = paramElement.child(1).text();
                String restType;
                if(url.contains("{" + name + "}")) {
                    restType = "Path";
                } else if(method.equalsIgnoreCase("get")) {
                    restType = "Query";
                } else {
                    restType = "Body";
                }
                RequestParam param = new RequestParam(name, type, desc, null);
                param.setRestType(restType);

                params.add(param);

                // 如果请求参数表中，已经有参数，则先删除。没有的话在后面补上
                if(paramsFromUrl.contains(name)) {
                    paramsFromUrl.remove(name);
                }
            }

            for(String param : paramsFromUrl) {
                // 只从url获取到的信息有限，类型写死为String
                params.add(new RequestParam(param, "String", "", null));
            }

            requestInfo.setParams(params);

            boolean has = false;
            for(RequestInfo ri : requestInfos) {
                if(ri.getUrl().equals(requestInfo.getUrl()) && ri.getMethod().equals(requestInfo.getMethod())) {
                    has = true;
                    break;
                }
            }
            if(!has) {
                requestInfos.add(requestInfo);
            }
        }

        for (int i = 0; i < requestInfos.size(); i++) {
            if(i < titleList.size()) {
                requestInfos.get(i).setName(titleList.get(i));
            }
        }

        return requestInfos;
    }

    private static void genRetrofitCode(ArrayList<RequestInfo> infos) {
        String baseUrl = "http://dev-api.qbaolive.com/v1/";

        for (RequestInfo info : infos) {
            StringBuilder sb = new StringBuilder();

            // 方式注释里参数
            StringBuilder sbAnnotation = new StringBuilder();
            ArrayList<RequestParam> params = info.getParams();
            if (params != null && params.size() > 0) {
                sbAnnotation.append(StringUtils.formatSingleLine(1, " *"));

                ArrayList<RequestParam> bodyParams = new ArrayList<>();
                for(RequestParam param : info.getParams()) {
                    if(param.getRestType().equals("Body")) {
                        bodyParams.add(param);
                    } else {
                        // 方式注释里参数
                        // * @param name 姓名
                        sbAnnotation.append(StringUtils.formatSingleLine(
                                1, " * @param " + param.getName() + " " + param.getDes()));
                    }
                }

                if(bodyParams.size() > 0) {
                    StringBuilder sbBodyParams = new StringBuilder();
                    for (int i = 0; i < bodyParams.size(); i++) {
                        RequestParam param = bodyParams.get(i);
                        if(i == 0) {
                            sbAnnotation.append(StringUtils.formatSingleLine(
                                    1, " * @param body " + param.getName() + " " + param.getDes()));
                        } else {
                            sbAnnotation.append(StringUtils.formatSingleLine(
                                    1, " *             " + param.getName() + " " + param.getDes()));
                        }
                        sbBodyParams.append(",").append(param.getName());
                    }
                }
            }
            sb.append(StringUtils.formatSingleLine(1, "/**"));
            sb.append(StringUtils.formatSingleLine(1, " * " + info.getName()));
            if(sbAnnotation.length() > 0) {
                sb.append(sbAnnotation.toString());
            }
            sb.append(StringUtils.formatSingleLine(1, " */"));

            String url = info.getUrl();
            String str1 = String.format("@%s(\"%s\")",
                    info.getMethod(),
                    url.replaceFirst(baseUrl, ""));
            sb.append(StringUtils.formatSingleLine(1, str1));

            url = url.replaceAll("/\\{[a-zA-Z0-9]+\\}", "");
            String methodName = url.substring(url.lastIndexOf("/") + 1);
            String str2 = String.format("Observable<HttpResult<String>> %s(", methodName);
            sb.append(StringUtils.formatSingleLine(1, str2));

            if(info.getParams().size() > 0) {
                ArrayList<RequestParam> bodyParams = new ArrayList<>();
                for(RequestParam param : info.getParams()) {
                    if(param.getRestType().equals("Body")) {
                        bodyParams.add(param);
                    } else {
                        String p = String.format("@%s(\"%s\") String %s,", param.getRestType(), param.getName(), param.getName());
                        sb.append(StringUtils.formatSingleLine(3, p));
                    }
                }

                if(bodyParams.size() > 0) {
                    sb.append(StringUtils.formatSingleLine(3, "@Body Object body);"));
                } else {
                    sb.replace(sb.lastIndexOf(","), sb.length(), ");");
                }
            } else {
                sb.replace(sb.length()-1, sb.length(), ");");
            }

            sb.append("\n");

            System.out.println(sb.toString());
        }
    }

    private static void genCode(ArrayList<RequestInfo> infos) {
        StringBuilder sb = new StringBuilder();
        for (RequestInfo info : infos) {

            String urlName = info.getName();
            String url = info.getUrl();

            // 方式注释里参数
            StringBuilder sbAnotation = new StringBuilder();
            // 方法参数里参数
            StringBuilder sbParam = new StringBuilder();
            // 方法内容里参数
            StringBuilder sbBody = new StringBuilder();

            ArrayList<RequestParam> params = info.getParams();
            if (params != null && params.size() > 0) {
                sbAnotation.append(StringUtils.formatSingleLine(1, " *"));
                for (RequestParam param : params) {
                    // 方式注释里参数
                    // * @param name 姓名
                    sbAnotation.append(StringUtils.formatSingleLine(
                            1,
                            " * @param " + param.getName() + " "
                                    + param.getDes()));

                    // 方法参数里参数 String phone, HttpListener<Object> listener
                    sbParam.append(param.getType() + " " + param.getName()
                            + ", ");

                    // 方法内容里参数 params.put("phone", phone);
                    sbBody.append(StringUtils.formatSingleLine(
                            2,
                            "params.put(\"" + param.getName() + "\", "
                                    + param.getName() + ");"));
                }
                sbAnotation.append("\n");
            }

            sb.append(StringUtils.formatSingleLine(1, "/**"));
            sb.append(StringUtils.formatSingleLine(1, " * " + info.getDes()));
            sb.append(sbAnotation.toString());
            sb.append(StringUtils.formatSingleLine(1, " */"));

            // 方法名
            String methodName = "request";
            String[] nameItems = url.split("/");
            for (int i = nameItems.length - 1; i > 0; i--) {
                if (nameItems[i].contains("{")) {
                    continue;
                }

                methodName = nameItems[i];
                break;
            }

            sb.append(StringUtils.formatSingleLine(1, "public static void "
                    + methodName + "("
                    + sbParam.toString() + ") {"));

            // url
            sb.append(StringUtils.formatSingleLine(2, "String url = \"" + info.getUrl() + "\";"));

            String method = "Request.Method." + info.getMethod();

            // 参数，如果是post放到map里，如果是get则拼接到url里
            if (info.getMethod().equals("post")) {
                sb.append(StringUtils.formatSingleLine(2,
                        "HashMap<String, Object> params = new HashMap<String, Object>();"));
                sb.append(sbBody.toString());
                sb.append(StringUtils.formatSingleLine(2,
                        "executeRequest(" + method + ", url, params, Object.class, listener, errorListener);"));
            } else {
                for (RequestParam param : info.getParams()) {
                    sb.append(StringUtils.formatSingleLine(2,
                            "url = url.replace(\"{" + param.getName() + "}\", String.valueOf(" + param.getName() + "));"));
                }
                sb.append(StringUtils.formatSingleLine(2,
                        "executeRequest(" + method + ", url, null, Object.class, listener, errorListener);"));
            }

            sb.append(StringUtils.formatSingleLine(1, "}"));
            sb.append("\n");

        }
        System.out.println(sb.toString());
    }

}
