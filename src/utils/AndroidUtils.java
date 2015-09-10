package utils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;

import entity.IdNamingBean;



public class AndroidUtils {
	
	public static void main(String[] args) {
//		autoCreateAdapter();
		autoCreateActivity();
	}
	
	/** DIMEN单位 */
	public static List<String> dimenUnits = new ArrayList<String>();
	static {
		dimenUnits.add("px");
		dimenUnits.add("sp");
		dimenUnits.add("dip");
		dimenUnits.add("dp");
	}
	
	/**
	 * 将string按需要格式化,前面加缩进符,后面加换行符
	 * @param tabNum 缩进量
	 * @param srcString
	 * @return
	 */
	public static String formatSingleLine(int tabNum, String srcString) {
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<tabNum; i++) {
			sb.append("\t");
		}
		sb.append(srcString);
		sb.append("\n");
		return sb.toString();
	}

	public static List<IdNamingBean> idNamingBeans = new ArrayList<IdNamingBean>();
	/**
	 * 递归获取layout中全部控件
	 * 
	 * @param layoutXml		布局文件的绝对路径,如xxx/res/layout/main.xml
	 * @param include		是否将include引用的布局中内容也获取到
	 */
	public static void parseElementFromXml(String layoutXml, boolean include) {
		Document document = XmlUtil.read(layoutXml);
		List<Element> docElements = XmlUtil.getAllElements(document);
		
		// 将view名称和对应的id名封装为一个实体类,并存至集合中
		for (Element element : docElements) {
			Attribute attrID = element.attribute("id");
			
			// 如果包含include并且需要获取其中内容则进行递归获取
			if(element.getName().equals("include") && include) {
				Attribute attribute = element.attribute("layout");
				// 原布局路径和include中的布局拼成新的路径
				String includeLayoutXml = layoutXml.substring(0, layoutXml.lastIndexOf("\\") + 1)
						+ attribute.getValue().substring(attribute.getValue().indexOf("/") + 1) + ".xml";
				// 继续递归获取include的布局中控件
				parseElementFromXml(includeLayoutXml, include);
			}
			
			// 保存有id的控件信息
			if(attrID != null) {
				String value = attrID.getValue();
				String idName = value.substring(value.indexOf("/") + 1);
				IdNamingBean bean = new IdNamingBean(element.getName(), idName, element);
				if(!idNamingBeans.contains(bean)) {
					idNamingBeans.add(bean);
				}
			}
		}
	}
	
	/**
	 * 自动遍历xml中所有带id的控件,在activity文件中设置对应变量,变量名为id名<br>
	 * <br>
	 * 使用前需要将xml文件内容复制到本项目里的Android文件夹下的layout.xml文件中<br>
	 * 运行该方法后,根据布局文件生成的相关代码会在Android文件夹下Activity.java中查看,可以复制到自己项目里使用
	 */
	public static void autoCreateActivity() {
		// 获取layout中控件信息,信息会保存至idNamingBeans集合中
		parseElementFromXml("Android" + File.separator + "layout.xml", false);
		
		// 解析idNamingBeans集合中的信息,生成页面文本信息
		String activityContent = createActivityContent();
		
		// 获取activity文件
		File javaFile = new File("Android" + File.separator + "Activity.java");
		
		// 将生成的内容写入至java类文件中
		FileUtils.writeString2File(activityContent, javaFile);
	}
	
	/**
	 * 自动遍历xml中所有带id的控件,在activity文件中设置对应变量,变量名为id名
	 * 
	 * @param layoutXml		布局文件的绝对路径,如xxx/res/layout/main.xml
	 * @param activityFile	Activity类文件名,如xxx/src/.../MainActivity.java
	 * @param include		是否将include引用的布局中内容也获取到
	 */
	public static void autoCreateActivity(String layoutXml, String activityFile, boolean include) {
		// 获取layout中控件信息,信息会保存至idNamingBeans集合中
		parseElementFromXml(layoutXml, include);
		
		// 解析idNamingBeans集合中的信息,生成页面文本信息
		String activityContent = createActivityContent();
		
		// 获取activity文件
		File javaFile = new File(activityFile);
		// 读取java文件的字符串
		String fileContent = FileUtils.readToString(javaFile);
		
		// 将生成的内容写入至java类文件内的起始端
		fileContent = fileContent.replaceFirst("\\{", "\\{" + activityContent);
		FileUtils.writeString2File(fileContent, javaFile);
	}

	/**
	 * 生成activity文件内容
	 */
	public static String createActivityContent() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n\n");
		
		// 根据view名-id名的实体类,依次生成控件对应的成员变量,变量名取id名称赋值
		// private TextView tv_name;
		for(IdNamingBean bean : idNamingBeans) {
			sb.append(formatSingleLine(1, "private " + bean.getViewName() + " " + bean.getIdName() + ";"));
		}
		sb.append("\n");
		
		// 生成initView自定义方法,并在其中依次findViewById为view成员变量赋值
		// private void initView() {
		//    tv_name = (TextView)findViewById(R.id.tv_name);
		// }
		sb.append(formatSingleLine(1, "private void initView() {"));
		for(IdNamingBean bean : idNamingBeans) {
			sb.append(formatSingleLine(2, bean.getIdName() + " = " +
					"(" + bean.getViewName() + ") findViewById(R.id." + bean.getIdName() + ");"));
		}
		sb.append("\n");

		// 是否包含可点击的控件,如果包含,自动生成onClick相关代码
		boolean hasClickView = false;
		// 点击事件复写的onclick方法
		// @Override
		// public void onClick(View v) {
		//    switch (v.getId()) {
		//    case R.id.btn_ok:
		// 		// doSomething
		// 		break;
		//    }
		// } 
		StringBuilder sbOnClick = new StringBuilder();
		sbOnClick.append("\n");
		sbOnClick.append(formatSingleLine(1, "@Override"));
		sbOnClick.append(formatSingleLine(1, "public void onClick(View v) {"));
		sbOnClick.append(formatSingleLine(2, "switch (v.getId()) {"));
		
		for(IdNamingBean bean : idNamingBeans) {
			Attribute attrClickable = bean.getElement().attribute("clickable");
			// 只设置Button的点击事件,和参数包含clickable=true的控件
			if(bean.getViewName().equals("Button")
					|| (attrClickable != null 
					&& attrClickable.getValue().equals("true"))) {
				// 设置监听
				// btn_ok.setOnClickListener(this);
				sb.append(formatSingleLine(2, bean.getIdName() + ".setOnClickListener(this);"));
				// 在onclick中分别处理不同id的点击
				sbOnClick.append(formatSingleLine(2, "case R.id." + bean.getIdName() + ":"));
				sbOnClick.append("\n");
				sbOnClick.append(formatSingleLine(3, "break;"));
				
				hasClickView = true;
			}
		}
		sbOnClick.append(formatSingleLine(2, "}"));
		sbOnClick.append(formatSingleLine(1, "}"));
		
		
		// 是否包含RadioGroup/Button等控件,如果包含,自动生成onCheckChanged相关代码
		boolean hasCheckedView = false;
		// 点击事件复写的onclick方法
		// @Override
		// public void onCheckedChanged(RadioGroup group, int checkedId) {
		//    switch (checkedId) {
		//    case R.id.rb_home:
		// 		// doSomething
		// 		break;
		//    }
		// }
		StringBuilder sbOnChecked = new StringBuilder();
		sbOnChecked.append("\n");
		sbOnChecked.append(formatSingleLine(1, "@Override"));
		sbOnChecked.append(formatSingleLine(1, "public void onCheckedChanged(RadioGroup group, int checkedId) {"));
		sbOnChecked.append(formatSingleLine(2, "switch (checkedId) {"));
		
		for(IdNamingBean bean : idNamingBeans) {
			// 只设置Button的点击事件,和参数包含clickable=true的控件
			if(bean.getViewName().equals("RadioGroup")) {
				// 设置监听
				// rg.setOnCheckedChangeListener(this);
				sb.append(formatSingleLine(2, bean.getIdName() + ".setOnCheckedChangeListener(this);"));
				
				hasCheckedView = true;
			} else if(bean.getViewName().equals("RadioButton")) {
				// 在onCheckedChanged中分别处理不同id的选中
				sbOnChecked.append(formatSingleLine(2, "case R.id." + bean.getIdName() + ":"));
				sbOnChecked.append("\n");
				sbOnChecked.append(formatSingleLine(3, "break;"));
				
				hasCheckedView = true;
			}
		}
		sbOnChecked.append(formatSingleLine(2, "}"));
		sbOnChecked.append(formatSingleLine(1, "}"));
		
		sb.append(formatSingleLine(1, "}\n"));
		
		String activityContent = sb.toString() + "\n" + 
				(hasClickView ? sbOnClick.toString() : "") + 
				(hasCheckedView ? sbOnChecked.toString() : "");
		return activityContent;
	}
	
	/**
	 * 自动遍历xml中所有带id的控件,在adapter文件中生成最基本的代码<br>
	 * <br>
	 * 使用前需要将xml文件内容复制到本项目里的Android文件夹下的item.xml文件中<br>
	 * 运行该方法后,根据布局文件生成的相关代码会在Android文件夹下Adapter.java中查看,可以复制到自己项目里使用
	 */
	public static void autoCreateAdapter() {
		String layoutXml = "Android\\item.xml";
		
		// 获取layout中控件信息,信息会保存至idNamingBeans集合中
		parseElementFromXml(layoutXml, false);
		
		// 解析idNamingBeans集合中的信息,生成适配器文本信息
		String adapterContent = createAdapterContent(layoutXml);
		
		// 获取adapter文件
		File javaFile = new File("Android\\Adapter.java");
		
		// 将生成的内容写入至java类文件中
		FileUtils.writeString2File(adapterContent, javaFile);
	}
	
	/**
	 * 自动遍历xml中所有带id的控件,在adapter文件中生成最基本的代码
	 * 
	 * @param layoutXml		item布局文件的绝对路径,如xxx/res/layout/item.xml
	 * @param adapterFile	Adapter类文件名,如xxx/src/.../MyAdapter.java
	 * @param include		是否将include引用的布局中内容也获取到
	 */
	public static void autoCreateAdapter(String layoutXml, String adapterFile, boolean include) {
		parseElementFromXml(layoutXml, include);
		
		String adapterContent = createAdapterContent(layoutXml);
		
		// 读取java文件的字符串
		File javaFile = new File(adapterFile);
		String fileContent = FileUtils.readToString(javaFile);

		// 将生成的内容写入至java类文件内的起始端
		fileContent = fileContent.replaceFirst("\\{", "\\{\n" + adapterContent);
		// 写入回文件
		FileUtils.writeString2File(fileContent, javaFile);
	}

	/**
	 * 生成adapter文件内容
	 */
	private static String createAdapterContent(String layoutXml) {
		StringBuilder sbAdapterInfo = new StringBuilder();
		sbAdapterInfo.append("\n");
		
		// 成员变量,只设置最基本的集合类和context
		sbAdapterInfo.append(formatSingleLine(1, "private Context context;"));
		sbAdapterInfo.append(formatSingleLine(1, "// TODO change the MyItem class to your data bean class"));
		sbAdapterInfo.append(formatSingleLine(1, "private List<MyItem> datas;"));
		sbAdapterInfo.append("\n");
		
		// 根据成员变量创建的构造函数
		sbAdapterInfo.append(formatSingleLine(1, "public MyAdapter(Context context, List<MyItem> datas) {"));
		sbAdapterInfo.append(formatSingleLine(2, "this.context = context;"));
		sbAdapterInfo.append(formatSingleLine(2, "this.datas = datas;"));
		sbAdapterInfo.append(formatSingleLine(1, "}"));
		sbAdapterInfo.append("\n");
		
		// 重写getCount方法
		sbAdapterInfo.append(formatSingleLine(1, "@Override"));
		sbAdapterInfo.append(formatSingleLine(1, "public int getCount() {"));
		sbAdapterInfo.append(formatSingleLine(2, "return datas.size();"));
		sbAdapterInfo.append(formatSingleLine(1, "}"));
		sbAdapterInfo.append("\n");
		
		// 重写getItem方法
		sbAdapterInfo.append(formatSingleLine(1, "@Override"));
		sbAdapterInfo.append(formatSingleLine(1, "public MyItem getItem(int position) {"));
		sbAdapterInfo.append(formatSingleLine(2, "return datas.get(position);"));
		sbAdapterInfo.append(formatSingleLine(1, "}"));
		sbAdapterInfo.append("\n");
		
		// 重写getItemId方法
		sbAdapterInfo.append(formatSingleLine(1, "@Override"));
		sbAdapterInfo.append(formatSingleLine(1, "public long getItemId(int position) {"));
		sbAdapterInfo.append(formatSingleLine(2, "return position;"));
		sbAdapterInfo.append(formatSingleLine(1, "}"));
		sbAdapterInfo.append("\n");
		
		// 重写getView方法,并进行优化处理
		sbAdapterInfo.append(formatSingleLine(1, "@Override"));
		sbAdapterInfo.append(formatSingleLine(1, "public View getView(int position, View convertView, ViewGroup parent) {"));
		sbAdapterInfo.append(formatSingleLine(2, "ViewHolder holder;"));
		sbAdapterInfo.append(formatSingleLine(2, "if(convertView == null) {"));
		sbAdapterInfo.append(formatSingleLine(3, "holder = new ViewHolder();"));
		sbAdapterInfo.append(formatSingleLine(3, "convertView = View.inflate(context, R.layout."+FileUtils.getName(layoutXml)+", null);"));
		
		// getView中viewholder的变量赋值处理
		// 根据view名-id名的实体类,依次生成控件对应的holder变量,变量名取id名称赋值
		for(IdNamingBean bean : idNamingBeans) {
			// holder.item_tv = (TextView) convertView.findViewById(R.id.item_tv);
			sbAdapterInfo.append("\t\t\t")
				.append("holder.")
				.append(bean.getIdName())
				.append(" = (")
				.append(bean.getViewName())
				.append(") ")
				.append("convertView.findViewById(R.id.")
				.append(bean.getIdName())
				.append(");\n");
		}
		sbAdapterInfo.append(formatSingleLine(3, "convertView.setTag(holder);"));
		sbAdapterInfo.append(formatSingleLine(2, "} else {"));
		sbAdapterInfo.append(formatSingleLine(3, "holder = (ViewHolder) convertView.getTag();"));
		sbAdapterInfo.append(formatSingleLine(2, "}"));
		
		sbAdapterInfo.append("\n");
		sbAdapterInfo.append(formatSingleLine(2, "// set data"));
		sbAdapterInfo.append("\n");
		
		sbAdapterInfo.append(formatSingleLine(2, "return convertView;"));
		sbAdapterInfo.append(formatSingleLine(1, "}"));
		sbAdapterInfo.append("\n");
		sbAdapterInfo.append("\n");

		
		// ViewHolder class的申明处理
		sbAdapterInfo.append(formatSingleLine(1, "public static class ViewHolder{"));
		for(IdNamingBean bean : idNamingBeans) {
			// public TextView item_tv;
			sbAdapterInfo.append("\t\t")
				.append("public ")
				.append(bean.getViewName())
				.append(" ")
				.append(bean.getIdName())
				.append(";\n");
		}
		sbAdapterInfo.append(formatSingleLine(1, "}"));
		
		return sbAdapterInfo.toString();
	}
	
	/**
	 * 批量修改图片sel文件名,复制的方式拷贝到同级目录下的sel文件夹中
	 * <br>修改成功后请再使用batchCreateSelFiles方法生成对应的sel.xml文件
	 * 
	 * @param normalPath normal普通状态图片的文件夹
	 * @param specialPath 特殊状态(pressed按下/checked选中)图片的文件夹
	 * @param pre 需要添加的前缀
	 * @param end 特殊状态(pressed按下/checked选中)后缀名
	 */
	public static void batchRenameSelFiles(String normalPath, String specialPath, String pre, String end) {
		String savePath = normalPath.substring(0, normalPath.lastIndexOf("\\"));
		File saveDir = new File(savePath, "sel");
		if(!saveDir.exists()) {
			saveDir.mkdirs();
		}
		
		List<File> files = FileUtils.getAllFiles(normalPath);
		for(File file : files) {
			String fileName = pre + "_" + file.getName();
			fileName = fileName.replace(" ", "_").toLowerCase(Locale.CHINESE);
			
			File nFile = new File(saveDir, fileName);
//			file.renameTo(nFile); // rename 直接移动文件,我们希望是复制并重命名
			FileUtils.copyFileByChannel(file, nFile);
		}
		List<File> filePresses = FileUtils.getAllFiles(specialPath);
		for(File file : filePresses) {
			String[] nameMap = FileUtils.getNameMap(file);
			
			String fileName = pre + "_" + nameMap[0] + "_" + end + nameMap[1];
			fileName = fileName.replace(" ", "_").toLowerCase(Locale.CHINESE);
			
			File nFile = new File(saveDir, fileName);
//			file.renameTo(nFile); // rename 直接移动文件,我们希望是复制并重命名
			FileUtils.copyFileByChannel(file, nFile);
		}
	}
	
	/**
	 * 批量生成sel的xml文件
	 * <br>比如图片名字是ic_img.png和按下的ic_img_pressed.png,那么最终生成的就是封装好的ic_img_sel.xml
	 * 
	 * @param path 包含全部图片的文件路径
	 * @param end 特殊状态(pressed按下/checked选中)后缀名
	 */
	public static void batchCreateSelFiles(String path, String end) {
		List<File> files = FileUtils.getAllFiles(path);
		for(File file : files) {
			String fileName = FileUtils.getName(file);
			// 用normal状态的图片名生成对应的_sel.xml文件
			if(!fileName.endsWith(end)) {
				Document doc = createSelector(fileName, fileName + "_" + end, end);
//				fileName + "_" + end
				File nFile = new File(path, fileName + "_sel.xml");
				XmlUtil.write2xml(nFile, doc);
			}
		}
	}
	
	/**
	 * 自动创建实心矩形sel,包括普通和按下状态的形状和总的sel,生成的xml文件保存在temp文件夹中
	 * 
	 * @param normalColorName 普通状态时的填充颜色
	 * @param specialColorName 特殊状态时的填充颜色
	 * @param cornersRadius 矩形圆角半径,需要带单位,比如4dp
	 * @param end 特殊状态(pressed按下/checked选中)后缀名
	 */
	public static void createShapeSel(String normalColorName, String specialColorName, 
			String cornersRadius, String end) {
		String shape = "rectangle";
		
		Document normalShapeDoc = createShape(shape, cornersRadius, null, null, "@color/" + normalColorName);
		Document specialShapeDoc = createShape(shape, cornersRadius, null, null, "@color/" + specialColorName);
		
		String normalShapeName = shape + "_" + normalColorName;
		String specialShapeName = shape + "_" + specialColorName;
		
		String path = "temp";
		File normalShapeFile = new File(path, normalShapeName + ".xml");
		File specialShapeFile = new File(path, specialShapeName + ".xml");
		XmlUtil.write2xml(normalShapeFile, normalShapeDoc);
		XmlUtil.write2xml(specialShapeFile, specialShapeDoc);
		
		Document selDoc = AndroidUtils.createSelector(normalShapeName, specialShapeName, "pressed");
		File selFile = new File(path, shape + "_" + normalColorName + "2" + specialColorName + "_sel.xml");
		XmlUtil.write2xml(selFile, selDoc);
	}
	
	/**
	 * 自动生成shape(只考虑shape,corner,stroke,solid参数)
	 * 
	 * @param shape 形状,一共有四种:rectangle矩形,oval圆,line线,ring环
	 * @param cornersRadius 圆角边半径,需要带单位,比如4dp
	 * @param strokeWidth 边线宽度,需要带单位,比如1px(stroke为可选,不要边线时传入空即可)
	 * @param strokeColor 边线颜色(可以是#ARGB或者是@color/颜色名)
	 * @param solidColor 填充颜色(可以是#ARGB或者是@color/颜色名)
	 * @return
	 */
	public static Document createShape(String shape, String cornersRadius, 
			String strokeWidth, String strokeColor, String solidColor) {
//		<?xml version="1.0" encoding="utf-8"?>
//		<shape xmlns:android="http://schemas.android.com/apk/res/android"
//		    android:shape="rectangle" >
//
//		    <corners android:radius="4dp" />
//
//		    <stroke
//		        android:width="1px"
//		        android:color="@color/white" />
//
//		    <solid android:color="@color/transparent" />
//
//		</shape>
		
		Document doc = XmlUtil.read("res\\drawable\\shape_correct.xml");
		Element rootElement = doc.getRootElement();
		rootElement.attribute("shape").setValue(shape);
		
		Element cornerElement = rootElement.element("corners");
		cornerElement.attribute("radius").setValue(cornersRadius);
		
		Element strokeElement = rootElement.element("stroke");
		if(strokeWidth == null || strokeWidth.length() == 0) {
			rootElement.remove(strokeElement);
		} else {
			strokeElement.attribute("width").setValue(strokeWidth);
			strokeElement.attribute("color").setValue(strokeColor);
		}
		
		Element solidElement = rootElement.element("solid");
		solidElement.attribute("color").setValue(solidColor);
		
		return doc;
	}
	
	/**
	 * 生成sel的document
	 * 
	 * @param normalName normal普通状态的图片名
	 * @param specialName 特殊状态(pressed按下/checked选中)的图片名
	 * @param end 特殊状态(pressed按下/checked选中)后缀名
	 * @return
	 */
	public static Document createSelector(String normalName, String specialName, String end) {
		Document doc = XmlUtil.read("res\\drawable\\sel.xml");
		Element rootElement = doc.getRootElement();
		
		List<Element> elements = XmlUtil.getAllElements(doc);
		
		for(Element element : elements) {
			Attribute attr = element.attribute("drawable");
			String value = attr.getStringValue();
			if(value.contains(end)) {
				// 替换特殊状态(pressed/checked)的item加后缀
				value = value.replace(end, specialName);
				attr.setValue(value);
			} else if(element.attributeCount() > 1){
				// 移除不需要的element
				rootElement.remove(element);
			} else {
				// normal状态的item不加后缀
				value = value.replace("normal", normalName);
				attr.setValue(value);
			}
		}
		
		return doc;
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
	public static void extract2valuesByJsoup(String proPath, String valuesXml, String type,
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
	 * <p>一般抽取的都是size参数,如20sp,60px,500dp等,支持范围替换
	 * <br>例如:dimenValue=50sp,belowScope=2;aboveScope=4,
	 * <br>则只要是48dp~54dp范围的值都会替换成dimenValue值
	 * 
	 * @param proPath		项目绝对路径
	 * @param dimenXml		保存dimen的xml文件名称,一般都是dimens.xml
	 * @param dimenName		dimens.xml文件内item的参数值,也是抽取值后替换的名称
	 * @param dimenValue	dimens.xml文件内item的值,即抽取前值
	 * @param matchAttr		匹配参数名,即只会替换该参数名对应的值
	 */
	public static void extract2Dimen(String proPath, 
			String dimenXml, String dimenName, String dimenValue, String matchAttr) {
		// 浮点型单位值
		String regex = "^(-?\\d+)(\\.\\d+)?(dp|dip|sp|px)$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(dimenValue);
		System.out.println(matcher.matches());
		
		int index = dimenUnits.indexOf(dimenValue.replaceAll("\\d+", ""));
		if(index == -1) {
			System.out.println("被抽取dimen值的单位不合法,必须为px/sp/dip/dp其中一种");
			return;
		}
		
		List<String> values = new ArrayList<String>();
		values.add(dimenValue);
		
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
