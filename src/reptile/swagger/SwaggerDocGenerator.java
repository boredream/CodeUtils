package reptile.swagger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import reptile.swagger.SwaggerDocGenerator.RequestInfo.RequestParam;
import utils.AndroidUtils;
import utils.FileUtils;

public class SwaggerDocGenerator {

	public static void main(String[] args) {
		ArrayList<RequestInfo> infos = parseDocFromHtml("temp" + File.separator + "apidoc" + File.separator + "swagger.txt");
//		genCode(infos);
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

	public static ArrayList<RequestInfo> parseDocFromHtml(String path) {
		ArrayList<RequestInfo> requestInfos = new ArrayList<RequestInfo>();
		
		File file = new File(path);
		String response = FileUtils.readToString(file, "UTF-8");
		Document parse = Jsoup.parse(response);
		// 总接口类型 
		// <li id="resource_用户相关api" class="resource">
		Elements typeElements = parse.getElementsByClass("resource");
		for(Element typeElement : typeElements) {
			// id为 "resource_用户相关api"
			String apiTitle = typeElement.id().split("_")[1];
			if(!apiTitle.endsWith("用户相关api")) {
				continue;
			}
			
			// 获取该类型下具体接口
			// <ul class="operations">
			Elements apiElements = typeElement.getElementsByClass("operations");
			
			for(Element apiElement : apiElements) {
				RequestInfo requestInfo = new RequestInfo();
				
				// header
				Element headingElement = apiElement.getElementsByClass("heading").get(0);
				
				// 接口名称
				// <ul class="options">
				String name = headingElement.getElementsByClass("options").get(0).text();
				requestInfo.setName(name);
				
				// 接口方式
				// <ul class="http_method">
				String method = headingElement.getElementsByClass("http_method").get(0).text();
				requestInfo.setMethod(method);
				
				// 接口地址
				// <ul class="path">
				String url = headingElement.getElementsByClass("path").get(0).text();
				requestInfo.setUrl(url);
				
				// content
				Element contentElement = apiElement.getElementsByClass("content").get(0);
				
				// 提交参数
				// <tbody class="operation-params">
				Elements paramsElements = contentElement.getElementsByClass("operation-params");
				if(paramsElements != null && paramsElements.size() > 0) {
					// 有参数才做处理
					Element paramsElement = paramsElements.get(0);
					
					// 一行对应一个参数
					// <tr>
					Elements paramElements = paramsElement.getElementsByTag("tr");
					
					List<RequestParam> params = new ArrayList<RequestParam>();
					for(Element paramElement : paramElements) {
						// 一列对应参数的一个属性,共5列,分别为 名称,值,描述,参数类型,数据类型
						// <td>
						Elements e = paramElement.getAllElements();
						
						if(e.size() == 5) {
							String pType = e.get(3).text();
							if(pType.equals("query")) {
								// 如果参数类型为query,则为数据类型为基础类型
								String paramName = e.get(0).text();
								String paramDes = e.get(2).text();
								String paramType = e.get(4).text();
								params.add(new RequestParam(paramName, paramType, paramDes));
							} else if(pType.equals("body")) {
								// 如果参数类型为body,则为数据类型为json字符串,一般只有一行
							}
						} else {
//							System.out.println("----- " + e);
						}
					}
				}
				
				System.out.println(requestInfo.toString());
				
			}
			
		}
		return requestInfos;
	}

	static class RequestInfo {
		
		public static final String METHOD_GET = "get";
		public static final String METHOD_PUT = "put";
		public static final String METHOD_POST = "post";
		public static final String METHOD_DELETE = "delete";
		
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

			@Override
			public String toString() {
				return "RequestParam [name=" + name + ", type=" + type
						+ ", des=" + des + "]";
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

		@Override
		public String toString() {
			return "RequestInfo [name=" + name + ", method=" + method
					+ ", url=" + url + ", des=" + des + ", params=" + params
					+ "]";
		}
		

	}
	
}
