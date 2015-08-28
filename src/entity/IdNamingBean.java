package entity;

import org.dom4j.Element;

/**
 * 控件名称-id名称数据
 */
public class IdNamingBean {

	/**
	 * 控件名称
	 */
	private String viewName;
	/**
	 * 控件id名称
	 */
	private String idName;
	/**
	 * 控件解析成的的标签元素
	 */
	private Element element;
	
	public IdNamingBean(String viewName, String idName, Element element) {
		this.viewName = viewName;
		this.idName = idName;
		this.element = element;
	}
	
	public String getViewName() {
		if(viewName.equals("include")) {
			// include标签的控件名用View
			return "View";
		} else if(viewName.contains(".")) {
			// ViewPager和自定义控件等带报名的,去除包名只取控件名称
			return viewName.substring(viewName.lastIndexOf(".") + 1);
		} else {
			return viewName;
		}
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public String getIdName() {
		return idName;
	}

	public void setIdName(String idName) {
		this.idName = idName;
	}

	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
	}

	@Override
	public String toString() {
		return "IdNamingBean [viewName=" + viewName + ", idName=" + idName + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IdNamingBean) {
			IdNamingBean bean = (IdNamingBean) obj;
			// equals用id名称比较
			return this.idName.equals(bean.idName);
		}
		return super.equals(obj);
	}

}
