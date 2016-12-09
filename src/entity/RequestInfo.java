package entity;

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
	private ResultInfo result;
	
	/**
	 * 返回数据json转换成的JavaBean代码字符串
	 */
	private String responseJson;

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
	
	public String getResponseJson() {
		return responseJson;
	}

	public void setResponseJson(String responseJson) {
		this.responseJson = responseJson;
	}

	@Override
	public String toString() {
		return "RequestInfo [name=" + name + ", method=" + method + ", url="
				+ url + ", des=" + des + ", params=" + params + "]";
	}

	@Override
	public boolean equals(Object o) {
		if(o instanceof RequestInfo) {
			RequestInfo oInfo = (RequestInfo) o;
			return this.url.equals(oInfo.url);
		}
		return super.equals(o);
	}
}
