import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;


public class AndroidLayoutUtils {

	/**
	 * 设置布局文件的id
	 * <p>
	 * 将设置全部View的id,id名字格式为布局文件名_控件名_index
	 * 
	 * @param proPath		项目绝对路径
	 * @param layoutXml		布局文件名称,如main.xml
	 */
	public static void setLayoutId(String proPath, String layoutXml) {
		String layoutName = layoutXml.substring(0, layoutXml.indexOf("."));
		
		File file = FileUtils.getXmlFileByName(proPath, layoutXml);
		Document doc = XmlUtil.read(file);
		List<Element> allElement = XmlUtil.getAllElements(doc);
		
		int index = 0;
		for (Element element : allElement) {
			if(!XmlUtil.hasAttribute(element, "id")) {
				String idValue = "@+id/" + layoutName 
						+ "_" + element.getName().toLowerCase(Locale.CHINESE) 
						+ "_" + (index++);
				element.addAttribute("android:id", idValue);
			}
		}

		XmlUtil.write2xml(file, doc);
	}
	
	/**
	 * 自动遍历xml中所有带id的控件,在activity文件中设置对应变量,变量名为id名
	 * @param proPath
	 * @param layoutXml
	 * @param activityFile
	 */
	public static void autoFindViewById(String proPath, String layoutXml, String activityFile) {
		File javaFile = FileUtils.getJavaFileByName(proPath, activityFile);
		List<Element> allElement = XmlUtil.getAllElements(proPath, layoutXml);
		
		List<IdNamingBean> idNamingBeans = new ArrayList<IdNamingBean>();
		for (Element element : allElement) {
			Attribute attribute = element.attribute("id");
			if(attribute != null) {
				String value = attribute.getValue();
				String idName = value.substring(value.indexOf("/") + 1);
				IdNamingBean bean = new IdNamingBean(element.getName(), idName);
				if(!idNamingBeans.contains(bean)) {
					idNamingBeans.add(bean);
				}
			}
		}
		
		String fileContent = FileUtils.readToString(javaFile);
		
		StringBuilder sb = new StringBuilder();
		for(IdNamingBean bean : idNamingBeans) {
			sb.append("private ")
				.append(bean.viewName)
				.append(" ")
				.append(bean.idName)
				.append(";")
				.append("\n");
		}
		
		sb.append("private void initView(){");
		
		for(IdNamingBean bean : idNamingBeans) {
			sb.append(bean.idName)
				.append(" = ")
				.append("(" + bean.viewName + ")")
				.append("findViewById(R.id." + bean.idName + ")")
				.append(";\n");
		}
		
		sb.append("}");
		
		fileContent = fileContent.replaceFirst("\\{", "\\{" + sb.toString());
		FileUtils.whiteString2File(fileContent, javaFile);
	}

}
