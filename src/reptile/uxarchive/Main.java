package reptile.uxarchive;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.FileUtils;
import utils.HttpUtils;
import utils.HttpUtils.Method;
import utils.OfficeUtils;
import utils.StringUtils;

public class Main {

	private static String hostUrl = "http://uxarchive.com";
//	private static boolean start;

	public static void main(String[] args) throws Exception {
		File dir = new File("temp" + File.separator + "reptile" + File.separator + "uxarchive");
		boolean start = false;
		for(File file : dir.listFiles()) {
			if(file.isDirectory()) {
				continue;
			}
			
			String name = FileUtils.getName(file);
			if(name.equals("rdio")) {
				start = true;
			}
			
			if(!start) {
				continue;
			}
			
			File appDir = new File(dir, name);				
			
			if(!appDir.exists()) {
				appDir.mkdir();
			}
			List<UxImageInfo> infos = OfficeUtils.readDatasFromCSV(file, UxImageInfo.class);
			for(UxImageInfo info : infos) {
				String imgName = StringUtils.gapToCamel(info.flowName) + "_" + 
						StringUtils.gapToCamel(info.name) + "_" + 
						info.seqInFlow + ".png";
				File imgFile = new File(appDir, imgName);
				if(imgFile.exists()) {
					continue;
				}
				byte[] bytes = HttpUtils.getOrPostFile(Method.GET, info.imgUrl, null, null);
				FileUtils.writeBytes2File(bytes, imgFile);
				System.out.println("image ... " + name + " : " + imgName);
			}
		}
		
//		File file = dir.listFiles()[1];
	}

	private static void saveAll() throws Exception {
		List<String> links = getAllAppLinks();
		for(int i=0; i<links.size(); i++) {
			String appUrl = hostUrl + links.get(i);

			String appName = appUrl.substring(appUrl.lastIndexOf("/") + 1);
			System.out.println(appName);
			
			List<UxImageInfo> allAppUx = getAllAppUx(appUrl);
			
			File file = new File("temp" + File.separator + "reptile" + File.separator +
					"uxarchive" + File.separator + appName + ".txt");
			OfficeUtils.saveCVS(allAppUx, file);
		}
	}

	private static List<UxImageInfo> getAllAppUx(String appUrl) throws Exception  {
		List<UxImageInfo> infos = new ArrayList<>();
		
		String response = HttpUtils.getString(appUrl);
		Document parse = Jsoup.parse(response);
		
		Elements flowElements = parse.getElementsByClass("flow");
		for(Element element : flowElements) {
			String flowName = element.getElementsByClass("app-taskname").get(0).text();
			Element imgElement0 = element.getElementsByAttributeValueStarting("class", "step-link").get(0);
			String flowUrl = hostUrl + imgElement0.attr("href");
			
			String responseFlow = HttpUtils.getString(flowUrl);
			Document parseFlow = Jsoup.parse(responseFlow);
			
			int seq = 1;
			for(Element e : parseFlow.getElementsByClass("screen-box")) {
				UxImageInfo info = new UxImageInfo();
				info.seqInFlow = String.valueOf(seq ++);
				info.flowName = flowName;
				
				Element imgElement = e.getElementsByTag("img").get(0);
				String imgUrl = "http:" + imgElement.attr("src");
				info.imgUrl = imgUrl;
				
				String name = imgElement.attr("alt");
				info.name = name;
				
				infos.add(info);
			}
		}
		
		return infos;
	}

	public static List<String> getAllAppLinks() throws Exception {
		List<String> links = new ArrayList<>();
		
		String response = HttpUtils.getString(hostUrl);
		Document parse = Jsoup.parse(response);
		
		Element appsElement = parse.getElementById("apps-dropdown");
		Elements appsItemElements = appsElement.getElementsByTag("a");
		for (Element element : appsItemElements) {
			String attr = element.attr("href");
			links.add(attr);
		}

		return links;
	}
	
	public static class UxImageInfo {
		public String imgUrl;
		public String name;
		public String flowName;
		public String seqInFlow;
	}
}
