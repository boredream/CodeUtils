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

public class Hnbang {

	private static String hostUrl = "http://hnbang.com";
	private static String imagePageUrl = hostUrl + "/page/%s";

	public static void main(String[] args) throws Exception {
		savePages();
	}

	private static void savePages() throws Exception {
		File path = new File("temp" + File.separator + "reptile"
				+ File.separator + "gif" + File.separator + "hnbang");

		List<GifInfo> infos = new ArrayList<>();
		int perCount = 100;
		for (int i = 501; i <= 780; i++) {
			File file = new File(path, "page" + i + ".txt");
			if (file.exists()) {
				continue;
			}

			String url = String.format(imagePageUrl, i);
			List<GifInfo> gifs = getAllGif(url);
			infos.addAll(gifs);

			if (i % perCount == 0) {
				OfficeUtils.saveCVS(infos, file);
				infos.clear();
			}
		}
		
		File file = new File(path, "page_last.txt");
		OfficeUtils.saveCVS(infos, file);
	}

	private static List<GifInfo> getAllGif(String appUrl) throws Exception {
		System.out.println("get gif ... url = " + appUrl);
		List<GifInfo> infos = new ArrayList<>();

		String response = HttpUtils.getString(appUrl);
		Document parse = Jsoup.parse(response);

		Elements contentElements = parse.getElementsByClass("content").get(0)
				.getElementsByTag("article");
		for (Element element : contentElements) {
			String title = element.getElementsByClass("note").get(0).text();
			title = title.replace("猛击图片查看大图！", "").trim();

			Elements oriGifUrlElements = element.getElementsByTag("img");
			if(oriGifUrlElements.size() == 0) {
				continue;
			}
			String oriGifUrl = oriGifUrlElements.get(0).attr("data-original");

			String thumbsUp = element
					.getElementsByAttributeValueEnding("class", "post-like")
					.get(0).text().replace("赞 (", "").replace(")", "")
					.replace(" ", "");
			int favCount = Integer.parseInt(thumbsUp);

			String tag = element.getElementsByClass("post-tags").text()
					.replace("标签：", "").replace("gif", "").replace(" ", "");

			GifInfo info = new GifInfo();
			info.tag = tag;
			info.title = title;
			info.imgUrl = oriGifUrl;
			info.favCount = favCount;

			infos.add(info);
		}

		return infos;
	}

	public static class GifInfo {
		/**
		 * 标题
		 */
		public String title;
		/**
		 * 标签
		 */
		public String tag;
		/**
		 * 搜索图地址
		 */
		public String thumbnailImgUrl;
		/**
		 * 动态图地址
		 */
		public String imgUrl;
		/**
		 * 收藏数量
		 */
		public int favCount;
		/**
		 * 评论数量
		 */
		public int commentCount;
	}
}
