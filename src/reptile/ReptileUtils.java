package reptile;

import utils.HttpUtils;

public class ReptileUtils {
	private static String url = "http://docs.stone-chat.com:8080/stonechat_short_server_v2_doc/#api-Search-PostSearchCompany";

	public static void main(String[] args) throws Exception {
		getWebHtml();
	}
	
	public static void getWebHtml() throws Exception {
		String response = HttpUtils.getString(url);
		System.out.println(response);
	}
}
