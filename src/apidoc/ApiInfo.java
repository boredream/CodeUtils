package apidoc;

import java.util.ArrayList;
import java.util.List;

// api字段实体类
public class ApiInfo {
    private String name;
    private String url;
    private String method;
    private List<ApiField> requestParams = new ArrayList<>();
    private List<ApiField> responseParams = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<ApiField> getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(List<ApiField> requestParams) {
        this.requestParams = requestParams;
    }

    public List<ApiField> getResponseParams() {
        return responseParams;
    }

    public void setResponseParams(List<ApiField> responseParams) {
        this.responseParams = responseParams;
    }
}
