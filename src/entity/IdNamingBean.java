package entity;

import org.dom4j.Element;

/**
 * 控件名称-id名称实体类
 *
 */
public class IdNamingBean {

	public String viewName;
	public String idName;
	public Element element;

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
