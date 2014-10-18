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
	 * @param proPath		项目绝对路径
	 * @param layoutXml		布局文件名称,如main.xml
	 * @param activityFile	Activity类文件名,如MainActivity.java
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
		FileUtils.writeString2File(fileContent, javaFile);
	}
	
//	/**
//	 * 根据布局文件中的id名称生成对应的变量名
//	 * <p>
//	 * 只转换自动生成的id名称,布局名_控件名_xx,会将其转换成驼峰形式的"控件名Xx"<br>
//	 * 如id=main_textview_name -> textviewName
//	 * @param layoutName	id名所在的布局文件名
//	 * @param idName		id名称
//	 * @return
//	 */
//	private static String createFieldName(String layoutName, String idName) {
//		String fieldname = null;
//		//
//		return fieldname;
//	}
	
	
	/**
	 * 将参数值抽取到values文件夹下
	 * <p>如textColor=#ff00aa,将#ff00aa这样的具体值替换为@color/colorname
	 * <br>并在color.xml文件内创建一个对应颜色item
	 * 
	 * @param proPath			项目绝对路径
	 * @param valuesXml			values文件名称,如strings.xml dimens.xml等
	 * @param type				抽取内容的前缀,如@color
	 * @param itemName			values文件内item的名称
	 * @param itemAttrName		values文件内item的参数,一般都是name
	 * @param itemAttrValue		values文件内item的参数值,也是抽取值后替换的名称
	 * @param itemValue			values文件内item的值
	 */
	public static void extract2values(String proPath, String valuesXml, String type,
			String itemName, String itemAttrName, String itemAttrValue, String itemValue) {
		try {
			FileUtils.getAllFiles(new File(proPath));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String valuesPath = proPath + "/res/values/" + valuesXml;
		File valuesFile = new File(valuesPath);
		if(!valuesFile.exists()) {
			System.out.println("文件不存在,请确定文件["+valuesXml+"]位于/res/values/文件夹下,且文件名称正确");
			return;
		}
		
		int extractFileCount = 0;
		for(File file : FileUtils.xmlAllFiles) {
			if(file.getName().equals(valuesXml)) {
				continue;
			}
			
			String fileContent = FileUtils.readToString(file);
			if(!fileContent.contains(itemValue)) {
				continue;
			}
			
			fileContent = fileContent.replace(itemValue, "@"+type+"/"+itemAttrValue);
			FileUtils.writeString2File(fileContent, file);
			
			Document docValues = XmlUtil.read(valuesFile);
			Element rootElement = docValues.getRootElement();
			List<Element> elements = rootElement.elements();
			
			boolean hasColor = false;
			for(Element element : elements) {
				String color = element.attributeValue(itemAttrName);
				if(color.equals(itemAttrValue)) {
					hasColor = true;
					break;
				}
			}
			
			if(!hasColor) {
				Element element = rootElement.addElement(itemName);
				element.addAttribute(itemAttrName, itemAttrValue);
				element.setText(itemValue);
				
				XmlUtil.write2xml(valuesFile, docValues);
			}
			
			extractFileCount ++;
		}
		
		System.out.println("将[" + itemValue 
				+ "]抽取为[" + valuesXml 
				+ "]文件下的[" + itemAttrValue + "]");
		System.out.println("共抽取了" + extractFileCount + "个文件下的内容");
		System.out.println("-------------------------");
	}
	
	public static void extractColor(String proPath, String colorXml, String colorName, String RGB) {
		extract2values(proPath, colorXml, "color", "color", "name", colorName, RGB);
	}
}
