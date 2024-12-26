package work;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utils.HttpUtils;
import utils.ShUser;

import java.io.FileInputStream;
import java.util.*;

public class WorkMain {

    static class TaskInfo {
        String story;
        String storyName;
        String taskName;
        String person;
        String personNumber;
        int sp;

        @Override
        public String toString() {
            return "TaskInfo{" +
                    "story='" + story + '\'' +
                    ", taskName='" + taskName + '\'' +
                    ", person='" + person + '\'' +
                    ", personNumber='" + personNumber + '\'' +
                    ", sp=" + sp +
                    '}';
        }
    }

    public static void main(String[] args) {
        if (1 == 1) {
            String data = "\"消息中心新增新鲜度预警消息类型\\tA2-4591\\tP0【AIO】预警消息通知 待办\\t皮嘉酉\\t\\t6\\t\\t\\t前端\\n\" +\n" +
                    "                    \"消息中心新增新鲜度预警消息类型\\tA2-4591\\tP0【AIO】预警消息通知 待办\\t胡翔\\t\\t6\\t\\t\\t前端\\n\" +\n" +
                    "                    \"新增新鲜度预警消息通知点击跳转\\tA2-4591\\tP0【AIO】预警消息通知 待办\\t皮嘉酉\\t\\t6\\t\\t\\t前端\\n\" +\n" +
                    "                    \"新增新鲜度预警消息通知点击跳转\\tA2-4591\\tP0【AIO】预警消息通知 待办\\t胡翔\\t\\t6\\t\\t\\t前端\\n\" +\n" +
                    "                    \"IPO1类型新鲜度通知特殊处理\\tA2-4591\\tP0【AIO】预警消息通知 待办\\t皮嘉酉\\t\\t4\\t\\t\\t前端\\n\" +\n" +
                    "                    \"IPO1类型新鲜度通知特殊处理\\tA2-4591\\tP0【AIO】预警消息通知 待办\\t胡翔\\t\\t4\\t\\t\\t前端\\n\" +\n" +
                    "                    \"首页新增新鲜度预警弹框\\tA2-4591\\tP0【AIO】预警消息通知 待办\\t皮嘉酉\\t\\t4\\t\\t\\t前端\\n\" +\n" +
                    "                    \"首页新增新鲜度预警弹框\\tA2-4591\\tP0【AIO】预警消息通知 待办\\t胡翔\\t\\t4\\t\\t\\t前端\\n\" +\n" +
                    "                    \"新鲜度预警已读回执\\tA2-4591\\tP0【AIO】预警消息通知 待办\\t皮嘉酉\\t\\t4\\t\\t\\t前端\\n\" +\n" +
                    "                    \"新鲜度预警已读回执\\tA2-4591\\tP0【AIO】预警消息通知 待办\\t胡翔\\t\\t4\\t\\t\\t前端\\n\" +\n" +
                    "                    \"首页新鲜度预警弹框聚合处理\\tA2-4591\\tP0【AIO】预警消息通知 待办\\t皮嘉酉\\t\\t4\\t\\t\\t前端\\n\" +\n" +
                    "                    \"首页新鲜度预警弹框聚合处理\\tA2-4591\\tP0【AIO】预警消息通知 待办\\t胡翔\\t\\t4\\t\\t\\t前端\\n\" +\n" +
                    "                    \"新鲜度预警推送整体联调\\tA2-4591\\tP0【AIO】预警消息通知 待办\\t皮嘉酉\\t\\t8\\t\\t\\t前端\\n\" +\n" +
                    "                    \"新鲜度预警推送整体联调\\tA2-4591\\tP0【AIO】预警消息通知 待办\\t胡翔\\t\\t8\\t\\t\\t前端\\n\" +\n" +
                    "                    \"新鲜度仪表盘一线业代H5和原生联调测试\\tA2-4591\\tP0【AIO】预警消息通知 待办\\t皮嘉酉\\t\\t8\\t\\t\\t前端\\n\" +\n" +
                    "                    \"新鲜度仪表盘一线业代H5和原生联调测试\\tA2-4591\\tP0【AIO】预警消息通知 待办\\t胡翔\\t\\t8\\t\\t\\t前端\\n\" +\n" +
                    "                    \"新鲜度仪表盘各级主管H5和原生联调测试\\tA2-4591\\tP0【AIO】预警消息通知 待办\\t皮嘉酉\\t\\t8\\t\\t\\t前端\\n\" +\n" +
                    "                    \"新鲜度仪表盘各级主管H5和原生联调测试\\tA2-4591\\tP0【AIO】预警消息通知 待办\\t胡翔\\t\\t8\\t\\t\\t前端\\n\" +\n" +
                    "                    \"店内工作模块新增点击校验处理\\tA2-4600\\tP0【SFA】App懒更新机制 待办\\t李春阳\\t\\t4\\t\\t\\t前端\\n\" +\n" +
                    "                    \"店内工作模块新增点击校验处理\\tA2-4600\\tP0【SFA】App懒更新机制 待办\\t皮嘉酉\\t\\t4\\t\\t\\t前端\\n\" +\n" +
                    "                    \"常规工作模块新增点击校验处理\\tA2-4600\\tP0【SFA】App懒更新机制 待办\\t李春阳\\t\\t4\\t\\t\\t前端\\n\" +\n" +
                    "                    \"常规工作模块新增点击校验处理\\tA2-4600\\tP0【SFA】App懒更新机制 待办\\t皮嘉酉\\t\\t4\\t\\t\\t前端\\n\" +\n" +
                    "                    \"服务端配置工作模块的最低版本属性\\tA2-4600\\tP0【SFA】App懒更新机制 待办\\t李春阳\\t\\t4\\t\\t\\t前端\\n\" +\n" +
                    "                    \"服务端配置工作模块的最低版本属性\\tA2-4600\\tP0【SFA】App懒更新机制 待办\\t皮嘉酉\\t\\t4\\t\\t\\t前端\\n\" +\n" +
                    "                    \"前端懒加载处理机制\\tA2-4600\\tP0【SFA】App懒更新机制 待办\\t李春阳\\t\\t16\\t\\t\\t前端\\n\" +\n" +
                    "                    \"前端懒加载处理机制\\tA2-4600\\tP0【SFA】App懒更新机制 待办\\t皮嘉酉\\t\\t16\\t\\t\\t前端\\n\" +\n" +
                    "                    \"多选平台覆盖城市输入组件\\tA2-4601\\tP0【SFA】SFA扫街模块改造 待办\\t李春阳\\t\\t16\\t\\t\\t前端\\n\" +\n" +
                    "                    \"O2O扫街详情表单页\\tA2-4601\\tP0【SFA】SFA扫街模块改造 待办\\t李春阳\\t\\t8\\t\\t\\t前端\\n\" +\n" +
                    "                    \"O2O扫街提交接口开发与联调\\tA2-4601\\tP0【SFA】SFA扫街模块改造 待办\\t李春阳\\t\\t4\\t\\t\\t前端\\n\" +\n" +
                    "                    \"餐饮渠道关系网选择组件\\tA2-4601\\tP0【SFA】SFA扫街模块改造 待办\\t李春阳\\t\\t8\\t\\t\\t前端\\n\" +\n" +
                    "                    \"关系网维护列表\\tA2-4601\\tP0【SFA】SFA扫街模块改造 待办\\t李春阳\\t\\t8\\t\\t\\t前端\\n\" +\n" +
                    "                    \"关系网人员信息详情\\tA2-4601\\tP0【SFA】SFA扫街模块改造 待办\\t李春阳\\t\\t4\\t\\t\\t前端\\n\" +\n" +
                    "                    \"关系网人员根据电话同步人员信息\\tA2-4601\\tP0【SFA】SFA扫街模块改造 待办\\t李春阳\\t\\t12\\t\\t\\t前端\\n\" +\n" +
                    "                    \"不同模块中关系网人员信息特殊处理\\tA2-4601\\tP0【SFA】SFA扫街模块改造 待办\\t李春阳\\t\\t8\\t\\t\\t前端\\n\" +\n" +
                    "                    \"传统渠道零售模式扫街\\tA2-4601\\tP0【SFA】SFA扫街模块改造 待办\\t李春阳\\t\\t8\\t\\t\\t前端\\n\" +\n" +
                    "                    \"传统渠道批发模式扫街\\tA2-4601\\tP0【SFA】SFA扫街模块改造 待办\\t李春阳\\t\\t8\\t\\t\\t前端\\n\" +\n" +
                    "                    \"经营方式选择组件\\tA2-4601\\tP0【SFA】SFA扫街模块改造 待办\\t李春阳\\t\\t8\\t\\t\\t前端\\n\" +\n" +
                    "                    \"锚点组件\\tA2-4601\\tP0【SFA】SFA扫街模块改造 待办\\t胡翔\\t\\t16\\t\\t\\t前端\\n\" +\n" +
                    "                    \"下游渠道数据录入组件\\tA2-4601\\tP0【SFA】SFA扫街模块改造 待办\\t胡翔\\t\\t12\\t\\t\\t前端\\n\" +\n" +
                    "                    \"仓储水平录入组件\\tA2-4601\\tP0【SFA】SFA扫街模块改造 待办\\t胡翔\\t\\t8\\t\\t\\t前端\\n\" +\n" +
                    "                    \"预设值多选组件\\tA2-4601\\tP0【SFA】SFA扫街模块改造 待办\\t胡翔\\t\\t6\\t\\t\\t前端\\n\" +\n" +
                    "                    \"经销商收集首页表单页\\tA2-4601\\tP0【SFA】SFA扫街模块改造 待办\\t胡翔\\t\\t4\\t\\t\\t前端\\n\" +\n" +
                    "                    \"经销商收集详情页表单页\\tA2-4601\\tP0【SFA】SFA扫街模块改造 待办\\t胡翔\\t\\t8\\t\\t\\t前端\\n\" +\n" +
                    "                    \"经销商收集接口开发与联调\\tA2-4601\\tP0【SFA】SFA扫街模块改造 待办\\t胡翔\\t\\t4\\t\\t\\t前端\\n\" +\n" +
                    "                    \"AI识别服务接入\\tA2-4629\\tP0【SFA】主数据调整 待办\\t皮嘉酉\\t\\t16\\t\\t\\t前端\\n\" +\n" +
                    "                    \"AI识别服务接入\\tA2-4629\\tP0【SFA】主数据调整 待办\\t胡翔\\t\\t16\\t\\t\\t前端\\n\" +\n" +
                    "                    \"AI图片录入组件\\tA2-4629\\tP0【SFA】主数据调整 待办\\t皮嘉酉\\t\\t8\\t\\t\\t前端\\n\" +\n" +
                    "                    \"AI图片录入组件\\tA2-4629\\tP0【SFA】主数据调整 待办\\t胡翔\\t\\t8\\t\\t\\t前端\\n\" +\n" +
                    "                    \"扫街、客户信息、审核页面的各种逻辑处理\\tA2-4629\\tP0【SFA】主数据调整 待办\\t皮嘉酉\\t\\t12\\t\\t\\t前端\\n\" +\n" +
                    "                    \"扫街、客户信息、审核页面的各种逻辑处理\\tA2-4629\\tP0【SFA】主数据调整 待办\\t胡翔\\t\\t12\\t\\t\\t前端\\n\" +\n" +
                    "                    \"配置表加载逻辑\\tA2-4629\\tP0【SFA】主数据调整 待办\\t皮嘉酉\\t\\t2\\t\\t\\t前端\\n\" +\n" +
                    "                    \"配置表加载逻辑\\tA2-4629\\tP0【SFA】主数据调整 待办\\t胡翔\\t\\t2\\t\\t\\t前端\\n\" +\n" +
                    "                    \"割箱陈列组件\\tA2-4629\\tP0【SFA】主数据调整 待办\\t皮嘉酉\\t\\t6\\t\\t\\t前端\\n\" +\n" +
                    "                    \"割箱陈列组件\\tA2-4629\\tP0【SFA】主数据调整 待办\\t胡翔\\t\\t6\\t\\t\\t前端\\n\" +\n" +
                    "                    \"割箱SKU搜索页面\\tA2-4629\\tP0【SFA】主数据调整 待办\\t皮嘉酉\\t\\t4\\t\\t\\t前端\\n\" +\n" +
                    "                    \"割箱SKU搜索页面\\tA2-4629\\tP0【SFA】主数据调整 待办\\t胡翔\\t\\t4\\t\\t\\t前端\\n\" +\n" +
                    "                    \"后台客户审核配置表加载逻辑\\tA2-4629\\tP0【SFA】主数据调整 待办\\t杨建\\t\\t2\\t\\t\\t前端\\n\" +\n" +
                    "                    \"后台割箱陈列显示组件\\tA2-4629\\tP0【SFA】主数据调整 待办\\t杨建\\t\\t6\\t\\t\\t前端\\n\" +\n" +
                    "                    \"后台主管客户审核，dadmin客户审核字段调整\\tA2-4629\\tP0【SFA】主数据调整 待办\\t杨建\\t\\t8\\t\\t\\t前端\\n\" +\n" +
                    "                    \"DSFA接入友盟+并且门店信息维护增加操作埋点\\tA2-4629\\tP0【SFA】主数据调整 待办\\t刁望庆\\t\\t16\\t\\t\\t前端\\n\" +\n" +
                    "                    \"鸿蒙小程序平台MPC项目初始化与基础结构搭建\\tA2-4602\\tP0【SFA】技术任务和其他优化 待办\\t胡翔\\t\\t6\\t\\t\\t前端\\n\" +\n" +
                    "                    \"鸿蒙小程序平台MPC实现小程序的加载与运行逻辑\\tA2-4602\\tP0【SFA】技术任务和其他优化 待办\\t胡翔\\t\\t12\\t\\t\\t前端\\n\" +\n" +
                    "                    \"鸿蒙小程序平台MPC实现页面跳转与参数传递功能\\tA2-4602\\tP0【SFA】技术任务和其他优化 待办\\t胡翔\\t\\t6\\t\\t\\t前端\\n\" +\n" +
                    "                    \"鸿蒙小程序平台MPC实现与原生交互的能力\\tA2-4602\\tP0【SFA】技术任务和其他优化 待办\\t胡翔\\t\\t12\\t\\t\\t前端\\n\" +\n" +
                    "                    \"鸿蒙小程序平台MPC实现小程序包管理与更新功能\\tA2-4602\\tP0【SFA】技术任务和其他优化 待办\\t胡翔\\t\\t12\\t\\t\\t前端\\n\" +\n" +
                    "                    \"鸿蒙小程序平台MPC实现数据跟踪与日志管理\\tA2-4602\\tP0【SFA】技术任务和其他优化 待办\\t胡翔\\t\\t12\\t\\t\\t前端\\n\" +\n" +
                    "                    \"经销商新鲜度仪表盘H5接入和联调测试\\tA2-4598\\tP0 【DSFA】经销商新鲜度仪表盘 待办\\t刁望庆\\t\\t8\\t\\t\\t前端\\n\" +\n" +
                    "                    \"门店新鲜度仪表盘H5接入和联调测试\\tA2-4599\\tP0 【DSFA】门店新鲜度仪表盘 待办\\t刁望庆\\t\\t8\\t\\t\\t前端\\n\" +\n" +
                    "                    \"门店拜访页面接SFA的POSM小程序\\tA2-4627\\tP0【DSFA】12.25上线需求集合 待办\\t刁望庆\\t\\t2\\t\\t\\t前端\\n\" +\n" +
                    "                    \"门店拜访页面新增驳回字段的提醒，未修改前不允许拜访\\tA2-4627\\tP0【DSFA】12.25上线需求集合 待办\\t刁望庆\\t\\t4\\t\\t\\t前端\\n\" +\n" +
                    "                    \"门店扫街中货架端架地堆割箱相机支持连拍后批量AI识别\\tA2-4627\\tP0【DSFA】12.25上线需求集合 待办\\t刁望庆\\t\\t6\\t\\t\\t前端\\n\" +\n" +
                    "                    \"配送的一物一码支持连续扫码\\tA2-4627\\tP0【DSFA】12.25上线需求集合 待办\\t刁望庆\\t\\t4\\t\\t\\t前端\\n\" +\n" +
                    "                    \"割箱活动中，执行阶段补充文案【按公司要求，需将“箱体二维码”拍照上传】\\tA2-4627\\tP0【DSFA】12.25上线需求集合 待办\\t刁望庆\\t\\t4\\t\\t\\t前端\\n\" +\n" +
                    "                    \"门店信息提交改为离线\\tA2-4627\\tP0【DSFA】12.25上线需求集合 待办\\t刁望庆\\t\\t4\\t\\t\\t前端\\n\" +\n" +
                    "                    \"增加”端架数量“、”割箱数量“、”地堆数量“三个字段\\tA2-4628\\tP0【DSFA】门店信息新增”端架数量“、”割箱数量“、”地堆数量“字段，并检查 待办\\t刁望庆\\t\\t6\\t\\t\\t前端\\n\" +\n" +
                    "                    \"”端架拍照“、”割箱拍照“、”地堆拍照“根据各自位置的数量判断是否必填\\tA2-4628\\tP0【DSFA】门店信息新增”端架数量“、”割箱数量“、”地堆数量“字段，并检查 待办\\t刁望庆\\t\\t6\\t\\t\\t前端\\n\" +\n" +
                    "                    \"必填时”端架数量“、”割箱数量“、”地堆数量“之和必须大于等于1，否则不允许提交\\tA2-4628\\tP0【DSFA】门店信息新增”端架数量“、”割箱数量“、”地堆数量“字段，并检查 待办\\t刁望庆\\t\\t4\\t\\t\\t前端\\n\" +\n" +
                    "                    \"新鲜度采集手动输入日期增加合规验证\\tA2-4628\\tP0【DSFA】门店信息新增”端架数量“、”割箱数量“、”地堆数量“字段，并检查 待办\\t刁望庆\\t\\t4\\t\\t\\t前端\\n\"";

                    

            return;
        }

        // 从网站获取HTML内容
        String html = getHtmlFromWeb();
        if (html == null) {
            System.out.println("获取网页内容失败");
            return;
        }

        Document doc = Jsoup.parse(html);

        List<TaskInfo> taskList = new ArrayList<>();
        String story = null;
        String storyName = null;
        String storyUser = null;

        // 获取表格内容
        Elements rows = doc.select("table.confluenceTable tr");
        for (Element row : rows) {
            // 跳过表头
            if (row.select("th").size() > 0) {
                continue;
            }

            Elements cells = row.select("td");
            if (cells.isEmpty()) {
                continue;
            }

            Element firstCell = cells.first();
            String firstValue = firstCell.text().trim();

            // 判断是否是故事行(灰色背景且包含JIRA编号)
            if (firstCell.hasClass("highlight-grey") && firstValue.contains("A2-")) {
                // 解析故事编号和名称
                story = firstValue.split(" - ")[0].trim();
                storyName = firstValue.split(" - ").length > 1 ? firstValue.split(" - ")[1].trim() : "";

                // 获取故事行的负责人
                Element lastCell = cells.last();
                if (lastCell != null && !lastCell.text().trim().isEmpty()) {
                    storyUser = lastCell.text().trim();
                }
                continue;
            }

            // 跳过空行
            if (firstValue.isEmpty()) {
                continue;
            }

            // 解析任务行
            String taskName = firstValue;

            // 获取SP值(倒数第二列)
            String spStr = cells.size() >= 2 ? cells.get(cells.size() - 2).text().trim() : "0";
            int sp = 0;
            try {
                sp = Integer.parseInt(spStr);
            } catch (NumberFormatException e) {
                // SP不是数字则跳过
                continue;
            }

            // 获取负责人(最后一列)
            String taskUser = cells.last().text().trim();
            String personStr = taskUser.isEmpty() ? storyUser : taskUser;

            // 如果没有指定负责人则跳过
            if (personStr == null || personStr.isEmpty()) {
                continue;
            }

            // 处理多个负责人的情况
            for (String person : personStr.split("[ ,]")) {
                if (person.trim().isEmpty()) {
                    continue;
                }
                TaskInfo taskInfo = new TaskInfo();
                taskInfo.story = story;
                taskInfo.storyName = storyName;
                taskInfo.taskName = taskName;
                taskInfo.person = person;
                taskInfo.sp = sp;
                taskInfo.personNumber = ShUser.getUserNumber(person);
                taskList.add(taskInfo);
            }
        }

        // 按人员统计数据
        HashMap<String, HashSet<String>> userStoryMap = new HashMap<>();
        for (TaskInfo taskInfo : taskList) {
            HashSet<String> stories = userStoryMap.getOrDefault(taskInfo.person, new HashSet<>());
            stories.add(taskInfo.story);
            userStoryMap.put(taskInfo.person, stories);
        }

        HashMap<String, Integer> userTotalSpMap = new HashMap<>();
        for (TaskInfo taskInfo : taskList) {
            userTotalSpMap.put(taskInfo.person, userTotalSpMap.getOrDefault(taskInfo.person, 0) + taskInfo.sp);
            System.out.println(taskInfo.taskName +
                    "\t" + taskInfo.story +
                    "\t" + taskInfo.storyName +
                    "\t" + taskInfo.person +
                    "\t" + taskInfo.personNumber +
                    "\t" + taskInfo.sp +
                    "\t" + // 开始时间
                    "\t" + // 结束时间
                    "\t" + "前端");
        }

        // 输出统计结果
        userTotalSpMap.forEach((name, sp) -> {
            System.out.println("姓名：" + name);
            System.out.println("SP：" + sp);
            System.out.println("故事：" + userStoryMap.get(name).size());
            System.out.println();
        });
    }

    private static String getHtmlFromWeb() {
        try {
            System.out.println("开始获取网页内容...");

            // 从配置文件读取Cookie
            Properties props = new Properties();
            String configPath = "src/work/config.properties";
            System.out.println("正在读取配置文件: " + configPath);

            props.load(new FileInputStream(configPath));
            String cookie = props.getProperty("confluence.cookie");
            System.out.println("Cookie长度: " + (cookie != null ? cookie.length() : 0));

            Map<String, String> headers = new HashMap<>();
            headers.put("Cookie", cookie);
            headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
            headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            headers.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");

            String url = "http://confluence.shinho.net.cn/pages/viewpage.action?pageId=117943117";
            System.out.println("正在请求URL: " + url);

            // 设置超时时间
            System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
            System.setProperty("sun.net.client.defaultReadTimeout", "10000");

            String result = HttpUtils.getString(url, headers);
            System.out.println("请求完成，返回内容长度: " + (result != null ? result.length() : 0));

            return result;
        } catch (Exception e) {
            System.out.println("获取网页内容出错: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

}
