package test;

import java.io.File;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.AndroidUtils;
import utils.FileUtils;

public class Main {

	private static String url = "http://www.a-hospital.com/w/%E6%9C%AC%E8%8D%89%E7%BA%B2%E7%9B%AE#.E8.8D.89.E9.83.A8";

	public static void main(String[] args) {

		AndroidUtils.autoCreateActivity();
		// AndroidUtils.autoCreateAdapter();
		// JsonUtils.parseJson2Java();

		// TempUtils.autoCreateSizeSet(true);

		new Thread() {
			public void run() {
				try {
					// String response = HttpUtils.getString(url);
					String response = FileUtils.readToString(new File("temp"
							+ File.separator + "bcgm_main.txt"), "UTF-8");

					Document parse = Jsoup.parse(response);

					Elements allElements = parse.getAllElements();
					for (int i = 0; i < allElements.size(); i++) {
						Element element = allElements.get(i);
						String nodeName = element.nodeName();
						String data = element.data();
						
						String attr = element.attr("title");
						String href = element.attr("href");
						
						if(attr.contains("甘草")) {
							System.out.println(element);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			};
		}.start();
	}
}
