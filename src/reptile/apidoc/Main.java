package reptile.apidoc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import entity.RequestParam;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import reptile.bmob.BmobHttpUtils;
import reptile.swagger.SwaggerDocGenerator;
import utils.JsonUtils;
import entity.Json2JavaElement;
import entity.RequestInfo;

public class Main {
	
	static String host = "http://192.168.0.117:9002";
	
	public static void main(String[] args) {
		String catename = "用户模块";
		 int cateid = 10;
		
//		String catename = "帖子模块";
//		int cateid = 15;
		
//		String catename = "礼物模块";
//		int cateid = 17;
		
//		String catename = "系统模块";
//		int cateid = 18;
		 
//		String catename = "发现模块";
//		int cateid = 20;
		
//		String catename = "H5页面地址";
//		int cateid = 21;
		
		String typeUrl = host + "/interface/interlist?cateid="+cateid+"&catename="+catename+"&projectName=玩趣商城";
		
		ArrayList<RequestInfo> infos = parseApi(typeUrl);
		SwaggerDocGenerator.genCode(infos);
	}

	private static ArrayList<RequestInfo> parseApi(String typeUrl) {
		ArrayList<RequestInfo> requestInfos = new ArrayList<RequestInfo>();
		try {
			String response = BmobHttpUtils.getString(typeUrl);
			
			Document parse = Jsoup.parse(response);
			Elements refentryElements = parse.getElementsByClass("refentry");
			for(Element element : refentryElements) {
				Elements hrefElement = element.getElementsByAttribute("href");
				if(hrefElement.size() > 0) {
					Element e = hrefElement.get(0);
					String link = e.attr("href");
					
					RequestInfo info = getApiInfo(link);
					requestInfos.add(info);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return requestInfos;
	}

	private static RequestInfo getApiInfo(String link) throws Exception {
		RequestInfo requestInfo = new RequestInfo();
		String typeUrl = host + link;
		
		String response = BmobHttpUtils.getString(typeUrl);
		Document parse = Jsoup.parse(response);
		
		Elements refnamedivEs = parse.getElementsByClass("refname");
		String refnamediv = refnamedivEs.get(0).text().trim();
		requestInfo.setUrl(refnamediv);
		
		Elements titleEs = parse.getElementsByClass("dc-title");
		String title = titleEs.get(0).text().trim();
		requestInfo.setName(title);
		
		Elements desEs = parse.getElementsByAttributeValueContaining("class", "rdfs-comment");
		String description = desEs.get(0).text().trim();
		requestInfo.setDes(description);
		
		requestInfo.setMethod("post");
		
		Elements paramElements = parse.getElementsByAttributeValueContaining("class", "parameters");
		
		// request param
		ArrayList<RequestParam> params = new ArrayList<>();
		if(paramElements.size() > 0) {
			Elements name_type = paramElements.get(0).getElementsByTag("dt");
			Elements des = paramElements.get(0).getElementsByTag("dd");
			
			for(int i=0; i<name_type.size(); i++) {
				// cate - int
				Element element = name_type.get(i);
				String name = element.text().split("-")[0].trim();
				String type = element.text().split("-")[1].trim();
				type = getType(name, type);
				
				String desStr = des.get(i).text().trim();
				
				params.add(new RequestParam(name, type, desStr, new ArrayList<String>()));
			}
		}
		
		// return value param
		Elements returnvaluesEs = parse.getElementsByAttributeValueContaining("class", "returnvalues");
		if(returnvaluesEs.size() > 0) {
			Elements exmEs = returnvaluesEs.get(0).getElementsByClass("exm");
			String exm = exmEs.get(0).text().trim();
			
			List<Json2JavaElement> jsonBeanTree = JsonUtils.getJsonBeanTree(exm);
			ArrayList<NameTypeDes> nameTypeDes = new ArrayList<NameTypeDes>();
			
			Elements name_type = returnvaluesEs.get(0).getElementsByTag("dt");
			Elements des = returnvaluesEs.get(0).getElementsByTag("dd");
			
			for(int i=0; i<name_type.size(); i++) {
				// cate - int
				Element element = name_type.get(i);
				String name = element.text().split("-")[0].trim();
				String type = element.text().split("-")[1].trim();
				
				if(name.equals("list")) {
					// 集合数据
					Element listParamE = des.get(i);
					
					Elements list_name_type = listParamE.getElementsByTag("dt");
					Elements list_des = listParamE.getElementsByTag("dd");
					
					for(int j=0; j<list_name_type.size(); j++) {
						// cate - int
						Element listE = list_name_type.get(j);
						String listname = listE.text().split("-")[0].trim();
						String listtype = listE.text().split("-")[1].trim();
						
						String desStr = list_des.get(i).text().trim();
						nameTypeDes.add(new NameTypeDes(listname, listtype, desStr));
					}
					
					String desStr = des.get(i).text().trim();
					nameTypeDes.add(new NameTypeDes(name, null, desStr));
				} else {
					type = getType(name, type);
					String desStr = des.get(i).text().trim();
					
					nameTypeDes.add(new NameTypeDes(name, type, desStr));
				}
			}

			// 遍历jsonbean集合,加上注释和类型
			for(Json2JavaElement jje : jsonBeanTree) {
				Iterator<NameTypeDes> iterator = nameTypeDes.iterator();
				while(iterator.hasNext()) {
					NameTypeDes next = iterator.next();
					if(jje.getName().equals(next.getName())) {
						String type = next.getType();
						
						if(type != null && type.equals("int")) {
							jje.setType(int.class);
						} else if(type != null && type.equals("long")) {
							jje.setType(long.class);
						}
						
						jje.setDes(next.getDes());
						iterator.remove();
					}
				}
			}
			
			String bean = JsonUtils.createJavaBean(jsonBeanTree);
			requestInfo.setResponseJson(bean);
		}
		
		requestInfo.setParams(params);
		
		return requestInfo;
	}

	private static String getType(String name, String type) {
		if(name.contains("id") && type.equals("int")) {
			// id使用long类型数字
			type = "long";
		} else if(type.equals("string")) {
			type = "String";
		} else if(type.equals("date")) {
			// 日期使用字符串类型
			type = "String";
		} else if(name.equals("status")) {
			// 状态类型int
			type = "int";
		}
		return type;
	}
	
	static class NameTypeDes {
		private String name;
		private String type;
		private String des;
		
		public NameTypeDes(String name, String type, String des) {
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
			return "NameTypeDes [name=" + name + ", type=" + type + ", des="
					+ des + "]";
		}
		
	}

}
