package test;


import java.io.File;
import java.util.List;

import utils.AndroidUtils;
import utils.FileUtils;



public class Main {

//	private static String proPath = "E:\\workspace\\ConvenientPos";
	private static String proPath = "D:\\adt-bundle-windows-x86_64-20140702\\sdk\\sources\\android-20";
	private static String activityPath = "E:\\workspace\\PosTest\\src\\com\\example\\postest\\TestActivity.java";
	private static String layoutPath = "E:\\workspace\\PosTest\\res\\layout\\test.xml";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
//		List<File> allFiles = FileUtils.getAllFiles(proPath);
//		for(File file : allFiles) {
//			if(file.getName().endsWith(".xml")) {
//				Document document = XmlUtil.read(file);
//				boolean replace = XmlUtil.replacePartAttrValue(document, ":", "：", "textLeft");
//				if(replace) {
//					XmlUtil.write2xml(file, document);
//				}
//			}
//		}
		
//		AndroidUtils.delNoUseSrcFile(proPath);
//		
//		List<File> allFiles = FileUtils.getAllFiles(proPath);
//		for(File file : allFiles) {
//			String string = FileUtils.readToString(file);
//			if(string.contains("drawText")) {
//				System.out.println(file.getAbsoluteFile());
//			}
//		}
		
//		AndroidUtils.autoFindViewById("E:\\ConvenientPos\\res\\layout\\activity_write_bankcard.xml", 
//				"E:\\ConvenientPos\\src\\com\\convenient\\pos\\purse\\WriteBankCardInfoActivity.java");
		AndroidUtils.autoCreateAdapter("E:\\ConvenientPos\\res\\layout\\item_bg.xml", 
				"E:\\ConvenientPos\\src\\com\\convenient\\pos\\homepage\\BgAdapter.java");
		
//		JsonUtils.parseJson2Java();
		
//		String string = FileUtils.readToString(new File("Json\\JsonString.txt"), "UTF-8");
//		JsonBean fromJson = new Gson().fromJson(string, JsonBean.class);
//		System.out.println(fromJson.getCode());
		
//		AndroidUtils.extract2Dimen("", "", "", "12sp", "");
//		AndroidUtils.extract2Dimen("", "", "", "12.5dp", "");
//		AndroidUtils.extract2Dimen("", "", "", "0.22dip", "");
//		AndroidUtils.extract2Dimen("", "", "", "999.111px", "");
	}
	
}

