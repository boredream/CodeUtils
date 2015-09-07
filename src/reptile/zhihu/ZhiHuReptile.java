package reptile.zhihu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import utils.HttpUtils;

import com.google.gson.Gson;

public class ZhiHuReptile {

	private static String hostUrl = "http://www.zhihu.com";
	private static String allTopicsUrl = hostUrl + "/topics";

	public static void main(String[] args) throws Exception {
		List<Topic> topics = getAllTopics();
		
//		for(int i=0; i<topics.size(); i++) {
//			String topicUrl = getTopicUrl(topics.get(i));
//			getTop100Ansers(topicUrl);
//		}
		
		String topicUrl = getTopicUrl(topics.get(0));
		getTop100Ansers(topicUrl);
	}
	
	
	
	private static void getTop100Ansers(String topicUrl) throws Exception {
//		http://www.zhihu.com/topic/19550517/top-answers
		String topAnswersOfTopic = topicUrl + "/top-answers";
		
//		<div class="content">
		String response = HttpUtils.getString(topAnswersOfTopic);
		Document parse = Jsoup.parse(response);
		Elements elements = parse.getElementsByAttributeValue("class", "content");
		
		for(Element element : elements) {
			Answer answer = parseAnswer(element);
			System.out.println(answer);
		}
	}



	public static Answer parseAnswer(Element answerRootElement) throws Exception {
//		<a class="question_link" target="_blank" href="/question/25504353">经常上知乎会带来什么错觉？</a>
		Element questionElement = answerRootElement.getElementsByAttributeValue("class", "question_link").get(0);
		String questionName = questionElement.text();
		String questionLink = hostUrl + questionElement.attr("href");
		
//		<div class="zm-item-vote-info " data-votecount="40947">
		Element voteCountElement = answerRootElement.getElementsByAttributeValueContaining("class", "zm-item-vote-info").get(0);
		String voteCount = voteCountElement.attr("data-votecount");
		
//		<span class="answer-date-link-wrap">
//		<a class="answer-date-link last_updated meta-item" data-tip="s$t$发布于 2014-09-24" target="_blank" href="/question/25504353/answer/30949097">编辑于 2014-09-24</a>
//		</span>
		Element answerLinkElement = answerRootElement.getElementsByAttributeValueContaining("href", "answer").get(0);
		String answerLink = hostUrl + answerLinkElement.attr("href");
		
//		<div class="zh-summary summary clearfix">
//		211 985 高考就是纸老虎gpa 3.8 托福雅思都是渣研究生 博士后 本科毕业像条狗北上广 英美欧 要想成功出亚洲 白瘦美 高富帅 满街都是官二代设计师 程序猿 就我一人还没钱大长腿 一八零 六块腹肌才算赢健身房 瑜伽馆 二十开练都算晚ipad mbp 4k才能玩游戏flym…
//		<a href="/question/25504353/answer/30949097" class="toggle-expand">显示全部</a>
//		</div>
		Element summaryElement = answerRootElement.getElementsByAttributeValue("class", "zh-summary summary clearfix").get(0);
		String summary = summaryElement.text();
		// remove 显示全部
		summary = summary.substring(0, summary.length() - 4);
		
		Answer answer = new Answer();
		answer.questionName = questionName;
		answer.questionLink = questionLink;
		answer.voteCount = voteCount;
		answer.answerLink = answerLink;
		answer.summary = summary;
		
		return answer;
	}
	
	static class Answer {
		String questionName;
		String questionLink;
		String voteCount;
		String answerLink;
		String summary;
		
		@Override
		public String toString() {
			return "Answer [questionName=" + questionName + ", questionLink="
					+ questionLink + ", voteCount=" + voteCount
					+ ", answerLink=" + answerLink + ", summary=" + summary
					+ "]";
		}
	}

	public static List<Topic> getAllTopics() throws Exception {
		// <li data-id="99" class="current"><a href="#互联网">互联网</a></li>
		List<Topic> topicses = new ArrayList<Topic>();

		String response = HttpUtils.getString(allTopicsUrl);
		Document parse = Jsoup.parse(response);
		Elements elements = parse.getElementsByAttributeValueStarting("href",
				"#");

		for (Element element : elements) {
			Topic topic = new Topic();
			topic.name = element.text();
			topic.topic_id = element.parent().attr("data-id");
			topicses.add(topic);
		}

		return topicses;
	}
	
	static class Topic {
		String name;
		String topic_id;

		@Override
		public String toString() {
			return "Topic [name=" + name + ", topic_id=" + topic_id + "]";
		}

	}

	public static String getTopicUrl(Topic topic)
			throws Exception {
		// AJAX
		// Request URL:http://www.zhihu.com/node/TopicsPlazzaListV2
		// Request Method:POST
		// Status Code:200 OK
		// Request Headersview source
		// Accept:*/*
		// Accept-Encoding:gzip,deflate,sdch
		// Accept-Language:zh-CN,zh;q=0.8,en;q=0.6,ja;q=0.4,zh-TW;q=0.2
		// Connection:keep-alive
		// Content-Length:127
		// Content-Type:application/x-www-form-urlencoded; charset=UTF-8
		// Cookie:_za=fd370992-b3c6-40c1-8878-fe6a16e1a9d3;
		// _xsrf=cef4e8af1bed27a4bf55bc9208519455;
		// tc=AQAAAMEmW164TAAA+stBMYtLPmY+q7Ig;
		// q_c1=c893fb9042e04fea9535bad53e1bcc01|1441596419000|1438925628000;
		// cap_id="OGQwZTBjMThjMmU3NDU4NmE2MTBmZTllMjcwNzZjYjE=|1441603641|ae140c8417ace5f4f2417cd6d1b87c75946b3d3e";
		// __utmt=1;
		// __utma=51854390.1029615844.1441599927.1441599927.1441600830.2;
		// __utmb=51854390.50.9.1441602466036; __utmc=51854390;
		// __utmz=51854390.1441600830.2.5.utmcsr=baidu|utmccn=(organic)|utmcmd=organic;
		// __utmv=51854390.000--|2=registration_date=20130923=1^3=entry_date=20150807=1;
		// n_c=1
		// Host:www.zhihu.com
		// Origin:http://www.zhihu.com
		// Referer:http://www.zhihu.com/topics
		// User-Agent:Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36
		// (KHTML, like Gecko) Chrome/30.0.1599.101 Safari/537.36
		// X-Requested-With:XMLHttpRequest
		// Form Dataview sourceview URL encoded
		// method:next
		// params:{"topic_id":99,"offset":0,"hash_id":""}
		// _xsrf:cef4e8af1bed27a4bf55bc9208519455

		PostBean postBean = new PostBean();
		postBean.topic_id = topic.topic_id;
		postBean.offset = 0;
		postBean.hash_id = "";
		
		Map<String, String> postParams = new HashMap<String, String>();
		postParams.put("method", "next");
		postParams.put("params", new Gson().toJson(postBean));
		postParams.put("_xsrf", "cef4e8af1bed27a4bf55bc9208519455");
		
		String response = HttpUtils.postString(
				"http://www.zhihu.com/node/TopicsPlazzaListV2", postParams, null);
		
		ResponseBean responseBean = new Gson().fromJson(response, ResponseBean.class);
		Document parse = Jsoup.parse(responseBean.msg.get(0));
		// <a target="_blank" href="/topic/19550517">
		Elements elements = parse.getElementsByAttributeValueStarting("href", "/topic");
		
		return hostUrl + elements.get(0).attr("href");
	}
	
	static class PostBean {
		String topic_id;
		int offset;
		String hash_id;
	}
	
	static class ResponseBean {
		String r;
		List<String> msg;
	}
}
