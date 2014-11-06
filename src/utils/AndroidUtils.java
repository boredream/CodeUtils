package utils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;

import other.IdNamingBean;


public class AndroidUtils {
	
	/** DIMEN单位 */
	public static List<String> dimenUnits = new ArrayList<String>();
	static {
		dimenUnits.add("px");
		dimenUnits.add("sp");
		dimenUnits.add("dip");
		dimenUnits.add("dp");
	}

	/**
	 * 设置布局文件的id
	 * <p>
	 * 将设置全部View的id,id名字格式为:布局文件名_view名称_自增整数(全部小写)
	 * 
	 * @param proPath		项目绝对路径
	 * @param layoutXml		布局文件的绝对路径,如xxx/res/layout/main.xml
	 */
	public static void setLayoutId(String proPath, String layoutXml) {
		String layoutName = layoutXml.substring(0, layoutXml.indexOf("."));
		File layoutFile = new File(layoutXml);
		
		Document doc = XmlUtil.read(layoutFile);
		List<Element> allElement = XmlUtil.getAllElements(doc);
		
		int index = 0;
		for (Element element : allElement) {
			if(!XmlUtil.hasAttribute(element, "id")) {
				String idValue = "@+id/" + layoutName 
						+ "_" + element.getName() 
						+ "_" + (index++);
				element.addAttribute("android:id", idValue.toLowerCase(Locale.CHINESE));
			}
		}

		XmlUtil.write2xml(layoutFile, doc);
	}
	
	/**
	 * 自动遍历xml中所有带id的控件,在activity文件中设置对应变量,变量名为id名
	 * @param proPath		项目绝对路径
	 * @param layoutXml		布局文件的绝对路径,如xxx/res/layout/main.xml
	 * @param activityFile	Activity类文件名,如xxx/src/.../MainActivity.java
	 */
	public static void autoFindViewById(String proPath, String layoutXml, String activityFile) {
		File javaFile = new File(activityFile);
		Document document = XmlUtil.read(layoutXml);
		List<Element> allElement = XmlUtil.getAllElements(document);
		
		// 将view名称和对应的id名封装为一个实体类,并存至集合中
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
		
		// 读取java文件的字符串
		String fileContent = FileUtils.readToString(javaFile);
		
		// 根据view名-id名的实体类,依次生成控件对应的成员变量,变量名取id名称赋值
		StringBuilder sb = new StringBuilder();
		for(IdNamingBean bean : idNamingBeans) {
			sb.append("private ")
				.append(bean.viewName)
				.append(" ")
				.append(bean.idName)
				.append(";")
				.append("\n");
		}
		
		// 生成initView自定义方法,并在其中依次findViewById为view成员变量赋值
		sb.append("private void initView(){");
		for(IdNamingBean bean : idNamingBeans) {
			sb.append(bean.idName)
				.append(" = ")
				.append("(" + bean.viewName + ")")
				.append("findViewById(R.id." + bean.idName + ")")
				.append(";\n");
		}
		sb.append("}");
		
		// 将生成的内容写入至java类文件内的起始端
		fileContent = fileContent.replaceFirst("\\{", "\\{" + sb.toString());
		FileUtils.writeString2File(fileContent, javaFile);
	}
	
	/**
	 * 将参数值抽取到values文件夹下
	 * <p>如textColor="#ff00aa",将#ff00aa这样的具体值替换为@color/colorname
	 * <br>并在color.xml文件内创建一个对应颜色item
	 * 
	 * @param proPath			项目绝对路径
	 * @param valuesXml			values文件名称,如strings.xml dimens.xml等
	 * @param type				抽取内容的前缀,如@color/,则type参数就输入"color"
	 * @param itemName			values文件内item的名称
	 * @param itemAttrName		values文件内item的参数,一般都是name
	 * @param itemAttrValue		values文件内item的参数值,也是抽取值后替换的名称
	 * @param itemValue			values文件内item的值,即替换后的值
	 * @param values			被替换的内容,支持模糊替换,只要匹配集合里中其中一个就会被抽取替换,最终抽取成一个值itemValue
	 * @param matchAttr			匹配参数名,即只会替换该参数名对应的值
	 */
	@SuppressWarnings("unchecked")
	public static void extract2values(String proPath, String valuesXml, String type,
			String itemName, String itemAttrName, String itemAttrValue, String itemValue, 
			List<String> values, String matchAttr) {
		List<File> files = FileUtils.getAllFiles(new File(proPath));
		
		String valuesPath = proPath + "/res/values/" + valuesXml;
		File valuesFile = new File(valuesPath);
		if(!valuesFile.exists()) {
			System.out.println("文件不存在,请确定文件["+valuesXml+"]位于/res/values/文件夹下,且文件名称正确");
			return;
		}
		
		int extractFileCount = 0;
		for(File file : files) {
			if(!file.getName().endsWith(".xml")) {
				continue;
			}
			
			if(file.getName().equals(valuesXml)) {
				continue;
			}
			
			Document tarDoc = XmlUtil.read(file);
			
			// 是否有替换操作
			boolean isReplace = XmlUtil.replaceAttrValue(tarDoc, values, "@"+type+"/"+itemAttrValue, matchAttr);
			
			if(!isReplace) {
				continue;
			}
			
			XmlUtil.write2xml(file, tarDoc);
			
			Document valuesDoc = XmlUtil.read(valuesFile);
			Element rootElement = valuesDoc.getRootElement();
			List<Element> elements = rootElement.elements();
			
			// 是否在values/xx.xml对应文件下下已有某个抽取过的值
			boolean hasInValues = false;
			
			for(Element element : elements) {
				String attrValue = element.attributeValue(itemAttrName);
				if(attrValue.equals(itemAttrValue)) {
					hasInValues = true;
					break;
				}
			}
			
			if(!hasInValues) {
				Element element = rootElement.addElement(itemName);
				element.addAttribute(itemAttrName, itemAttrValue);
				element.setText(itemValue);
				
				XmlUtil.write2xml(valuesFile, valuesDoc);
			}
			
			extractFileCount ++;
		}
		
		System.out.println("将" + values 
				+ "抽取为[" + valuesXml 
				+ "]文件下的[" + itemAttrValue + "=" + itemValue + "]");
		System.out.println("共抽取了" + extractFileCount + "个文件下的内容");
		System.out.println("-------------------------");
	}
	
	/**
	 * 抽取为dimen值
	 * 
	 * <p>一般抽取的都是size参数,如20sp,60px,500dp等,范围替换使用带int belowScope, int aboveScope的重载方法
	 * <br>例如:dimenValue=50sp,dimenName=txt_size_large, dimenXml="dimens.xml" 
	 * <br>则将会把xml文件中的所有50sp都替换为@dimen/txt_size_large
	 * <br>并在dimens.xml下创建一个对应的< dimen name="txt_size_large">50sp< /dimen>"
	 * 
	 * @param proPath		项目绝对路径
	 * @param dimenXml		保存dimen的xml文件名称,一般都是dimens.xml
	 * @param dimenName		dimens.xml文件内item的参数值,也是抽取值后替换的名称
	 * @param dimenValue	dimens.xml文件内item的值,即被抽取替换的值
	 */
	public static void extract2Dimen(String proPath, String dimenXml, String dimenName, String dimenValue) {
		extract2Dimen(proPath,dimenXml, dimenName, dimenValue, 0, 0, "");
	}
	
	/**
	 * 抽取为dimen值
	 * 
	 * <p>一般抽取的都是size参数,如20sp,60px,500dp等,支持范围替换
	 * <br>例如:dimenValue=50sp,belowScope=2;aboveScope=4,
	 * <br>则只要是48dp~54dp范围的值都会替换成dimenValue值
	 * 
	 * @param proPath		项目绝对路径
	 * @param dimenXml		保存dimen的xml文件名称,一般都是dimens.xml
	 * @param dimenName		dimens.xml文件内item的参数值,也是抽取值后替换的名称
	 * @param dimenValue	dimens.xml文件内item的值,即抽取前的中心值
	 * @param belowScope	抽取匹配范围最小差值
	 * @param aboveScope	抽取匹配范围最大差值
	 * @param matchAttr		匹配参数名,即只会替换该参数名对应的值
	 */
	public static void extract2Dimen(String proPath, String dimenXml, String dimenName, String dimenValue, 
			int belowScope, int aboveScope, String matchAttr) {
		int index = dimenUnits.indexOf(dimenValue.replaceAll("\\d+", ""));
		if(index == -1) {
			System.out.println("被抽取dimen值的单位不合法,必须为px/sp/dip/dp其中一种");
			return;
		}
		
		int splitIndex = dimenValue.indexOf(dimenUnits.get(index));
		int num = Integer.parseInt(dimenValue.substring(0, splitIndex));
		String unit = dimenValue.substring(splitIndex);
		
		List<String> values = new ArrayList<String>();
		for(int i=num-belowScope; i<=num+aboveScope; i++) {
			values.add(i+unit);
		}
		
		extract2values(proPath, dimenXml, "dimen", "dimen", "name", dimenName, dimenValue, values, matchAttr);
	}
	
	public static void extract2Color(String proPath, String colorXml, String colorName, String RGB) {
		List<String> values = new ArrayList<String>();
		values.add(RGB);
		extract2values(proPath, colorXml, "color", "color", "name", colorName, RGB, values, "");
	}
	
	public static void extract2String(String proPath, String stringXml, String stringName, String stringValue) {
		List<String> values = new ArrayList<String>();
		values.add(stringValue);
		extract2values(proPath, stringXml, "string", "string", "name", stringName, stringValue, values, "");
	}
	
	public static void relaceLayoutViewNames(String proPath, List<String> tarList, String replacement) {
		List<File> allFiles = FileUtils.getAllFiles(proPath);
		for(File file : allFiles) {
			// 如果是xml文件,且在layout..目录下,则视为布局文件
			if(file.getName().endsWith(".xml") 
					&& file.getParentFile().getName().startsWith("layout")) {
				Document document = XmlUtil.read(file);
				boolean hasReplace = XmlUtil.replaceElementName(document, tarList, replacement);
				if(hasReplace) {
					XmlUtil.write2xml(file, document);
				}
			}
		}
	}
	
	/**
	 * 删除无用的src文件
	 * 
	 * @param rootPath		根目录的绝对路径
	 */
	public static void delNoUseSrcFile(String rootPath) {
		List<File> files = FileUtils.getAllFiles(rootPath);
		out:
			for (File file : files) {
				String name = file.getName();
				// 只需要删除src包下的文件
				if(!file.getPath().contains("\\src\\")) {
					continue;
				}
				
				for (File compareFile : files) {
					String compareName = compareFile.getName();
					if(name.equals(compareName)) {
						// 自己跟自己不匹配
						continue;
					}
					
					// 只需要对比src包和layout包下的文件
					if(!compareFile.getPath().contains("\\src\\")
							&& !compareFile.getPath().contains("\\layout")) {
						continue;
					}
					
					if(!compareFile.exists()) {
						continue;
					}
					
					// 如果对比文件的本文内容中包含文件名,则视为有使用
					String fileContent = FileUtils.readToString(compareFile);
					if (fileContent.contains(FileUtils.getName(file))) {
						continue out;
					}
				}
				
				// 删除没使用过的文件
				String absname = file.getAbsoluteFile().getName();
				boolean delete = file.delete();
				System.out.println(absname + " ... delete=" + delete);
			}
	}

	/**
	 * 删除无用的布局xml文件
	 * 
	 * @param rootPath		根目录的绝对路径
	 */
	public static void delNoUseLayoutFile(String rootPath) {
		List<File> files = FileUtils.getAllFiles(rootPath);
		out:
		for (File file : files) {
			String name = file.getName();
			// 只需要删除layout包下的xml文件
			if(!name.endsWith(".xml") 
					|| !file.getPath().contains("\\layout")) {
				continue;
			}
			
			for (File compareFile : files) {
				String compareName = compareFile.getName();
				if(name.equals(compareName)) {
					// 自己跟自己不匹配
					continue;
				}
				
				// 只需要对比src包下和layout包下文件
				if(!compareFile.getPath().contains("\\layout") 
						&& !compareFile.getPath().contains("\\src\\")) {
					continue;
				}
				
				// 如果对比文件的本文内容中包含文件名,则视为有使用
				String fileContent = FileUtils.readToString(compareFile, "UTF-8");
				if (fileContent.contains(FileUtils.getName(file))) {
					continue out;
				}
			}

			// 删除没使用过的文件
			String absname = file.getAbsoluteFile().getName()+"";
			boolean delete = file.delete();
			System.out.println(absname + " ... delete=" + delete);
		}
	}
}
