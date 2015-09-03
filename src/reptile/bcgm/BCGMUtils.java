package reptile.bcgm;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.FileUtils;
import utils.HttpUtils;

public class BCGMUtils {
	public static String DOMAIN_URL = "http://www.a-hospital.com/w/";
	public static String MAIN_URL = DOMAIN_URL
			+ "%E6%9C%AC%E8%8D%89%E7%BA%B2%E7%9B%AE#.E8.8D.89.E9.83.A8";
	public static String IMG_URL = DOMAIN_URL
			+ "%E4%B8%AD%E8%8D%AF%E5%9B%BE%E5%85%B8";

	public static List<CaoMain> getMainUrl() {
		// String response = HttpUtils.getString(mainUrl);
		String response = FileUtils.readToString(
				new File("temp" + File.separator + "reptile" + File.separator
						+ "bcgm_main.txt"), "UTF-8");

		String type = null;
		List<CaoMain> caos = new ArrayList<CaoMain>();

		Document parse = Jsoup.parse(response);
		Elements allElements = parse.getAllElements();
		for (int i = 0; i < allElements.size(); i++) {
			Element element = allElements.get(i);

			// <span class="mw-headline"
			// id=".E8.8D.89.E9.83.A8">草部</span>
			String attrClass = element.attr("class");
			if ("mw-headline".equals(attrClass + "")) {
				type = element.text();
				continue;
			}

			// <a
			// href="/w/%E6%9C%AC%E8%8D%89%E7%BA%B2%E7%9B%AE/%E7%94%98%E8%8D%89"
			// title="本草纲目/甘草">甘草</a>
			String title = element.attr("title");
			String href = element.attr("href");

			if (type != null && href != null && title != null
					&& title.startsWith("本草纲目/")) {
				CaoMain cao = new CaoMain();
				cao.setType(type);
				cao.setName(element.text());
				cao.setHref(DOMAIN_URL + href);

				caos.add(cao);
			}
		}

		return caos;
	}

	public static List<CaoImg> getImgData(String url) throws Exception {
		String response = HttpUtils.getString(url);

		Document parse = Jsoup.parse(response);
		Elements allElements = parse.getAllElements();

		List<CaoImg> caoImgs = new ArrayList<CaoImg>();

		for (int i = 0; i < allElements.size(); i++) {
			Element element = allElements.get(i);

			// <table class="wikitable"
			// style="width: 22em; position: absolute; top: 0px; left: 0px;">
			String nodeName = element.nodeName();
			String attrClass = element.attr("class");
			if (nodeName.equals("table") && "wikitable".equals(attrClass + "")) {
				String title = element.getElementsByAttribute("title").get(0)
						.attr("title");
				Elements imgElement = element.getElementsByTag("img");
				String src = imgElement.attr("src");

				Elements styleElements = element
						.getElementsByAttributeValueContaining("style",
								"font-size");
				String otherName = null;
				String intro = null;
				if (styleElements.size() == 1) {
					intro = styleElements.get(0).text();
				} else {
					otherName = styleElements.get(0).text();
					intro = styleElements.get(1).text();
				}

				CaoImg caoImg = new CaoImg();
				caoImg.setName(title);
				caoImg.setImg(src);
				caoImg.setOtherName(otherName);
				caoImg.setIntro(intro);

				caoImgs.add(caoImg);
			}
		}

		return caoImgs;
	}

	public static String getDetailData(String caoName) throws Exception {
		String detail = null;
		String response = null;
		response = HttpUtils.getString(DOMAIN_URL
				+ URLEncoder.encode(caoName, "UTF-8"));

		// String response = FileUtils.readToString(
		// new File("temp" + File.separator + "reptile" + File.separator
		// + "bcgm_detail.txt"), "UTF-8");

		Document parse = Jsoup.parse(response);

		StringBuilder sb = new StringBuilder();
		Elements pes = parse.getElementsByTag("p");
		for (Element e : pes) {
			String text = e.text();
			if (text.startsWith("--")) {
				break;
			}
			sb.append(e.text() + "<br/>");
		}

		detail = sb.toString().trim();
		return detail;
	}

}
