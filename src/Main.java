

public class Main {

	private static String proPath = "E:\\ConvenientPos";
	private static String layout = "myhome_setting.xml";
	private static String activity = "MyHomeSettingActivity.java";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		AndroidLayoutUtils.setLayoutId(proPath, layout);
//		AndroidLayoutUtils.autoFindViewById(proPath, layout, activity);
		
//		AndroidLayoutUtils.extract2Color(proPath, "colors.xml", "mycolor1", "#ffffff");
		
		AndroidLayoutUtils.extract2String(proPath, "strings.xml", "testtttt", "为什么为什么为什么");
		
//		Map<String, String> maps = new HashMap<String, String>();
//		maps.put("bg_bluea", "blue");
//		FileUtils.replaceStringOfXml(proPath, "bg_bluea", maps);
		
//		try {
//			FileUtils.getAllFiles(new File(proPath));
//			for(File file : FileUtils.xmlAllFiles) {
//				if(file.getName().contains("item_info")) {
//					String string = FileUtils.readToString(file, "UTF-8");
//					System.out.println(string);
//					string = string.replace("为什么", "去死吧");
//					System.out.println(string);
//					FileUtils.writeString2File(string, file);
//				}
//				
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}

}
