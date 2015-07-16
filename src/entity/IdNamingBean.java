package entity;

import org.dom4j.Element;

/**
 * 控件名称-id名称实体类
 *
 */
public class IdNamingBean {

	private String viewName;
	private String idName;
	private Element element;
	
	public String getViewName() {
		if(viewName.equals("include")) {
			// include标签的控件名用View
			return "View";
		} else if(viewName.contains(".")) {
			// 去除包名,只取控件名
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

	public IdNamingBean(String viewName, String idName, Element element) {
		this.viewName = viewName;
		this.idName = idName;
		this.element = element;
	}

	@Override
	public String toString() {
		return "IdNamingBean [viewName=" + viewName + ", idName=" + idName
				+ "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IdNamingBean) {
			IdNamingBean bean = (IdNamingBean) obj;
			return this.idName.equals(bean.idName);
		}
		return super.equals(obj);
	}

}
