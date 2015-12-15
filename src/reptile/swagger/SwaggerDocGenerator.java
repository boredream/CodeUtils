package reptile.swagger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.AndroidUtils;
import utils.FileUtils;
import utils.JsonUtils;
import utils.StringUtils;
import entity.Json2JavaElement;
import entity.RequestInfo;
import entity.RequestInfo.RequestParam;

public class SwaggerDocGenerator {

	public static void main(String[] args) {
		ArrayList<RequestInfo> infos = parseDocFromHtml("temp" + File.separator + "apidoc" + File.separator + "swagger.txt");
		System.out.println("接口总数量：" + infos.size());
		System.out.println(infos);
		genCode(infos);
	}

	public static void genCode(ArrayList<RequestInfo> infos) {
		StringBuilder sbUrl = new StringBuilder();
		StringBuilder sb = new StringBuilder();
		for(RequestInfo info : infos) {
			
//			/**
//			 * 查询指定服务申请详细信息
//			 */
//			public static final String SERVICEAPPLIES = "/rest/serviceApplies/{applyId}";
			
			String name = info.getName();
			String url = info.getUrl();
			String urlName = /*info.getMethod() + "_" + */url
					.replace("/rest/", "")
					.replace("/", "_")
					.replace("{", "")
					.replace("}", "");
			
			String responseBeanName = "Object";
			if(info.getResponseJson() != null) {
				// user/test -> UserTestResponse
				String[] split = url.split("/");
				responseBeanName = StringUtils.firstToUpperCase(split[0]) + StringUtils.firstToUpperCase(split[1]) + "Response";
				
				String jsonJava = info.getResponseJson();
				// TODO 特殊处理,继承基类 extends BaseEntity
				jsonJava = jsonJava.replace("JsonBeans", responseBeanName + " extends BaseEntity");
				
				File file = new File("temp" + File.separator + "entity" + File.separator + responseBeanName + ".java");
				FileUtils.writeString2File(jsonJava, file, "utf-8");
			}
			
			// 作为url末尾直接拼装的参数,一般只有一个
			String endParam = null;
			String endParamName = null;
			Pattern pattern = Pattern.compile("\\{[\\s\\S]+\\}");
			Matcher matcher = pattern.matcher(url);
			if(matcher.find()) {
				endParam = matcher.group();
				endParamName = endParam.replace("{", "").replace("}", "");
				url = url.replace(endParam, "");
			}
			
			sbUrl.append(AndroidUtils.formatSingleLine(1, "/**"));
			sbUrl.append(AndroidUtils.formatSingleLine(1, " * " + name));
			sbUrl.append(AndroidUtils.formatSingleLine(1, " */"));
			sbUrl.append(AndroidUtils.formatSingleLine(1, "public static final String " 
					+ urlName.toUpperCase() + " = \"" + url + "\";" 
					+ (endParam == null ? "" : "// " + endParam)));
			sbUrl.append("\n");
			
//			/**
//			 * 注册用户
//			 *
//			 * @param phone 手机号 (可选项: 1; 2)
//			 * @param listener
//			 */
//			public static void userSignUp(String phone, HttpListener<User> listener) {
//			    String time = String.valueOf(getCurrentTime());
//			    Map<String, Object> params = new HashMap<String, Object>();
//			    params.put("phone", phone);
//			    params.put("time", time);
//			    doHttp(Urls.getUrl(Urls.USER_SIGN_UP), "get", params, User.class, listener);
//			}
			
			// endParam模式的为
//			    String time = String.valueOf(getCurrentTime());
//			    doHttp(Urls.getUrl(Urls.USER_SIGN_UP) + phone, params, User.class, listener);
			
			// 方式注释里参数
			StringBuilder sbAnotation = new StringBuilder();
			// 方法参数里参数
			StringBuilder sbParam = new StringBuilder();
			// 方法内容里参数
			StringBuilder sbBody = new StringBuilder();
			
			ArrayList<RequestParam> params = info.getParams();
			if(params != null && params.size() > 0) {
				sbAnotation.append(AndroidUtils.formatSingleLine(1, " *"));
				for(RequestParam param : params) {
					// 方式注释里参数
					sbAnotation.append("\t * @param")
						.append(" ").append(param.getName())
						.append(" ").append(param.getDes());
	
					ArrayList<String> options = param.getSelectOptions();
					if(options != null && options.size() > 0) {
						sbAnotation.append(" (可选项: ");
						for(int i=0; i<options.size(); i++) {
							if(i > 0) {
								sbAnotation.append("; ");
							}
							sbAnotation.append(options.get(i));
						}
						sbAnotation.append(")");
					}
					sbAnotation.append("\n");
					
					// 方法参数里参数 String phone, HttpListener<User> listener
					sbParam.append(param.getType() + " " + param.getName() + ", ");
					
					// 方法内容里参数 params.put("phone", phone);
					sbBody.append(AndroidUtils.formatSingleLine(2, 
							"params.put(\"" + param.getName() + "\", " + param.getName() + ");"));
				}
			}
			sbParam.append("HttpListener<" + responseBeanName + "> listener");
			
			sb.append(AndroidUtils.formatSingleLine(1, "/**"));
			sb.append(AndroidUtils.formatSingleLine(1, " * " + name));
			if(endParamName != null) {
				sb.append(AndroidUtils.formatSingleLine(1, " *"))
					.append("\t * @param").append(" ").append(endParamName);
			} else {
				sb.append(sbAnotation.toString());
			}
			sb.append(AndroidUtils.formatSingleLine(1, " */"));
			
			StringBuilder methodNameSb = new StringBuilder();
			String[] nameItems = urlName.split("_");
			for(int i=0; i<nameItems.length; i++) {
				String nameItem = nameItems[i];
				if(i>0) {
					nameItem = nameItem.substring(0, 1).toUpperCase() + nameItem.substring(1);
				}
				methodNameSb.append(nameItem);
			}
			
			sb.append(AndroidUtils.formatSingleLine(1, "public static void " + methodNameSb.toString() +
					"("  + (endParamName==null?"":"String "+endParamName+", ") + sbParam.toString() + ") {"));
			sb.append(AndroidUtils.formatSingleLine(2, 
					"HashMap<String, Object> params = new HashMap<String, Object>();"));
			sb.append(sbBody.toString());
			sb.append(AndroidUtils.formatSingleLine(2, 
					"doHttp(URLs.getUrl(URLs." + urlName.toUpperCase() 
					+ ")" + (endParamName==null?"":" + "+endParamName) + ", \"" + info.getMethod() + "\", params, " + responseBeanName + ".class, listener);"));
			sb.append(AndroidUtils.formatSingleLine(1, "}")); 
			sb.append("\n");
		}
		System.out.println(sbUrl.toString());
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
			
			// 获取该类型下具体接口
			// <ul class="operations">
			Elements apiElements = typeElement.getElementsByClass("operations");
//			System.out.println(apiTitle + " ... " + apiElements.size());
			
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
					Elements paramElements = paramsElement.children();
					
					ArrayList<RequestParam> params = new ArrayList<RequestParam>();
					for(Element paramElement : paramElements) {
						// 一列对应参数的一个属性,共5列,分别为 名称,值,描述,参数类型,数据类型
						// <td>
						Elements columeElement = paramElement.children();
						
						String paramName = columeElement.get(0).text();
						String paramDes = columeElement.get(2).text();
						String pType = columeElement.get(3).text();
						
						if(pType.equals("query")) {
							// 如果参数类型为query,则为数据类型为基础类型
							String paramType = columeElement.get(4).text();
							
							// 数据值可能为可选项
							Elements valueOptionsElements = columeElement.get(1).getElementsByTag("option");
							ArrayList<String> valueOptions = new ArrayList<String>();
							for(Element valueOptionsElement : valueOptionsElements) {
								String option = valueOptionsElement.text();
								if(option != null && !option.trim().equals("")) {
									valueOptions.add(option);
								}
							}
							
							params.add(new RequestParam(paramName, paramType, paramDes, valueOptions));
							requestInfo.setParams(params);
						} else if(pType.equals("body")) {
							// 如果参数类型为body,则为数据类型为json字符串,且一般只有一行
							Element jsonElement = columeElement.get(4);
							// 解析json中的多个参数
							requestInfo.setParams(parseJsonParams(jsonElement));
							break;
						}
					}
				}
				requestInfos.add(requestInfo);
//				System.out.println(requestInfo.toString());
			}
			
		}
		return requestInfos;
	}

	private static ArrayList<RequestParam> parseJsonParams(Element jsonElement) {
		ArrayList<RequestParam> requestParams = new ArrayList<RequestParam>();
		
		String jsonStr = jsonElement.getElementsByClass("json").text();
		if(!jsonStr.isEmpty()) {
			List<Json2JavaElement> jsonBeanTree = JsonUtils.getJsonBeanTree(jsonStr);
			for(Json2JavaElement j2je : jsonBeanTree) {
				String paramName = j2je.getName();
				String paramDes = ""; // 此类格式post参数没有描述
				String pType = j2je.getType().getSimpleName();
				requestParams.add(new RequestParam(paramName, pType, paramDes, new ArrayList<String>()));
			}
		}
		return requestParams;
	}
	
}
