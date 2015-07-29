package test;

import java.io.File;
import java.util.List;

import utils.AndroidUtils;
import utils.FileUtils;

public class Main {

	private static String proPath = "D:\\adt-bundle-windows-x86_64-20140702\\sdk\\sources\\android-20";
	private static String activityPath = "E:\\ConvenientMobile\\src\\com\\sfbm\\convenientmobile\\";
	
	private static String weiboSrc = "E:\\GitHub\\GeekSchool\\MyWeibo\\Demo4Eclipse\\src\\com\\boredream\\boreweibo\\";
	private static String weiboLayout = "E:\\GitHub\\GeekSchool\\MyWeibo\\Demo4Eclipse\\res\\layout\\";
	
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
		
		
//		AndroidUtils.autoFindViewById(weiboLayout + "include_avatar.xml", 
//				weiboSrc + "fragment\\User2Fragment.java");
//		AndroidUtils.autoFindViewById(weiboLayout + "activity_write_status.xml",
//				weiboSrc + "activity\\WriteStatusActivity.java");
//		AndroidUtils.autoCreateAdapter(weiboLayout + "item_user.xml", 
//				weiboSrc + "adapter\\UserItemAdapter.java");
		
//		JsonUtils.parseJson2Java();
		
//		FileUtils.getCodeLines("E:\\workspace\\GifView");
		
//		AndroidUtils.extract2Dimen("", "", "", "12sp", "");
//		AndroidUtils.extract2Dimen("", "", "", "12.5dp", "");
//		AndroidUtils.extract2Dimen("", "", "", "0.22dip", "");
//		AndroidUtils.extract2Dimen("", "", "", "999.111px", "");
		
		// 自动处理图片sel
//		String rootPath = "C:\\Users\\root\\Documents\\QQEIM Files\\2851657065\\FileRecv\\images_7.14";
//		String normalPath = rootPath + "\\normal";
//		String specialPath = rootPath + "\\pressdown";
//		AndroidUtils.batchRenameSelFiles(normalPath, specialPath, "ic", "checked");
//		AndroidUtils.batchCreateSelFiles(rootPath + "\\sel", "checked");
		
//		AndroidUtils.createShapeSel("blue", "blue_dark", "4dp", "pressed");
		
		String boreIM = "E:\\GitHub\\BoreIM\\";
//		AndroidUtils.autoFindViewById(
//				boreIM + "res\\layout\\activity_main.xml", 
//				boreIM + "src\\com\\boredream\\im\\activity\\TestActivity.java",
//				true);
//		AndroidUtils.autoCreateAdapter(
//				boreIM + "res\\layout\\item_conversation.xml", 
//				boreIM + "src\\com\\boredream\\im\\adapter\\MessageRecentAdapter.java",
//				true);
		
		String cm = "E:\\ConvenientMobile\\";
		AndroidUtils.autoFindViewById(
				cm + "res\\layout\\activity_psw_setting.xml", 
				cm + "src\\com\\sfbm\\convenientmobile\\activity\\PaySettingActvity.java",
				true);
//		AndroidUtils.autoCreateAdapter(
//				cm + "res\\layout\\item_conversation.xml", 
//				cm + "src\\com\\boredream\\im\\adapter\\MessageRecentAdapter.java",
//				true);
	}
	
}

