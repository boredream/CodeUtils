package utils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
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
	 * 获得Xml文档对象
	 */
	public static Document read(File xmlFile) {
		Document document = null;
		try {
			document = saxReader.read(xmlFile);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return document;
	}

	/**
	 * 获得Xml文档对象
	 * 
	 * @param xmlFileName xml文件的绝对路径
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
			e.printStackTrace();
		}
		return document;
	}
	
	/**
	 * 递归获取到的element集合
	 */
	static List<Element> elements = new ArrayList<Element>();
	/**
	 * 递归获取全部element
	 * 
	 * @param parentElement	发起递归的根element
	 */
	public static void getElements(Element parentElement) {
		@SuppressWarnings("unchecked")
		List<Element> elementList = parentElement.elements();
		for(Element element : elementList) {
			elements.add(element);
			if(element.elements().size() > 0) {
				getElements(element);
			}
		}
	}
	
	/**
	 * 获取document下全部element
	 * @param Document
	 * @return
	 */
	public static List<Element> getAllElements(Document doc) {
		Element rootElement = doc.getRootElement();
		
		elements = new ArrayList<Element>();
		getElements(rootElement);
		return elements;
	}
	
	
	/**
	 * 是否包含某属性值
	 * @param element	需要查询的节点
	 * @param attrName	参数名(注android:这样的命名空间不用加)
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
	 * @param targetString	目标替换文字
	 * @param replacement	替换成的内容
	 * @param matchAttr		匹配参数名,即只会替换该参数名对应的值
	 * @return 是否有替换操作
	 */
	public static boolean replaceAttrValue(Document tarDoc, String targetString, String replacement, String matchAttr) {
		List<String> targetList = new ArrayList<String>();
		targetList.add(targetString);
		return replaceAttrValue(tarDoc, targetList, replacement, matchAttr);
	}
	
	/**
	 * 替换XML文件中attribute的value值
	 * @param tarDoc		目标document
	 * @param tarStrList	目标替换文字,模糊替换,即只要匹配list集合里中其中一个就会被抽取替换
	 * @param replacement	替换成的内容
	 * @param matchAttr		匹配参数名,即只会替换该参数名对应的值,null时不做筛选
	 * @return 是否有替换操作
	 */
	@SuppressWarnings("unchecked")
	public static boolean replaceAttrValue(Document tarDoc, List<String> tarStrList, String replacement, String matchAttr) {
		boolean hasReplace = false;
		
		List<Element> tarElements = XmlUtil.getAllElements(tarDoc);
		for(Element element : tarElements) {
			List<Attribute> attrs = element.attributes();
			for(Attribute attr : attrs) {
				if(matchAttr == null || !attr.getName().equals(matchAttr+"")) {
					continue;
				}
				String attrValue = attr.getValue();
				int index = tarStrList.indexOf(attrValue);
				if(index != -1) {
					attr.setValue(replacement);
					hasReplace = true;
				}
			}
		}
		return hasReplace;
	}
	
	/**
	 * 替换XML文件中attribute的value值中部分字符串
	 * @param tarDoc		目标document
	 * @param tarStr		目标替换文字正则
	 * @param replacement	替换成的内容
	 * @param matchAttr		匹配参数名,即只会替换该参数名对应的值,null时不做筛选
	 * @return 是否有替换操作
	 */
	@SuppressWarnings("unchecked")
	public static boolean replacePartAttrValue(Document tarDoc, String tarStr, String replacement, String matchAttr) {
		boolean hasReplace = false;
		
		List<Element> tarElements = XmlUtil.getAllElements(tarDoc);
		for(Element element : tarElements) {
			List<Attribute> attrs = element.attributes();
			for(Attribute attr : attrs) {
				if(matchAttr == null || !attr.getName().equals(matchAttr+"")) {
					continue;
				}
				
				String attrValue = attr.getValue();
				if(attrValue.contains(tarStr)) {
					hasReplace = true;
					attr.setValue(attrValue.replace(tarStr, replacement));
				}
			}
		}
		return hasReplace;
	}
	
	
	/**
	 * 替换指定目录下全部xml文件内符合自定义条件的Element名称
	 * 
	 * @param tarDoc		目标document
	 * @param targetList	目标替换文字,模糊替换,即只要匹配list集合里中其中一个就会被抽取替换
	 * @param replacement	替换成的内容
	 * @return 是否有替换操作
	 */
	public static boolean replaceElementName(Document tarDoc, List<String> targetList, String replacement) {
		boolean hasReplace = false;
		
		List<Element> tarElements = XmlUtil.getAllElements(tarDoc);
		for(Element element : tarElements) {
			int index = targetList.indexOf(element.getName());
			if(index != -1) {
				element.setName(replacement);
				hasReplace = true;
			}
		}
		return hasReplace;
	}
}