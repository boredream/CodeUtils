package utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ObjectiveCUtils {

	public static void createModels() {
		// url map
		String contentUrl = FileUtils.readToString(new File("mac\\B2CConstants.java"), "UTF-8");
		Map<String, String> urlMap = getUrlMap(contentUrl);

		String content = FileUtils.readToString(new File("mac\\B2CHttpRequest.java"), "UTF-8");
		String regex = "\\tpublic static void [a-zA-Z0-9]+\\([\\S ]+\\) \\{\\r\\n"
				+ "([\\t\\S \\\\]+\\r\\n)+"
				+ "\\t}";
		Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			// get method content
			String method = matcher.group();

			String methodName = method.substring(20, method.indexOf("(")).replace("request", "");
			// 开头字母小写
			String modelNameFL = methodName + "Model";
			// 开头字母大写
			String modelName = StringUtils.firstToUpperCase(modelNameFL);

			// get params
			ArrayList<String> paramsKey = getParams(method);

			// HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH
			StringBuilder sbh = createH(methodName, modelNameFL, modelName, paramsKey);
			File fileH = new File("mac\\Model\\" + modelName + ".h");
			FileUtils.writeString2File(sbh.toString(), fileH, "UTF-8");

			// find url
			String regexUrl = "\\(B2CConstants.([a-z_A-Z0-9]+)\\)";
			Pattern patternUrl = Pattern.compile(regexUrl);
			Matcher matcherUrl = patternUrl.matcher(content);
			String url = "";
			if (matcherUrl.find()) {
				url = urlMap.get(matcherUrl.group(1));
			}

			// MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
			StringBuilder sbm = createM(methodName, modelNameFL, modelName, paramsKey, url);
			File file = new File("mac\\Model\\" + modelName + ".m");
			FileUtils.writeString2File(sbm.toString(), file, "UTF-8");
		}
	}

	private static ArrayList<String> getParams(String method) {
		String regexParams = "params\\.put\\(\"([a-z_A-Z]+)\",";
		Pattern patternParams = Pattern.compile(regexParams, Pattern.DOTALL);
		Matcher matcherParams = patternParams.matcher(method);

		ArrayList<String> paramsKey = new ArrayList<String>();
		while (matcherParams.find()) {
			if (!paramsKey.contains(matcherParams.group())) {
				paramsKey.add(matcherParams.group(1));
			}
		}
		return paramsKey;
	}

	private static Map<String, String> getUrlMap(String contentUrl) {
		String regexUrl = "\\tpublic static final String ([A-Z_0-9]+) = \"([A-Z_a-z0-9/\\.]+)\";\\r\\n";
		Pattern patternUrl = Pattern.compile(regexUrl);
		Matcher matcherUrl = patternUrl.matcher(contentUrl);

		Map<String, String> urlMap = new HashMap<String, String>();
		while (matcherUrl.find()) {
			String group1 = matcherUrl.group(1);
			String group2 = matcherUrl.group(2);
			urlMap.put(group1, group2);
		}
		return urlMap;
	}

	private static StringBuilder createH(String methodName, String modelNameFL, String modelName, ArrayList<String> paramsKey) {
		StringBuilder sbh = new StringBuilder();
		sbh.append(AndroidUtils.formatSingleLine(0, "#import <Foundation/Foundation.h>"));
		sbh.append(AndroidUtils.formatSingleLine(0, ""));
		sbh.append(AndroidUtils.formatSingleLine(0, "@class AFHTTPRequestOperation;"));
		sbh.append(AndroidUtils.formatSingleLine(0, "@protocol " + modelName + "Delegate;"));
		sbh.append(AndroidUtils.formatSingleLine(0, ""));
		sbh.append(AndroidUtils.formatSingleLine(0, "@interface " + modelName + " : NSObject"));
		sbh.append(AndroidUtils.formatSingleLine(0, ""));

		// property
		sbh.append(AndroidUtils.formatSingleLine(0, "@property (assign, nonatomic) NSInteger ret;"));
		sbh.append(AndroidUtils.formatSingleLine(0, "@property (copy, nonatomic) NSString * ret_msg;"));
		for (String params : paramsKey) {
			sbh.append(AndroidUtils.formatSingleLine(0, "@property (copy, nonatomic) NSString * " + params + ";"));
		}
		sbh.append(AndroidUtils.formatSingleLine(0, "@property (weak, nonatomic) id<" + modelName + "Delegate> " + modelNameFL + "Delegate;"));
		sbh.append(AndroidUtils.formatSingleLine(0, ""));
		sbh.append(AndroidUtils.formatSingleLine(0, "- (void)setWithDictionary:(NSDictionary *)dictionary;"));

		// request method
		StringBuilder sbRequestMethod = new StringBuilder();
		sbRequestMethod.append("- (void)" + methodName + ":");
		for (int i = 0; i < paramsKey.size(); i++) {
			String p = paramsKey.get(i);
			if (i == 0) {
				sbRequestMethod.append("(NSString *)" + p);
			} else {
				sbRequestMethod.append(" " + p + ":(NSString *)" + p);
			}
		}
		sbRequestMethod.append(";");
		sbh.append(sbRequestMethod.toString());
		sbh.append(AndroidUtils.formatSingleLine(0, ""));

		sbh.append(AndroidUtils.formatSingleLine(0, ""));
		sbh.append(AndroidUtils.formatSingleLine(0, "@end"));
		sbh.append(AndroidUtils.formatSingleLine(0, ""));
		sbh.append(AndroidUtils.formatSingleLine(0, "@protocol " + modelName + "Delegate <NSObject>"));
		sbh.append(AndroidUtils.formatSingleLine(0, ""));
		sbh.append(AndroidUtils.formatSingleLine(0, "- (void)" + modelNameFL + ":(" + modelName + " *)" + modelNameFL
				+ " httpRequestSuccessWithOperation:(AFHTTPRequestOperation *)operation responseObject:(id)responseObject;"));
		sbh.append(AndroidUtils.formatSingleLine(0, "- (void)" + modelNameFL + ":(" + modelName + " *)" + modelNameFL
				+ " httpRequestFailureWithOperation:(AFHTTPRequestOperation *)operation error:(NSError *)error;"));
		sbh.append(AndroidUtils.formatSingleLine(0, "- (void)" + modelNameFL + ":(" + modelName + " *)" + modelNameFL + " did" + StringUtils.firstToUpperCase(methodName)
				+ "WithRetCode:(NSInteger)retCode;"));
		sbh.append(AndroidUtils.formatSingleLine(0, ""));
		sbh.append(AndroidUtils.formatSingleLine(0, "@end"));
		return sbh;
	}

	private static StringBuilder createM(String methodName, String modelNameFL, String modelName, ArrayList<String> paramsKey, String url) {
		StringBuilder sbm = new StringBuilder();
		sbm.append(AndroidUtils.formatSingleLine(0, "#import \"" + modelName + "Model.h\""));
		sbm.append(AndroidUtils.formatSingleLine(0, "#import \"AFNetworking.h\""));
		sbm.append(AndroidUtils.formatSingleLine(0, "#import \"NSString+Time.h\""));
		sbm.append(AndroidUtils.formatSingleLine(0, "#import \"NSString+Sign.h\""));
		sbm.append(AndroidUtils.formatSingleLine(0, "#import \"NSString+Encrypt.h\""));
		sbm.append(AndroidUtils.formatSingleLine(0, ""));

		sbm.append(AndroidUtils.formatSingleLine(0, "@interface " + modelName + " ()"));
		sbm.append(AndroidUtils.formatSingleLine(0, ""));

		sbm.append(AndroidUtils.formatSingleLine(0, "@property (strong, nonatomic) AFHTTPRequestOperationManager * httpRequestOperationManager;"));
		sbm.append(AndroidUtils.formatSingleLine(0, ""));

		sbm.append(AndroidUtils.formatSingleLine(0, "@end"));
		sbm.append(AndroidUtils.formatSingleLine(0, ""));

		sbm.append(AndroidUtils.formatSingleLine(0, "@implementation " + modelName));
		sbm.append(AndroidUtils.formatSingleLine(0, ""));

		sbm.append(AndroidUtils.formatSingleLine(0, "#pragma mark - setter"));
		sbm.append(AndroidUtils.formatSingleLine(0, "- (void)setWithDictionary:(NSDictionary *)dictionary {"));
		sbm.append(AndroidUtils.formatSingleLine(1, "self.ret = [dictionary[@\"ret\"] integerValue];"));
		sbm.append(AndroidUtils.formatSingleLine(1, "self.ret_msg = dictionary[@\"ret_msg\"];"));
		sbm.append(AndroidUtils.formatSingleLine(1, "if (self.ret == 0) {"));
		sbm.append(AndroidUtils.formatSingleLine(2, "# do something when success ~~~~~~~~"));
		sbm.append(AndroidUtils.formatSingleLine(1, "}"));
		sbm.append(AndroidUtils.formatSingleLine(0, "}"));
		sbm.append(AndroidUtils.formatSingleLine(0, ""));

		sbm.append(AndroidUtils.formatSingleLine(0, "#pragma mark - getter"));
		sbm.append(AndroidUtils.formatSingleLine(0, "- (AFHTTPRequestOperationManager *)httpRequestOperationManager {"));
		sbm.append(AndroidUtils.formatSingleLine(1, "if (!_httpRequestOperationManager) {"));
		sbm.append(AndroidUtils.formatSingleLine(2, "_httpRequestOperationManager = [[AFHTTPRequestOperationManager alloc] initWithBaseURL:[NSURL URLWithString:@\"http://27.54.228.172:9080\"]];"));
		sbm.append(AndroidUtils.formatSingleLine(2, "_httpRequestOperationManager.requestSerializer.timeoutInterval = 5;"));
		sbm.append(AndroidUtils.formatSingleLine(2, "_httpRequestOperationManager.responseSerializer.acceptableContentTypes = [NSSet setWithObject:@\"text/html\"];"));
		sbm.append(AndroidUtils.formatSingleLine(1, "}"));
		sbm.append(AndroidUtils.formatSingleLine(1, "return _httpRequestOperationManager;"));
		sbm.append(AndroidUtils.formatSingleLine(0, "}"));
		sbm.append(AndroidUtils.formatSingleLine(0, ""));

		sbm.append(AndroidUtils.formatSingleLine(0, "#pragma mark - Controller"));
		// request method
		StringBuilder sbRequestMethodM = new StringBuilder();
		sbRequestMethodM.append("- (void)" + methodName + ":");
		for (int i = 0; i < paramsKey.size(); i++) {
			String p = paramsKey.get(i);
			if (i == 0) {
				sbRequestMethodM.append("(NSString *)" + p);
			} else {
				sbRequestMethodM.append(" " + p + ":(NSString *)" + p);
			}
		}
		sbRequestMethodM.append(" {");
		sbRequestMethodM.append(AndroidUtils.formatSingleLine(0, ""));
		sbm.append(sbRequestMethodM.toString());
		sbm.append(AndroidUtils.formatSingleLine(0, ""));

		sbm.append(AndroidUtils.formatSingleLine(1, "// 提交数据"));
		sbm.append(AndroidUtils.formatSingleLine(1, "NSDictionary * dictionary = @{"));

		StringBuilder sbDictionary = new StringBuilder();
		for (int i = 0; i < paramsKey.size(); i++) {
			String p = paramsKey.get(i);
			if (p.contains("lkey")) {
				sbDictionary.append(AndroidUtils.formatSingleLine(4, "@\"" + p + "\":[" + sbDictionary + " md5_32]"
						+ (i == paramsKey.size() ? "," : "};")));
			} else if (p.equals("time")) {
				sbDictionary.append(AndroidUtils.formatSingleLine(4, "@\"" + p + "\":[NSString timestamp]"
						+ (i == paramsKey.size() ? "," : "};")));
			} else if (p.equals("sign")) {
				sbDictionary.append(AndroidUtils.formatSingleLine(4, "@\"" + p + "\":[NSString sign]"
						+ (i == paramsKey.size() ? "," : "};")));
			} else {
				sbDictionary.append(AndroidUtils.formatSingleLine(4, "@\"" + p + "\":" + p
						+ (i == paramsKey.size() ? "," : "};")));
			}
		}

		sbm.append(sbDictionary.toString());
		sbm.append(AndroidUtils.formatSingleLine(0, ""));

		sbm.append(AndroidUtils.formatSingleLine(1, "// 处理接收的数据"));
		sbm.append(AndroidUtils.formatSingleLine(1, "[self.httpRequestOperationManager POST:@\"" + url + "\" parameters:dictionary success:^(AFHTTPRequestOperation *operation, id responseObject) {"));
		sbm.append(AndroidUtils.formatSingleLine(2, "// http请求成功"));
		sbm.append(AndroidUtils.formatSingleLine(2, "if ([self." + modelName + "Delegate respondsToSelector:@selector(" + modelNameFL + ":httpRequestSuccessWithOperation:responseObject:)]) {"));
		sbm.append(AndroidUtils.formatSingleLine(3, "[self." + modelName + "Delegate " + modelName + ":self httpRequestSuccessWithOperation:operation responseObject:responseObject];"));
		sbm.append(AndroidUtils.formatSingleLine(2, "}"));
		sbm.append(AndroidUtils.formatSingleLine(2, "// 处理返回数据"));
		sbm.append(AndroidUtils.formatSingleLine(2, "[self setWithDictionary:responseObject];"));
		sbm.append(AndroidUtils.formatSingleLine(2, "// 根据返回数据的不同状态，进行处理"));
		sbm.append(AndroidUtils.formatSingleLine(2, "if ([self." + modelName + "Delegate respondsToSelector:@selector(" + modelName + ":did" + StringUtils.firstToUpperCase(methodName)
				+ "WithRetCode:)]) {"));
		sbm.append(AndroidUtils.formatSingleLine(3, "[self." + modelName + "Delegate " + modelName + ":self did" + StringUtils.firstToUpperCase(methodName) + "WithRetCode:self.ret];"));
		sbm.append(AndroidUtils.formatSingleLine(2, "}"));
		sbm.append(AndroidUtils.formatSingleLine(1, "} failure:^(AFHTTPRequestOperation *operation, NSError *error) {"));
		sbm.append(AndroidUtils.formatSingleLine(2, "if ([self." + modelName + "Delegate respondsToSelector:@selector(" + modelName + ":httpRequestFailureWithOperation:error:)]) {"));
		sbm.append(AndroidUtils.formatSingleLine(3, "[self." + modelName + "Delegate " + modelName + ":self httpRequestFailureWithOperation:operation error:error];"));
		sbm.append(AndroidUtils.formatSingleLine(2, "}"));
		sbm.append(AndroidUtils.formatSingleLine(1, "}]"));
		sbm.append(AndroidUtils.formatSingleLine(0, "}"));
		sbm.append(AndroidUtils.formatSingleLine(0, ""));

		sbm.append(AndroidUtils.formatSingleLine(0, "@end"));
		return sbm;
	}
}
