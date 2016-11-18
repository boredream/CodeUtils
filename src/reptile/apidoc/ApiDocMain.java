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

public class ApiDocMain {

	public static void main(String[] args) {
		String path = "temp" + File.separator + "apidoc" + File.separator
				+ "apidoc.txt";
		ArrayList<RequestInfo> infos = parseApiDoc(path);
		genCode(infos);
	}

	private static void genCode(ArrayList<RequestInfo> infos) {
		StringBuilder sbUrls = new StringBuilder();
		StringBuilder sb = new StringBuilder();
		for (RequestInfo info : infos) {

			String urlName = info.getName();
			String url = info.getUrl();

			sbUrls.append(StringUtils.formatSingleLine(1, "/**"));
			sbUrls.append(StringUtils.formatSingleLine(1,
					" * " + info.getDes()));
			sbUrls.append(StringUtils.formatSingleLine(1, " */"));
			sbUrls.append(StringUtils.formatSingleLine(1,
					"public static final String " + urlName + " = \"" + url
							+ "\";"));
			sbUrls.append("\n");

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
			sbParam.append("HttpListener<Object> listener");

			sb.append(StringUtils.formatSingleLine(1, "/**"));
			sb.append(StringUtils.formatSingleLine(1, " * " + info.getDes()));
			sb.append(sbAnotation.toString());
			sb.append(StringUtils.formatSingleLine(1, " */"));

			StringBuilder methodNameSb = new StringBuilder();
			String[] nameItems = url.substring(1).split("[/_]");
			for (int i = 0; i < nameItems.length; i++) {
				String nameItem = nameItems[i];
				if (i > 0 && nameItem.length() > 0) {
					nameItem = nameItem.substring(0, 1).toUpperCase()
							+ nameItem.substring(1);
				}
				methodNameSb.append(nameItem);
			}

			sb.append(StringUtils.formatSingleLine(1, "public static void "
					+ methodNameSb.toString() + "("
					+ sbParam.toString() + ") {"));
			sb.append(StringUtils.formatSingleLine(2,
							"HashMap<String, Object> params = new HashMap<String, Object>();"));
			sb.append(sbBody.toString());
			sb.append(StringUtils.formatSingleLine(2,
					"doHttp(URLs.getUrl(URLs." + urlName + ")"
					+ ", \"" + info.getMethod() + "\", params, Object.class, listener);"));
			sb.append(StringUtils.formatSingleLine(1, "}"));
			sb.append("\n");

		}
		System.out.println(sbUrls.toString());
		System.out.println("--------------");
		System.out.println(sb.toString());
	}

	public static ArrayList<RequestInfo> parseApiDoc(String path) {
		File file = new File(path);
		String response = FileUtils.readToString(file, "UTF-8");
		Document parse = Jsoup.parse(response);
		ArrayList<RequestInfo> requestInfos = new ArrayList<RequestInfo>();
		// 接口类型总称 api-Search
		Elements rootElements = parse.getElementsByTag("section");
		for(Element rootElement : rootElements) {
			// 全部类型接口
			Elements docElements = rootElement.getElementsByTag("article");
			for (Element e : docElements) {
				RequestInfo requestInfo = new RequestInfo();
				
				// method post/get
				String method = "post";
				// System.out.println("-----> method = " + method);
				requestInfo.setMethod(method);
				
				String host = "http://short-server.cloudchat.stone-chat.com";
				
				// url
				String url = e.getElementsByAttributeValue("class", "pln").get(0)
						.text().replace(host, "");
				// System.out.println("-----> url = " + url);
				requestInfo.setUrl(url);
				
				// System.out.println("---> name = " + attrId);
				String name = url.substring(1).replace("/", "_").toUpperCase();
				requestInfo.setName(name);
				
				// des
				String des = e.getElementsByAttributeValue("class", "pull-left")
						.get(0).text();
				// System.out.println("-----> des = " + des);
				requestInfo.setDes(des);
				
				// post params
				Elements paramsElements = e.getElementsByTag("table");
				ArrayList<RequestParam> params = new ArrayList<RequestParam>();
				if (paramsElements.size() > 0) {
					Element ePostParams = paramsElements.get(0);
					for (Element ePostParam : ePostParams.getElementsByTag("tr")) {
						// param 字段
						Elements eColumn = ePostParam.getElementsByTag("td");
						if (eColumn.size() == 0) {
							continue;
						}
						
						// 第一个字段为参数名
						String paramName = eColumn.get(0).text();
						paramName = paramName.replace(" ", "");
						paramName = paramName.replace("選項", "");
						paramName = paramName.trim();
						// 第二个字段为参数类型
						// 可能类型为 String Number Float
						String paramType = eColumn.get(1).text();
						// 第三个字段为参数描述
						String paramDes = eColumn.get(2).text();
						// System.out.println("-----> param = " + paramName +
						// " ... "
						// + paramType + " ... " + paramDes);
						RequestParam param = new RequestParam(paramName, paramType,
								paramDes, null);
						params.add(param);
					}
				}
				
				requestInfo.setParams(params);
				requestInfos.add(requestInfo);
				// System.out.println();
			}
		}

		return requestInfos;
	}

}
