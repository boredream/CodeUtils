package test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import test.JsoupUtils.RequestInfo.RequestParam;
import utils.AndroidUtils;
import utils.FileUtils;

public class JsoupUtils {

	public static void main(String[] args) {
		ArrayList<RequestInfo> infos = parseApiDoc("temp" + File.separator + "api_search.txt");
//		ArrayList<RequestInfo> infos = parseApiDoc("temp" + File.separator + "api_stone.txt");
//		ArrayList<RequestInfo> infos = parseApiDoc("temp" + File.separator + "api_company.txt");
		genCode(infos);
	}

	private static void genCode(ArrayList<RequestInfo> infos) {
		StringBuilder sb = new StringBuilder();
		for(RequestInfo info : infos) {
			
//			/**
//			 * Search - 搜索 石种
//			 */
//			public static final String SEARCH_STONE = "/search/stone";
			
			// url地址后缀
			String urlEnd = info.getUrlEnd();
			// 替换/为_同时转大写
			String name = info.getUrlEnd().replace("/", "_").toUpperCase(Locale.CHINA).substring(1);
			
			sb.append(AndroidUtils.formatSingleLine(1, "/**"));
			sb.append(AndroidUtils.formatSingleLine(1, " * " + info.getDes()));
			sb.append(AndroidUtils.formatSingleLine(1, " */"));
			sb.append(AndroidUtils.formatSingleLine(1, "public static final String " 
					+ name + " = \"" + urlEnd + "\";"));
			sb.append("\n");
			
			
//			HashMap<String, Object> postParams = new HashMap<String, Object>();
//			postParams.put("page", page);
//			postParams.put("keyword", searchKey);
			
			sb.append(AndroidUtils.formatSingleLine(2, "HashMap<String, Object> postParams = new HashMap<String, Object>();"));
			for(RequestParam param : info.getParams()) {
				sb.append(AndroidUtils.formatSingleLine(2, 
						"postParams.put(\"" + param.getName() + "\", " + param.getName() + ");"
								+ " // " + param.getDes()));
			}
			
			System.out.println("----------------------");
		}
		System.out.println(sb.toString());
	}

	public static ArrayList<RequestInfo> parseApiDoc(String path) {
		File file = new File(path);
		String response = FileUtils.readToString(file, "UTF-8");
		Document parse = Jsoup.parse(response);
		// 接口类型总称 api-Search
		String mainName = parse.getElementsByTag("section").get(0).attr("id");
//		System.out.println("-> type = " + mainName);
//		System.out.println();
		ArrayList<RequestInfo> requestInfos = new ArrayList<RequestInfo>();
		// 全部类型接口
		for (Element e : parse.getAllElements()) {
			String attrId = e.attr("id");
			// div的标签,且id名前缀为mainName
			// 为类型下单个接口 api-Search-PostSearchCompany
			if (e.tagName().equals("div") && attrId.startsWith(mainName)) {
				RequestInfo requestInfo = new RequestInfo();

//				System.out.println("---> name = " + attrId);
				requestInfo.setName(attrId);

				// method post/get
				String method = e.getElementsByTag("pre").get(0)
						.attr("data-type");
//				System.out.println("-----> method = " + method);
				requestInfo.setMethod(method);

				// url
				String url = e.getElementsByAttributeValue("class", "pln")
						.get(0).text();
//				System.out.println("-----> url = " + url);
				requestInfo.setUrl(url);
				
				// des
				String des = e.getElementsByAttributeValue("class", "pull-left")
						.get(0).text();
//				System.out.println("-----> des = " + des);
				requestInfo.setDes(des);

				// post params
				Element ePostParams = e.getElementsByTag("table").get(0);
				ArrayList<RequestParam> params = new ArrayList<RequestParam>();
				for (Element ePostParam : ePostParams.getElementsByTag("tr")) {
					// param 字段
					Elements eColumn = ePostParam.getElementsByTag("td");
					if (eColumn.size() == 0) {
						continue;
					}
					
					// 标签"选项"
//					String label = ePostParam.getElementsByAttributeValue("class", "label label-optional")
//							.get(0).text();
					String label = "选项";
					
					// 第一个字段为参数名
					String paramName = eColumn.get(0).text();
					// 去除标签
					paramName = paramName.replace(label, "").trim();
					// 第二个字段为参数类型
					// 可能类型为 String Number Float
					String paramType = eColumn.get(1).text();
					// 第三个字段为参数描述
					String paramDes = eColumn.get(2).text();
//					System.out.println("-----> param = " + paramName + " ... "
//							+ paramType + " ... " + paramDes);
					RequestParam param = new RequestParam(paramName, paramType,
							paramDes);
					params.add(param);
				}

				requestInfo.setParams(params);
				requestInfos.add(requestInfo);
//				System.out.println();
			}
		}
		
		return requestInfos;
	}

	static class RequestInfo {
		private String name;
		private String method;
		private String url;
		private String des;
		private ArrayList<RequestParam> params;

		static class RequestParam {
			private String name;
			private String type;
			private String des;

			public RequestParam(String name, String type, String des) {
				this.name = name;
				this.type = type;
				this.des = des;
			}

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public String getType() {
				return type;
			}

			public void setType(String type) {
				this.type = type;
			}

			public String getDes() {
				return des;
			}

			public void setDes(String des) {
				this.des = des;
			}
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getMethod() {
			return method;
		}

		public void setMethod(String method) {
			this.method = method;
		}

		public String getUrl() {
			return url;
		}
		
		// TODO
		public String getUrlEnd() {
			String host = "http://short.server.stone-chat.com";
			return url.replace(host, "");
		}

		public void setUrl(String url) {
			this.url = url;
		}
		
		public String getDes() {
			return des;
		}

		public void setDes(String des) {
			this.des = des;
		}

		public ArrayList<RequestParam> getParams() {
			return params;
		}

		public void setParams(ArrayList<RequestParam> params) {
			this.params = params;
		}

	}
}
