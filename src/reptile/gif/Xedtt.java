package reptile.gif;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.HttpUtils;
import utils.OfficeUtils;

public class Xedtt {

	private static String hostUrl = "http://www.xedtt.com";
	private static String imagePageUrl = hostUrl
			+ "/gaoxiaodongtaitu/index_%s.html";

	public static void main(String[] args) throws Exception {
		saveAll();
	}

	private static void saveAll() throws Exception {
		File path = new File("temp" + File.separator + "reptile"
				+ File.separator + "gif" + File.separator + "xedtt");
		for (int i = 5; i < 10; i++) {
			File file = new File(path, "page" + i + ".txt");
			if (file.exists()) {
				continue;
			}

			String url = String.format(imagePageUrl, i);
			List<GifInfo> infos = getAllGif(url);

			OfficeUtils.saveCVS(infos, file);
		}
	}

	private static List<GifInfo> getAllGif(String appUrl) throws Exception {
		System.out.println("get gif ... url = " + appUrl);
		List<GifInfo> infos = new ArrayList<>();

		String response = HttpUtils.getString(appUrl);
		Document parse = Jsoup.parse(response);

		Elements contentElements = parse.getElementsByClass("miao-con").get(0)
				.getElementsByTag("li");
		for (Element element : contentElements) {
			String title = element.text();

			String thumbnailImgUrl = element.getElementsByTag("img").get(0)
					.attr("src");

			String oriGifUrl = element.getElementsByTag("a").get(0)
					.attr("href");
			oriGifUrl = hostUrl + oriGifUrl;

			GifInfo info = new GifInfo();
			info.title = title;
			info.thumbnailImgUrl = thumbnailImgUrl;

			String oriGifContent = HttpUtils.getString(oriGifUrl);
			Element oriGifElement = Jsoup.parse(oriGifContent)
					.getElementsByClass("content").get(0);
			Element imageElement = oriGifElement.getElementsByTag("img").get(0);
			String imgUrl = imageElement.attr("src");
			int width = 0;
			int height = 0;
			try {
				width = Integer.parseInt(imageElement.attr("width"));
				height = Integer.parseInt(imageElement.attr("height"));
			} catch (Exception e) {
			}
			info.imgUrl = imgUrl;
			info.width = width;
			info.height = height;

			infos.add(info);
		}

		return infos;
	}

	public static List<String> getAllAppLinks() throws Exception {
		List<String> links = new ArrayList<>();

		String response = HttpUtils.getString(imagePageUrl);
		Document parse = Jsoup.parse(response);

		Element appsElement = parse.getElementById("apps-dropdown");
		Elements appsItemElements = appsElement.getElementsByTag("a");
		for (Element element : appsItemElements) {
			String attr = element.attr("href");
			links.add(attr);
		}

		return links;
	}

	public static class GifInfo {
		public String title;
		public String thumbnailImgUrl;
		public String imgUrl;
		public int width;
		public int height;
	}
}
