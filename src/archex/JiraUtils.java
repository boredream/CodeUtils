package archex;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import utils.FileUtils;
import utils.HttpUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JiraUtils {

    private static Map<String, String> headers = new HashMap<>();

    static {
        headers.put("Cookie", "gr_user_id=5fe47c2e-d1ad-4cbb-b04e-ff88567520d7; grwng_uid=dabd4bb7-1f02-4ff6-988b-902f488a128f; jira.editor.user.mode=wysiwyg; UM_distinctid=17a368eeb54305-0ace9dda7281a4-34657400-384000-17a368eeb55a4f; cookiesession1=67C4FDDEQ0IMWGH2C3KZRRQJD722ED6D; JSESSIONID=A24A32906929B8B39E2713BC2AB92592; seraph.rememberme.cookie=20639:807810271145ffea3969afb19d96cfe3ac0864ea; atlassian.xsrf.token=B6QL-CTZW-3V74-RQS9_932d6ba8a7328addbfc352928db9aece51c1a4a2_lin");
    }

    interface AListener<T> {
        void method(T t);
    }

    public static void main(String[] args) {


        try {
            addIssues();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static ArrayList<String> getSubIssues(String jiraId) throws Exception {
        ArrayList<String> names = new ArrayList<>();
        String url = "http://jira.shinho.net.cn/rest/api/2/issue/" + jiraId;
        String content = HttpUtils.getString(url, headers);

        JsonArray subTaskList = new JsonParser().parse(content).getAsJsonObject()
                .get("fields").getAsJsonObject()
                .get("subtasks").getAsJsonArray();
        for (int i = 0; i < subTaskList.size(); i++) {
            String summary = subTaskList.get(i).getAsJsonObject()
                    .get("fields").getAsJsonObject()
                    .get("summary").getAsString();
            names.add(summary);
        }
        return names;
    }

    private static void createIssue(String parent, String summary, String userId, String startDate, String endDate, float point) {
        String projectId = "10301"; // SFA1
        String issueType = "10003"; // 子任务

        JiraIssue issue = new JiraIssue();
        issue.setFields(new JiraIssue.Fields());
        issue.getFields().setParent(new JiraIssue.Parent(parent));
        issue.getFields().setProject(new JiraIssue.Project(projectId));
        issue.getFields().setSummary(summary);
        issue.getFields().setIssuetype(new JiraIssue.Issuetype(issueType));
        issue.getFields().setAssignee(new JiraIssue.Assignee(userId));
        issue.getFields().setReporter(new JiraIssue.Reporter("18010089"));
        issue.getFields().setPriority(new JiraIssue.Priority("3"));
        issue.getFields().setCustomfield_10006(point); // point
        // TODO 开始结束时间

        String url = "http://jira.shinho.net.cn/rest/api/2/issue";
        String response = null;
        try {
            response = HttpUtils.postJson(url, new Gson().toJson(issue), headers);
            System.out.println("success create = " + summary + " ... response = " + response);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error create = " + summary + " ... response = " + response);
        }
    }

    private static void addIssues() {
        HashMap<String, String> nameMap = new HashMap<>();
        nameMap.put("李春阳", "18010089");
        nameMap.put("胡翔", "18020013");
        nameMap.put("杨建", "18070144");
        nameMap.put("刘殷平", "18070142");
        nameMap.put("李永炳", "20030015");
        nameMap.put("曾维坚", "21030087");
        nameMap.put("张祺", "19040096");

        //  先获取所有表格内问题
        String data = FileUtils.readToString(new File("temp/jira/jira.txt"), "utf-8");
        HashMap<String, ArrayList<JiraTask>> jiraGroupMap = new HashMap<>();
        for (String listStr : data.split("SFA1-")) {
            listStr = listStr.trim();
            if (listStr.length() == 0) continue;

            String parent = "";
            for (String line : listStr.split("\n")) {
                line = line.trim();
                if (line.length() == 0) continue;

                for (String tab : line.split("\t")) {
                    tab = tab.trim();
                    if (tab.contains(" - ")) {
                        // 遇到新jira了，新建个数组
                        parent = tab;
                        jiraGroupMap.put(parent, new ArrayList<>());
                    } else {
                        ArrayList<JiraTask> taskList = jiraGroupMap.get(parent);
                        if (tab.startsWith("【")) {
                            // 任务
                            JiraTask task = new JiraTask();
                            task.name = tab;
                            taskList.add(task);
                        } else if (nameMap.containsKey(tab)) {
                            // 名字
                            taskList.get(taskList.size() - 1).user = tab;
                        } else {
                            // point
                            taskList.get(taskList.size() - 1).point = Float.parseFloat(tab);
                        }
                    }
                }
            }
        }

        // TODO 插入前查询当前sprint下jira去重
        // TODO 日期自动排序or手动
        for (Map.Entry<String, ArrayList<JiraTask>> entry : jiraGroupMap.entrySet()) {
            String jiraId = "SFA1-" + entry.getKey().split(" - ")[0].trim();

            // 先查询jira已有任务，防止重复添加
            ArrayList<String> subIssueNames = null;
            try {
                subIssueNames = getSubIssues(jiraId);
            } catch (Exception e) {
                System.out.println("get sub issues error = " + jiraId);
                continue;
            }

            // 子任务总分
            int totalPoint = 0;

            for (JiraTask jira : entry.getValue()) {
                if (subIssueNames.contains(jira.name)) {
                    System.out.println("already create = " + jira.name);
                    continue;
                }

                createIssue(jiraId, jira.name, nameMap.get(jira.user), "", "", jira.point);
                totalPoint += jira.point;

                // if '李春阳' == jira['user'] or '胡翔' == jira['user']:
                //    # 如果是安卓估时，新增对应的iOS开发
                //    create_issue(jira_id, jira['name'], name_map['曾维坚'], '', '', jira['point'])
                //    total_points += jira['point']
            }

            // 更新总分
            if (totalPoint > 0) {
                try {
                    editIssuePoint(jiraId, totalPoint);
                } catch (Exception e) {
                    System.out.println("error update total point = " + jiraId);
                }
            }
        }
    }

    private static void editIssuePoint(String jiraId, float point) throws Exception {
        String url = "http://jira.shinho.net.cn/rest/api/2/issue/" + jiraId;

        JiraIssue issue = new JiraIssue();
        issue.setFields(new JiraIssue.Fields());
        issue.getFields().setCustomfield_10006(point); // point

        String response = HttpUtils.putJson(url, new Gson().toJson(issue), headers);
        System.out.println("success update point = " + jiraId + " ... response = " + response);
    }

    //def update_issues():
    //    task_list = ['SFA1-5059', 'SFA1-5060', 'SFA1-5017']
    //
    //    for jira_id in task_list:
    //
    //        # 先查询故事
    //        url = 'http://jira.shinho.net.cn/rest/api/2/issue/%s' % jira_id
    //        request = urllib.request.Request(url, headers={
    //            'Cookie': cookie
    //        })
    //        content = urllib.request.urlopen(request).read().decode('utf-8')
    //        tasks = json.loads(content)['fields']['subtasks']
    //
    //        # 故事下所有子任务
    //        for task in tasks:
    //
    //            # 查询任务原有信息
    //            url = 'http://jira.shinho.net.cn/rest/api/2/issue/%s' % task['key']
    //            request = urllib.request.Request(url, headers={
    //                'Cookie': cookie
    //            })
    //            content = urllib.request.urlopen(request).read().decode('utf-8')
    //
    //            # 解析并生成新数据
    //            old_point = json.loads(content)['fields']['customfield_10006']
    //            new_point = float(old_point) * 8
    //
    //            # 提交更新数据
    //            issue = {
    //                "fields": {
    //                    "customfield_10006": new_point,  # story point 1.0
    //
    //                }
    //            }
    //            data_json = json.dumps(issue).encode(encoding='utf-8')
    //            post_req = urllib.request.Request(url=url,
    //                                              method='PUT',
    //                                              data=data_json,
    //                                              headers={
    //                                                  'content-type': 'application/json',
    //                                                  'Cookie': cookie
    //                                              })
    //            post_res_data = urllib.request.urlopen(post_req)
    //            content = post_res_data.read().decode('utf-8')
    //            print("success edit = " + task['key'] + " ... response = " + content)
    //
    //
    //add_issues()


    static class JiraTask {
        String user;
        String name;
        float point = 0;
    }
}
