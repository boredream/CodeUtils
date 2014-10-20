package utils;

/**
 * 控件名称-id名称实体类
 *
 */
public class IdNamingBean {

	public String viewName;
	public String idName;

	public IdNamingBean(String viewName, String idName) {
		this.viewName = viewName;
		this.idName = idName;
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
