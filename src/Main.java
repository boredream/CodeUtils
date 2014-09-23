
public class Main {

	static String fileCharCode = "UTF-8";
	private static String proPath = "E:\\workspace\\Manual4Body";
	private static String layout = "auto_test.xml";
	private static String activity = "AutoMainActivity.java";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AndroidLayoutUtils.setLayoutId(proPath, layout);
		AndroidLayoutUtils.autoFindViewById(proPath, layout, activity);
	}

}
