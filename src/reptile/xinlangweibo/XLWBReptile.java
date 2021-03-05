package reptile.xinlangweibo;

import java.util.HashMap;
import java.util.Map;

import utils.HttpUtils;

public class XLWBReptile {

	static String host = "http://weibo.cn/";
	static String gsid_CTandWM = "4u0aea251L1rNf9U3kMfho6MTf3";
	
	public static void main(String[] args) throws Exception {
		String userId = "313555377";
		String url = host + userId;
		Map<String, String> headers = new HashMap<String, String>();
		
		headers.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.101 Safari/537.36");
		headers.put("Cookie", "gsid_CTandWM=" + gsid_CTandWM);
		String html = HttpUtils.getString(url, headers);
		
		System.out.println(html);
	}
	
}
