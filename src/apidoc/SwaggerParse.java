package apidoc;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import utils.HttpUtils;
import utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SwaggerParse {

	public static ArrayList<ApiInfo> parseDocFromUrl(String url, List<String> ignoreFields) throws Exception {
		ArrayList<ApiInfo> requestInfoList = new ArrayList<>();
		String string = HttpUtils.getString(url);
		JsonElement root = new JsonParser().parse(string);
		JsonObject joPaths = root.getAsJsonObject().get("paths").getAsJsonObject();
		for (Map.Entry<String, JsonElement> entryPath : joPaths.entrySet()) {
			// 按path分
			String path = entryPath.getKey();
			for (Map.Entry<String, JsonElement> entryMethod : entryPath.getValue().getAsJsonObject().entrySet()) {
				// 按method分
				String method = entryMethod.getKey().toUpperCase();
				JsonObject joApi = entryMethod.getValue().getAsJsonObject();
				String summary = joApi.get("summary").getAsString();

				ApiInfo requestInfo = new ApiInfo();
				requestInfo.setUrl(path);
				requestInfo.setName(summary);
				requestInfo.setMethod(method);

				ArrayList<ApiField> params = new ArrayList<>();
				if(joApi.has("parameters")) {
					for (JsonElement jeItem : joApi.get("parameters").getAsJsonArray()) {
						JsonObject joItem = jeItem.getAsJsonObject();
						String name = joItem.get("name").getAsString();
						if (ignoreFields.contains(name)) {
							// 忽略字段
							continue;
						}

						ApiField param = new ApiField();
						param.name = name;
						param.in = joItem.get("in").getAsString();
						param.desc = joItem.get("description").getAsString();

						if("body".equalsIgnoreCase(param.in)) {
							// post body
							JsonObject joSchema = joItem.get("schema").getAsJsonObject();
							if(joSchema.has("$ref")) {
								param.type = "body";
								param.schema = joSchema.get("$ref").getAsString()
										.replace("#/definitions/", "")
										.replace("对象", "");
							} else {
								// path
								param.in = "path";
								param.type = joSchema.get("type").getAsString();
							}
						} else {
							// query or path
							param.type = joItem.get("type").getAsString();
						}
						param.in = StringUtils.firstToUpperCase(param.in);

						params.add(param);
					}
				}
				requestInfo.setRequestParams(params);
				requestInfoList.add(requestInfo);
			}
		}
		return requestInfoList;
	}

}
