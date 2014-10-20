package utils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class XmlUtil {

	static SAXReader saxReader = new SAXReader();

	/**
	 * 获得Xml 文档对象
	 * 
	 * @param xmlFile
	 *            指向xml 文件的引用
	 * @return xmlDoc 从文件读取xml Document
	 */
	public static Document read(File xmlFile) {
		Document document = null;
		try {
			document = saxReader.read(xmlFile);
		} catch (DocumentException e) {
			System.out.println("通过指向xml文件的文件获得Document对象时出错 !");
		}
		return document;
	}

	/**
	 * 通过xml 文件的名字读取Document对象
	 * 
	 * @param xmlFileName
	 * @return Document
	 */
	public static Document read(String xmlFileName) {
		return read(new File(xmlFileName));
	}

	/**
	 * 通过指向xml 文件的URL获得Document对象
	 * 
	 * @param url
	 * @return Document
	 */
	public static Document read(URL url) {
		Document document = null;
		try {
			document = saxReader.read(url);
		} catch (DocumentException e) {
			System.out.println("通过指向xml文件的URL获得Document对象时出错...");
		}
		return document;
	}
	
	static List<Element> elements = new ArrayList<Element>();
	public static void getChildElements(Element parentElement) {
		@SuppressWarnings("unchecked")
		List<Element> elementList = parentElement.elements();
		for(Element element : elementList) {
			elements.add(element);
			if(element.elements().size() > 0) {
				getChildElements(element);
			}
		}
	}
	
	/**
	 * 获取xml文件下全部element
	 * @param proPath
	 * @param xmlFileName
	 * @return
	 */
	public static List<Element> getAllElements(String proPath, String xmlFileName) {
		File file = FileUtils.getXmlFileByName(proPath, xmlFileName);
		Document doc = XmlUtil.read(file);
		Element rootElement = doc.getRootElement();
		
		getChildElements(rootElement);
		return elements;
	}
	
	/**
	 * 获取document下全部element
	 * @param Document
	 * @return
	 */
	public static List<Element> getAllElements(Document doc) {
		Element rootElement = doc.getRootElement();
		
		elements = new ArrayList<Element>();
		getChildElements(rootElement);
		return elements;
	}
	
	
	/**
	 * 是否包含某属性值
	 * @param element	需要查询的节点
	 * @param attrName	参数名(注android:这样的前缀不用加)
	 * @return
	 */
	public static boolean hasAttribute(Element element, String attrName) {
		@SuppressWarnings("unchecked")
		List<Attribute> attributes = element.attributes();
		for(Attribute attr : attributes) {
			if(attrName.equals(attr.getName())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 将Document写回至xml文件
	 * @param file
	 * @param doc
	 */
	public static void write2xml(File file, Document doc) {
		XMLWriter writer;
		try {
			writer = new XMLWriter(new FileOutputStream(file));
			writer.write(doc);
			writer.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 替换XML文件中attribute的value值
	 * @param tarDoc		目标document
	 * @param targetList		目标替换文字,模糊替换,即只要匹配list集合里中其中一个就会被抽取替换
	 * @param replacement	替换成的内容
	 * @return 是否有替换操作
	 */
	@SuppressWarnings("unchecked")
	public static boolean replaceAttrValue(Document tarDoc, List<String> targetList, String replacement) {
		boolean hasReplace = false;
		
		List<Element> tarElements = XmlUtil.getAllElements(tarDoc);
		for(Element element : tarElements) {
			List<Attribute> attrs = element.attributes();
			for(Attribute attr : attrs) {
				String attrValue = attr.getValue();
				int index = targetList.indexOf(attrValue);
				if(index != -1) {
					attr.setValue(replacement);
					hasReplace = true;
				}
			}
		}
		return hasReplace;
	}
}