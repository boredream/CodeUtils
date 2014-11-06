package test;

import utils.AndroidUtils;



public class Main {

	private static String proPath = "E:\\workspace\\ConvenientPos";

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
		
		AndroidUtils.delNoUseSrcFile(proPath);
	}

}
