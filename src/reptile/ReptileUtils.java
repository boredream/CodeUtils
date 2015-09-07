package reptile;

import utils.HttpUtils;

public class ReptileUtils {
	private static String url = "http://www.zhihu.com/topics";

	public static void main(String[] args) throws Exception {
		getWebHtml();
	}
	
	public static void getWebHtml() throws Exception {
		String response = HttpUtils.getString(url);
		System.out.println(response);
	}
}
