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
import java.util.ArrayList;

public class WikiApiDocMain {

    public static void main(String[] args) {
        String path = "temp" + File.separator + "apidoc" + File.separator
                + "wikiapi.txt";
        ArrayList<RequestInfo> infos = parseApiDoc(path);
        genCode(infos);
    }

    public static ArrayList<RequestInfo> parseApiDoc(String path) {
        File file = new File(path);
        String response = FileUtils.readToString(file, "UTF-8");
        Document parse = Jsoup.parse(response);
        ArrayList<RequestInfo> requestInfos = new ArrayList<>();
        // 接口类型总称 api-Search
        Elements rootElements = parse.getElementsByTag("table");
        for (Element rootElement : rootElements) {
            if (!rootElement.attr("class").contains("confluenceTable")) {
                continue;
            }

            if (rootElement.child(0).childNodeSize() != 4) {
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
            requestInfo.setUrl(url);

            // method
            String method = tds.get(1).text();
            requestInfo.setMethod(method);

            // params
            ArrayList<RequestParam> params = new ArrayList<>();
            Elements paramsElements = tds.get(2).getElementsByTag("tr");
            for (int i = 1; i < paramsElements.size(); i++) {
                Element paramElement = paramsElements.get(i);
                String name = paramElement.child(0).text();
                String type = paramElement.child(1).text();
                String desc = paramElement.child(2).text();

                params.add(new RequestParam(name, type, desc, null));
            }
            requestInfo.setParams(params);

            requestInfos.add(requestInfo);
        }

        return requestInfos;
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
            }
            sbParam.append("Listener<Object> listener, ErrorListener errorListener");

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
