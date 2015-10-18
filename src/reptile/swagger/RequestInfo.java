package reptile.swagger;

import java.util.ArrayList;

public class RequestInfo {

	public static final String METHOD_GET = "get";
	public static final String METHOD_PUT = "put";
	public static final String METHOD_POST = "post";
	public static final String METHOD_DELETE = "delete";

	private String name;
	private String method;
	private String url;
	private String des;
	private ArrayList<RequestParam> params;

	public static class RequestParam {
		private String name;
		private String type;
		private String des;
		// 参数值为枚举型时
		private ArrayList<String> selectOptions;

		public RequestParam(String name, String type, String des,
				ArrayList<String> selectOptions) {
			this.name = name;
			this.type = type;
			this.des = des;
			this.selectOptions = selectOptions;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getType() {
			// TODO string -> String
			if(type.equals("string")) {
				return "String";
			}
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getDes() {
			return des;
		}

		public void setDes(String des) {
			this.des = des;
		}

		public ArrayList<String> getSelectOptions() {
			return selectOptions;
		}

		public void setSelectOptions(ArrayList<String> selectOptions) {
			this.selectOptions = selectOptions;
		}

		@Override
		public String toString() {
			return "RequestParam [name=" + name + ", type=" + type + ", des="
					+ des + "]";
		}

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public ArrayList<RequestParam> getParams() {
		return params;
	}

	public void setParams(ArrayList<RequestParam> params) {
		this.params = params;
	}

	@Override
	public String toString() {
		return "RequestInfo [name=" + name + ", method=" + method + ", url="
				+ url + ", des=" + des + ", params=" + params + "]";
	}

}
