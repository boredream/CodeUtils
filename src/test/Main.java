package test;

import com.google.gson.*;
import utils.FileUtils;
import utils.HttpUtils;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    static class MethodInfo {
        String method;
        String name;
    }

    static class Customer {
        public String type;
        public Aio customer_aio;
        public List<Address> customer_address;
        public List<CirCumBusiness> customer_circum_business_states;
    }

    static class Aio {
        public String locate_at;
    }

    static class Address {
        public String detailed_address;
    }

    static class CirCumBusiness {
        public SfaBaseCompartment sfaBaseCompartmentDto;
    }

    static class SfaBaseCompartment {
        public String name;
    }

    public static void main(String[] args) throws Exception {
        ArrayList<String> lines = FileUtils.readToStringLines(new File("temp/query_result.csv"));
        ArrayList<String> errorList = new ArrayList<>();
        StringBuilder errorCodes = new StringBuilder();
        for (String line : lines) {
            int index = line.indexOf(",");
            String json = line.substring(index + 2, line.length() - 1);
            json = json.replace("\\\"", "\"");

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(json);
            JsonObject jo = element.getAsJsonObject();

            JsonObject joR = jo.getAsJsonObject("dto").getAsJsonObject("customer_r");
            int set_occupancy = joR.get("set_occupancy").getAsInt();
            int consumption = joR.get("consumption").getAsInt();
            if (set_occupancy > 100 && consumption > 1000) {
                errorList.add(line);

//                "key": "customer_r#turnover",
//                        "key": "customer_r#consumption",
//                        "key": "customer_r#spices_month",
//                        "key": "customer_r#set_occupancy",
//                        "key": "customer_r#hotel_area",

                String code = jo.getAsJsonObject("dto").get("code").getAsString();
                String name = jo.getAsJsonObject("dto").get("name").getAsString();
                System.out.println(code + "," + name + " :" +
                        " 上座率=" + set_occupancy +
                        " 人均消费=" + joR.get("consumption") +
                        " 日均餐饮营业额=" + joR.get("turnover") +
                        " 调味品月开销金额=" + joR.get("spices_month") +
                        " 酒店总面积=" + joR.get("hotel_area"));

                errorCodes.append(",").append(code);
            }
        }
        System.out.println(errorList.size());
        System.out.println(errorCodes.substring(1));

//        StringBuilder sb = new StringBuilder();
//        for (String s : errorList) {
//            sb.append(s).append("\n");
//        }
//        File file = new File("temp/query_result_error.csv");
//        file.createNewFile();
//        FileUtils.writeString2File(sb.toString(), file);
    }

    /**
     * 从bean中解析config对应key的字段对应的值，可能是多个
     */
    private static String parseJsonValueFromBean(String key, JsonObject joDto) {
        String data = null;
        String[] filedList = key.split("\\.");
        for (int i = 0; i < filedList.length; i++) {
            String field = filedList[i];
            if (field.endsWith("/")) {
                // 数组直接判断element类型，不使用 /
                field = field.substring(0, field.length() - 1);
            }
            JsonElement element = joDto.get(field);

            // 直接是null或者主类型数据，返回
            if (element.isJsonNull()) {
                break;
            }

            if (element.isJsonPrimitive()) {
                data = element.getAsString();
                break;
            }

            if (element.isJsonArray()) {
                // 直接是空数组，或主数据类型数组，返回
                JsonArray array = element.getAsJsonArray();
                if (array.size() == 0) {
                    break;
                }

                JsonElement arrayJe = array.get(0);
                if (arrayJe.isJsonNull()) {
                    break;
                }

                if (arrayJe.isJsonPrimitive()) {
                    data = arrayJe.getAsString();
                    break;
                }

                // 数组对象类型，继续遍历下一个字段
                joDto = array.get(0).getAsJsonObject();
            } else {
                // 对象类型，继续遍历下一个字段
                joDto = element.getAsJsonObject();
            }
        }
        return data;
    }

    private static void method() {
        String str = "稽查首页 500008 sfa:global:check:index 500000\n" +
                "稽查日志 200044 sfa:global:check:checklog 200000\n" +
                "稽查报告 200045 sfa:global:check:checkreport 200000\n" +
                "检查计划管理 200046 sfa:global:check:futurework 200000\n" +
                "稽查非访店打卡签到签出 200047 sfa:global:check:novisitkqsio 200007\n" +
                "稽查签到签出 300214 sfa:inner:check:signinout 300000\n" +
                "货架检查 300215 sfa:inner:check:hj 300000\n" +
                "本品检查 300216 sfa:inner:check:shsku 300000\n" +
                "活动核销检查 300217 sfa:inner:check:fm 300000\n" +
                "检查拜访统计 600003 sfa:usercenter:statistics:check:visit 600000";
        for (String s : str.split("\n")) {
            String name = s.split(" ")[0];
            String key = s.split(" ")[2];
            String[] pathList = key.split(":");
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < pathList.length; i++) {
                sb.append(pathList[i]);
                if (i < pathList.length - 1) {
                    sb.append("_");
                }
            }
            String field = sb.toString().toUpperCase();
            System.out.println("/**");
            System.out.println(" * " + name);
            System.out.println(" **/");
            System.out.printf("public static final String %s = \"%s\";%n", field, key);
            System.out.println();
        }
    }

    private static void permissionMap() {
        String workIds =
                "    public static final String WORK_OTHERS = \"0\";\n" +
                        "    public static final String WORK_HOME = \"1\";\n" +
                        "    public static final String WORK_VISIT_CHECK = \"2\";\n" +
                        "    public static final String WORK_DISPLAY_PHOTO = \"4\";\n" +
                        "    public static final String WORK_OWN_GOODS = \"5\";\n" +
                        "    public static final String WORK_CHECK_STOCK = \"6\";\n" +
                        "    public static final String WORK_CHECK_STOCK_GMT = \"42\";\n" +
                        "    public static final String WORK_NEW_SALE = \"7\";\n" +
                        "    public static final String WORK_OWN_ORDER = \"9\";\n" +
                        "    public static final String WORK_COMPET_GOODS_GMT = \"10\";\n" +
                        "    public static final String WORK_COMPET_GOODS_R = \"11\";\n" +
                        "    public static final String WORK_FOOD_GOODS_CHECK = \"12\";\n" +
                        "    public static final String WORK_ACT_CHECK_FM = \"13\";\n" +
                        "    public static final String WORK_ACt_CHECK_FM_NEW = \"86\";\n" +
                        "    public static final String WORK_SHINHO_ACT = \"14\";\n" +
                        "    public static final String WORK_CHECKOUT_GMT = \"15\";\n" +
                        "    public static final String WORK_CHECKOUT_R = \"16\";\n" +
                        "    public static final String WORK_SEARCH_STREET = \"17\";\n" +
                        "    public static final String WORK_MT_COMMENT = \"19\";\n" +
                        "    public static final String WORK_DISH_COLLECT = \"20\";\n" +
                        "    public static final String WORK_CUSTOM_LIST = \"21\";\n" +
                        "    public static final String WORK_WORK_RECORD = \"22\";\n" +
                        "    public static final String WORK_REMOTE_CHECK = \"23\";\n" +
                        "    public static final String WORK_SPECIAL_VISIT = \"24\";\n" +
                        "    public static final String WORK_DEALER_CHECK = \"78\";\n" +
                        "    public static final String WORK_SALES_REPORT = \"26\";\n" +
                        "    public static final String WORK_COOP_VISIT = \"27\";\n" +
                        "    public static final String WORK_COOP_VISIT_R = \"41\";\n" +
                        "    public static final String WORK_MT_FEEDBACK = \"29\";\n" +
                        "    public static final String WORK_MT_HOME = \"30\";\n" +
                        "    public static final String WORK_PERFORMANCE = \"33\";\n" +
                        "    public static final String WORK_ACT_CHECK_CE = \"34\";\n" +
                        "    public static final String WORK_MUST_WIN = \"35\";\n" +
                        "    public static final String WORK_DEALER_MEETING = \"36\";\n" +
                        "    public static final String WORK_SALES_VOLUME_R = \"38\";\n" +
                        "    public static final String WORK_VISIT_PLAN_MANAGER = \"40\";\n" +
                        "    public static final String WORK_REVIEW_VISIT = \"43\";\n" +
                        "    public static final String WORK_VIVID_CHECK = \"44\";\n" +
                        "    public static final String WORK_OWN_GOODS_DIS = \"45\";\n" +
                        "    public static final String WORK_COMPET_GOODS_DIS = \"46\";\n" +
                        "    public static final String WORK_OWN_ACT_DIS = \"47\";\n" +
                        "    public static final String WORK_COMPET_ACT_DIS = \"48\";\n" +
                        "    public static final String WORK_AUDIT = \"73\";\n" +
                        "    public static final String WORK_VISIT_FOLLOW = \"80\";\n" +
                        "    public static final String WORK_MT_INSPIRE = \"87\";\n" +
                        "    public static final String WORK_CAR_HOME = \"49\";\n" +
                        "    public static final String WORK_CAR_CHECK = \"51\";\n" +
                        "    public static final String WORK_CAR_EVENT_EXPECT = \"53\";\n" +
                        "    public static final String WORK_CAR_EVENT_PREPARE = \"54\";\n" +
                        "    public static final String WORK_CAR_CONSUMER_INTERACT = \"55\";\n" +
                        "    public static final String WORK_CAR_DISHES = \"56\";\n" +
                        "    public static final String WORK_CAR_SALES_LIST = \"57\";\n" +
                        "    public static final String WORK_CAR_OWN_GOODS = \"58\";\n" +
                        "    public static final String WORK_CAR_GIFT_INFOS = \"59\";\n" +
                        "    public static final String WORK_CAR_CONSUMER_INFO = \"60\";\n" +
                        "    public static final String WORK_CAR_EXECUTE_EVALUATION = \"61\";\n" +
                        "    public static final String WORK_CAR_TOUR_BUSINESS = \"62\";\n" +
                        "    public static final String WORK_CAR_CHECK_HOME = \"49\";\n" +
                        "    public static final String WORK_CAR_WORK_HOME = \"50\";\n" +
                        "    public static final String WORK_CAR_ACT_LIST = \"52\";\n" +
                        "    public static final String WORK_CAR_FUTURE_WORK = \"66\";\n" +
                        "    public static final String WORK_CAR_OVERTIME = \"72\";\n" +
                        "    public static final String WORK_CAR_WORK_RECORD = \"64\";\n" +
                        "    public static final String WORK_CAR_CUSTOMER_LIST = \"63\";\n" +
                        "    public static final String WORK_FOOD_BUSINESS_OPPORTUNITY = \"83\";\n" +
                        "    public static final String WORK_CAR_WORK = \"67\";\n" +
                        "    public static final String WORK_CAR_GAS_COST = \"69\";\n" +
                        "    public static final String WORK_CAR_MONEY_COST = \"70\";\n" +
                        "    public static final String WORK_CAR_STAY_REPORT = \"71\";\n" +
                        "    public static final String WORK_CAR_MAINTAIN = \"74\";\n" +
                        "    public static final String WORK_CAR_MAINTAIN_COST = \"75\";\n" +
                        "    public static final String WORK_ORGANIZATION_STAFF = \"76\";\n" +
                        "    public static final String WORK_SALE_VOLUME_LC = \"77\";\n" +
                        "    public static final String WORK_DISH_FEED = \"79\";\n" +
                        "    public static final String WORK_CUSTOM_MODULE = \"2345\"; //自定义模块，Android临时定的ID，没有实际意义\n" +
                        "    public static final String WORK_SGB_HOME = \"81\";\n" +
                        "    public static final String WORK_SGB_REMOTE_CHECK = \"82\";\n" +
                        "    public static final String WORK_SGB_STOCK = \"84\";\n" +
                        "    public static final String WORK_SGB_COMPET_GOODS = \"85\";\n" +
                        "    public static final String WORK_HHH_OWN_ORDER = \"88\";";

        Map<String, String> nameIdMap = new HashMap<>();
        for (String s : workIds.split("\n")) {
            String name = s.split(" ")[8];
            String id = s.split("\"")[1];
            nameIdMap.put(name, id);
        }

        String old =
                "            case WORK_MT_INSPIRE:\n" +
                        "                item.setIcon(R.drawable.work_mt_inspire);\n" +
                        "                item.setName(\"激励\");\n" +
                        "                item.setShowFirstTag(true);\n" +
                        "                item.setActClass(ActivityKeeper.RegWork.WorkRecordActivity);\n" +
                        "                break;\n" +
                        "            case WORK_MUST_WIN:\n" +
                        "                item.setIcon(R.drawable.work_must_win);\n" +
                        "                item.setName(\"Must-Win\");\n" +
                        "                item.setActClass(ActivityKeeper.RegWork.MustWinMainActivity);\n" +
                        "                break;\n" +
                        "            case WORK_SEARCH_STREET:\n" +
                        "                item.setIcon(R.drawable.work_search_street);\n" +
                        "                item.setName(\"扫街\");\n" +
                        "                item.setActClass(ActivityKeeper.RegWork.SearchStreetActivity);\n" +
                        "                break;\n" +
                        "            case WORK_REMOTE_CHECK:\n" +
                        "                item.setIcon(R.drawable.work_remote_checkin);\n" +
                        "                item.setName(\"非访店打卡\");\n" +
                        "                item.setActClass(ActivityKeeper.RegWork.NoVisitCheckActivity);\n" +
                        "                break;\n" +
                        "            case WORK_SGB_REMOTE_CHECK:\n" +
                        "                item.setIcon(R.drawable.work_remote_checkin);\n" +
                        "                item.setName(\"非访店打卡\");\n" +
                        "                item.setActClass(ActivityKeeper.RegWork.SgbNoVisitCheckActivity);\n" +
                        "                break;\n" +
                        "            case WORK_DEALER_CHECK:\n" +
                        "                item.setIcon(R.drawable.work_remote_checkin);\n" +
                        "                item.setName(\"经销商考勤\");\n" +
                        "                item.setActClass(ActivityKeeper.RegWork.DealerCheckActivity);\n" +
                        "                break;\n" +
                        "            case WORK_OWN_ORDER:\n" +
                        "                item.setIcon(R.drawable.work_own_order);\n" +
                        "                item.setName(\"本品订单\");\n" +
                        "                item.setActClass(ActivityKeeper.RegWork.OwnOrderRegActivity);\n" +
                        "                break;\n" +
                        "            case WORK_OWN_GOODS:\n" +
                        "                item.setIcon(R.drawable.visit_own_goods_info);\n" +
                        "                item.setName(\"本品信息\");\n" +
                        "                item.setActClass(ActivityKeeper.RegWork.OwnGoodsListRegActivity);\n" +
                        "                break;\n" +
                        "            case WORK_CHECK_STOCK_GMT:\n" +
                        "                item.setIcon(R.drawable.icon_store_check);\n" +
                        "                item.setName(\"库存信息\");\n" +
                        "                item.setActClass(ActivityKeeper.RegWork.StockCheckGMTRegActivity);\n" +
                        "                break;\n" +
                        "            case WORK_SALES_REPORT:\n" +
                        "                item.setIcon(R.drawable.visit_sales_report_reg);\n" +
                        "                item.setName(\"销量提报\");\n" +
                        "                item.setActClass(ActivityKeeper.RegWork.SalesVolumeActivity);\n" +
                        "                break;\n" +
                        "            case WORK_ACT_CHECK_FM:\n" +
                        "                item.setIcon(R.drawable.visit_act_check_fm_reg);\n" +
                        "                item.setName(\"活动核销\");\n" +
                        "                item.setActClass(ActivityKeeper.RegWork.ActCheckFmRegActivity);\n" +
                        "                break;\n" +
                        "            case WORK_ACt_CHECK_FM_NEW:\n" +
                        "                item.setIcon(R.drawable.visit_act_check_fm_reg);\n" +
                        "                item.setName(\"新活动核销\");\n" +
                        "                item.setActClass(ActivityKeeper.RegWork.ActCheckFmNewRegActivity);\n" +
                        "                break;\n" +
                        "            case WORK_SHINHO_ACT:\n" +
                        "                item.setIcon(R.drawable.visit_shinho_act_reg);\n" +
                        "                item.setName(\"欣和活动跟进\");\n" +
                        "                item.setActClass(ActivityKeeper.RegWork.ActivityFollowActivity);\n" +
                        "                break;\n" +
                        "            case WORK_SPECIAL_VISIT:\n" +
                        "                item.setIcon(R.drawable.work_special_visit);\n" +
                        "                item.setName(\"特殊拜访活动\");\n" +
                        "                item.setActClass(ActivityKeeper.RegWork.ActivitySpecialListActivity);\n" +
                        "                break;\n" +
                        "            case WORK_VISIT_PLAN_MANAGER:\n" +
                        "                item.setIcon(R.drawable.work_visit_plan_manager);\n" +
                        "                item.setName(\"拜访计划管理\");\n" +
                        "                item.setActClass(ActivityKeeper.RegWork.VisitPlanManagerActivity);\n" +
                        "                break;\n" +
                        "            case WORK_CUSTOM_LIST:\n" +
                        "                item.setIcon(R.drawable.work_custom_list);\n" +
                        "                item.setName(\"客户列表\");\n" +
                        "                item.setActClass(ActivityKeeper.RegWork.CustomerActivity);\n" +
                        "                break;\n" +
                        "            case WORK_ORGANIZATION_STAFF:\n" +
                        "                item.setIcon(R.drawable.work_org_staff);\n" +
                        "                item.setName(\"组织与人员\");\n" +
                        "                item.setActClass(ActivityKeeper.RegWork.OrganizationStaffActivity);\n" +
                        "                break;\n" +
                        "            case WORK_WORK_RECORD:\n" +
                        "                item.setIcon(R.drawable.work_record);\n" +
                        "                item.setName(\"工作记录\");\n" +
                        "                item.setActClass(ActivityKeeper.RegWork.WorkRecordActivity);\n" +
                        "                break;\n" +
                        "            case WORK_PERFORMANCE:\n" +
                        "                item.setIcon(R.drawable.work_performance);\n" +
                        "                item.setName(\"绩效呈现\");\n" +
                        "                item.setActClass(ActivityKeeper.RegWork.PerformanceTabActivity);\n" +
                        "                break;\n" +
                        "            case WORK_COOP_VISIT_R:\n" +
                        "                item.setIcon(R.drawable.visit_coop_visit);\n" +
                        "                item.setName(\"随访辅导\");\n" +
                        "                item.setActClass(ActivityKeeper.RegWork.CoopVisitRActivity);\n" +
                        "                break;\n" +
                        "            case WORK_AUDIT:\n" +
                        "                item.setIcon(R.drawable.visit_audit);\n" +
                        "                item.setName(\"工作审核\");\n" +
                        "                item.setActClass(ActivityKeeper.RegWork.AuditActivity);\n" +
                        "                break;\n" +
                        "            case WORK_VISIT_FOLLOW:\n" +
                        "                item.setIcon(R.drawable.work_visit_follow);\n" +
                        "                item.setName(\"协访跟进\");\n" +
                        "                item.setActClass(ActivityKeeper.RegWork.VisitFollowTabActivity);\n" +
                        "                break;\n" +
                        "            case WORK_CAR_FUTURE_WORK:\n" +
                        "                item.setIcon(R.drawable.work_visit_plan_manager);\n" +
                        "                item.setName(\"未来活动计划\");\n" +
                        "                item.setActClass(ActivityKeeper.CarWork.CarWorkRecordActivity);\n" +
                        "                break;\n" +
                        "            case WORK_CAR_CHECK:\n" +
                        "                item.setIcon(R.drawable.work_remote_checkin);\n" +
                        "                item.setName(\"签到签出\");\n" +
                        "                item.setActClass(ActivityKeeper.CarWork.CarWorkRecordActivity);\n" +
                        "                break;\n" +
                        "            case WORK_CAR_OVERTIME:\n" +
                        "                item.setIcon(R.drawable.work_car_overtime);\n" +
                        "                item.setName(\"加班提报\");\n" +
                        "                item.setActClass(ActivityKeeper.CarWork.CarOvertimeActivity);\n" +
                        "                break;\n" +
                        "            case WORK_CAR_TOUR_BUSINESS:\n" +
                        "                item.setIcon(R.drawable.work_must_win);\n" +
                        "                item.setName(\"巡场业务\");\n" +
                        "                item.setActClass(ActivityKeeper.CarWork.CarTourBusinessActivity);\n" +
                        "                break;\n" +
                        "            case WORK_CAR_CUSTOMER_LIST:\n" +
                        "                item.setIcon(R.drawable.work_custom_list);\n" +
                        "                item.setName(\"客户列表\");\n" +
                        "                item.setActClass(ActivityKeeper.CarWork.CarCustomerListActivity);\n" +
                        "                break;\n" +
                        "            case WORK_CAR_WORK_RECORD:\n" +
                        "                item.setIcon(R.drawable.work_record);\n" +
                        "                item.setName(\"工作记录\");\n" +
                        "                item.setActClass(ActivityKeeper.CarWork.CarWorkRecordActivity);\n";

        Map<String, Map<String, String>> idInfoMap = new HashMap<>();
        for (String s : old.split("break;")) {
            String caseStr = s.trim();
            String[] lines = caseStr.split("\n");
            String name = lines[0].replace("case ", "").replace(":", "").trim();
            String id = nameIdMap.get(name);
            String icon = lines[1].replace("item.setIcon(", "").replace(");", "").trim();
            String displayName = lines[2].replace("item.setName(\"", "").replace("\");", "").trim();
            String actClass = lines[3].replace("item.setActClass(", "").replace(");", "").trim();

            Map<String, String> infoMap = new HashMap<>();
            infoMap.put("icon", icon);
            infoMap.put("displayName", displayName);
            infoMap.put("actClass", actClass);
            idInfoMap.put(id, infoMap);
        }

        String str = "11\t11竞品信息（R）\t200001\tsfa:global:r:competition\t200000\n" +
                "13\t13活动核销（通用）\t200002\tsfa:global:common:fm\t200000\n" +
                "14\t14欣和活动跟进（R）\t200003\tsfa:global:r:activityfollowup\t200000\n" +
                "17\t17扫街（通用）\t200004\tsfa:global:common:findstore\t200000\n" +
                "21\t21客户信息列表（通用）\t200005\tsfa:global:common:storeinfo\t200000\n" +
                "22\t22工作记录（通用）\t200006\tsfa:global:common:workrecord\t200000\n" +
                "23\t23非访店打卡（通用）\t200007\tsfa:global:common:novisitkq\t200000\n" +
                "24\t24特殊拜访活动（R）\t200008\tsfa:global:common:spevisit\t200000\n" +
                "26\t26销量提报（GT/MT）\t200009\tsfa:global:gtmt:salequantity\t200000\n" +
                "33\t33绩效考核\t200010\tsfa:global:common:performance\t200000\n" +
                "36\t36消息中心\t200011\tsfa:global:common:msg\t200000\n" +
                "40\t40未来拜访计划\t200012\tsfa:global:common:futurework\t200000\n" +
                "41\t41随访辅导 (R)\t200013\tsfa:global:r:visitcoach\t200000\n" +
                "42\t42库存盘点 (GT/MT)\t200014\tsfa:global:gtmt:stockinventory\t200000\n" +
                "51\t签到签出(C)\t200015\tsfa:global:c:qdqc\t200000\n" +
                "53\t活动预期(C)\t200017\tsfa:global:c:activityexpect\t200000\n" +
                "54\t活动准备(C)\t200018\tsfa:global:c:activityready\t200000\n" +
                "55\t消费者互动(C)\t200019\tsfa:global:c:interaction\t200000\n" +
                "56\t菜品信息(C)\t200020\tsfa:global:c:dishinfo\t200000\n" +
                "57\t销售清单(C)\t200021\tsfa:global:c:saleorder\t200000\n" +
                "58\t本品信息(C)\t200022\tsfa:global:c:shskuinfo\t200000\n" +
                "59\t赠品信息(C)\t200023\tsfa:global:c:freeskuinfo\t200000\n" +
                "60\t消费者信息(C)\t200024\tsfa:global:c:consumerinfo\t200000\n" +
                "61\t执行评价表(C)\t200025\tsfa:global:c:exelist\t200000\n" +
                "62\t巡场业务(C)\t200026\tsfa:global:c:aroundbiz\t200000\n" +
                "63\t客户列表(C)\t200027\tsfa:global:c:storeinfo\t200000\n" +
                "64\t工作记录(C)\t200028\tsfa:global:c:workrecord\t200000\n" +
                "65\t个人中心(C)\t200029\tsfa:global:c:usercenter\t200000\n" +
                "66\t未来活动计划(C)\t200030\tsfa:global:c:futurework\t200000\n" +
                "72\t加班提报(C)\t200031\tsfa:global:c:overtime\t200000\n" +
                "73\t工作审核\t200032\tsfa:global:common:audit\t200000\n" +
                "78\t经销商考勤\t200033\tsfa:global:common:jxsqk\t200000\n" +
                "76\t临促管理\t200034\tsfa:global:common:lc\t200000\n" +
                "80\t随访跟进\t200035\tsfa:global:common:visitfollowup\t200000\n" +
                "82\tSGB非访店打卡\t200036\tsfa:global:sgb:novisitkq\t200000\n" +
                "86\tCRM活动核销\t200037\tsfa:global:common:crmfm\t200000\n" +
                "87\t美顾绩效\t200038\tsfa:global:common:mth5\t200000\n" +
                "30\t30美顾定制首页（MT）\t500001\tsfa:global:mt:index\t500000\n" +
                "49\t厨演车推广员首页(C)\t500002\tsfa:global:c:index\t500000\n" +
                "50\t驾驶员首页(C)\t500003\tsfa:global:c:carindex\t500000\n" +
                "81\tSGB首页\t500004\tsfa:global:sgb:index\t500000\n" +
                "1\t01首页（通用）\t500005\tsfa:global:common:index\t500000\n" +
                "52\t活动计划\t500006\tsfa:global:c:carindex:plan\t500003";

        str = "4\t04陈列拍照（通用）\t300003\tsfa:inner:common:shelvesphotos\t300000\t04陈列拍照（通用）\n" +
                "5\t05本品信息（GT/MT）\t300004\tsfa:inner:gtmt:shskuinfo\t300000\t05本品信息（GT/MT）\n" +
                "6\t06库存盘点（通用）\t300005\tsfa:inner:common:stockinventory\t300000\t06库存盘点（R）\n" +
                "7\t07新产品销售（R）\t300006\tsfa:inner:r:newsale\t300000\t07新产品销售（R）\n" +
                "8\t08本品订单（GT/MT）\t300007\tsfa:inner:gtmt:shorder\t300000\t移除\n" +
                "9\t09本品订单（R）\t300008\tsfa:inner:r:shorder\t300000\t09本品订单（R）\n" +
                "10\t10竞品信息（GT/MT）\t300009\tsfa:inner:gtmt:competitioninfo\t300000\t10竞品信息（GT/MT）\n" +
                "11\t11竞品信息（R）\t300010\tsfa:inner:r:competitioninfo\t300000\t11竞品商机（R）\n" +
                "12\t12餐饮物料核销（R）\t300011\tsfa:inner:r:matewriteoff\t300000\t12餐饮物料核销（R）\n" +
                "13\t13活动核销（通用）\t300012\tsfa:inner:common:fm\t300000\t13活动核销（通用）\n" +
                "14\t14欣和活动跟进（R）\t300013\tsfa:inner:r:shfmfollowup\t300000\t14欣和活动跟进（R）\n" +
                "15\t15签出小结（GT/MT）\t300014\tsfa:inner:gtmt:visitout\t300000\t15签出小结（GT/MT）\n" +
                "16\t16签出小结（R）\t300015\tsfa:inner:r:visitout\t300000\t16签出小结（R）\n" +
                "19\t19美顾评价（MT）\t300016\tsfa:inner:mt:evaluation\t300000\t19美顾评价（MT）\n" +
                "20\t20菜品收集（R）\t300017\tsfa:inner:r:dishcollect\t300000\t20菜品商机（R）\n" +
                "26\t26销量提报（GT/MT）\t300018\tsfa:inner:gtmt:salequantity\t300000\t26销量提报（GT/MT）\n" +
                "27\t27协同拜访（通用）\t300019\tsfa:inner:common:togvisit\t300000\t27协同拜访（GT/MT）\n" +
                "28\t28拜访历史（通用）\t300020\tsfa:inner:common:visithistory\t300000\t28拜访历史（通用）\n" +
                "29\t29美顾反馈（MT）\t300021\tsfa:inner:mt:feedback\t300000\t29美顾反馈（MT）\n" +
                "34\t34CE 活动跟进\t300022\tsfa:inner:common:cefollowup\t300000\t34CE 活动跟进（通用）\n" +
                "38\t38进货提报（R）\t300023\tsfa:inner:r:takeinquantity\t300000\t38进货提报（R）\n" +
                "41\t41随访辅导 (R)\t300024\tsfa:inner:r:visitcoach\t300000\t41随访辅导（通用）\n" +
                "42\t42库存盘点 (GT/MT)\t300025\tsfa:inner:gtmt:stockinventory\t300000\t42库存盘点 (GT/MT)\n" +
                "45\t本品分销\t300026\tsfa:inner:common:distributionshsku\t300000\t本品分销（GT）\n" +
                "46\t竞品分销\t300027\tsfa:inner:common:distributioncompetition\t300000\t竞品分销（GT）\n" +
                "47\t本品活动陈列\t300028\tsfa:inner:common:acitvityshelvesshsku\t300000\t本品活动陈列（GT）\n" +
                "48\t竞品活动陈列\t300029\tsfa:inner:common:acitvityshelvescompetition\t300000\t竞品活动陈列（GT）\n" +
                "79\t菜品反馈\t300030\tsfa:inner:common:dishfeedback\t300000\t菜品推广（R）\n" +
                "77\t销量提报(临促)\t300031\tsfa:inner:common:lcsalequantity\t300000\t销量提报(临促)\n" +
                "83\tSGB商机\t300032\tsfa:inner:sgb:opportunity\t300000\tSGB商机\n" +
                "84\tSGB库存\t300033\tsfa:inner:sgb:stock\t300000\tSGB库存\n" +
                "85\tSGB竞品信息\t300034\tsfa:inner:sgb:competition\t300000\tSGB竞品信息\n" +
                "88\t黄飞鸿本品订单\t300035\tsfa:inner:hfh:shorder\t300000\t黄飞红本品订单";

        str = "67\t车辆情况(C)\t400001\tsfa:global:c:carinfo\t400000\n" +
                "69\t车辆油耗(C)\t400002\tsfa:global:c:consumption\t400000\n" +
                "70\t车辆费用(C)\t400003\tsfa:global:c:cost\t400000\n" +
                "71\t未出车报备(C)\t400004\tsfa:global:c:noout\t400000\n" +
                "74\t维保计划(C)\t400005\tsfa:global:c:repair\t400000\n" +
                "75\t维保费用(C)\t400006\tsfa:global:c:repaircost\t400000";

        String[] split = str.split("\n");
        StringBuilder sbUnMatch = new StringBuilder();
        for (String s : split) {
            String workId = s.split("\t")[0];
            String name = s.split("\t")[1];
            String id = s.split("\t")[2];
            String key = s.split("\t")[3];
            String parentId = s.split("\t")[4];
            String[] pathList = key.split(":");
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < pathList.length; i++) {
                sb.append(pathList[i]);
                if (i < pathList.length - 1) {
                    sb.append("_");
                }
            }
            String field = sb.toString().toUpperCase();
//            System.out.println("/**");
//            System.out.println(" * " + name);
//            System.out.println(" **/");
//            System.out.printf("public static final String %s = \"%s\";%n", field, key);
//            System.out.println();

            System.out.printf("case \"%s\": // %s\n", key, name);
            System.out.printf("\treturn \"%s\";\n", workId);

            Map<String, String> infoMap = idInfoMap.get(workId);
            if (infoMap == null) {
                sbUnMatch.append(s).append("\n");
                continue;
            }
            String displayName = infoMap.get("displayName");
            String icon = infoMap.get("icon");
            String actClass = infoMap.get("actClass");

//            System.out.printf("case \"%s\":\n", key);
//            System.out.printf("\titem.setName(\"%s\"); // %s\n", displayName, name);
//            System.out.printf("\titem.setParentPermissionId(%s);\n", parentId);
//            System.out.printf("\titem.setPermissionId(%s);\n", id);
//            System.out.printf("\titem.setIcon(%s);\n", icon);
//            System.out.printf("\titem.setActClass(%s);\n", actClass);
//            System.out.println("\tbreak;");
        }

        str = "拜访记录卡照片\t300176\tsfa:inner:gtmt:visitout:photo\t300014\t\n" +
                "审核拜访\t300177\tsfa:inner:common:visitaudit\t300000\t" +
                "订单审核\t200039\tsfa:global:common:audit:order\t200032\t\n" +
                "经销商门店审核\t200040\tsfa:global:common:audit:dealer\t200032\t\n" +
                "报备线路审核\t200041\tsfa:global:common:audit:storedeclare\t200032\t\n" +
                "活动审核\t200042\tsfa:global:common:audit:activity\t200032\t\n" +
                "办公车\t400007\tsfa:global:c:carinfo:bg\t400001\n" +
                "厨演车\t400008\tsfa:global:c:carinfo:cy\t400001\n" +
                "铺货车\t400009\tsfa:global:c:carinfo:ph\t400001\n" +
                "拜访统计\t6000001\tsfa:usercenter:statistics:visit\t600000\n" +
                "车辆行程统计\t6000002\tsfa:usercenter:statistics:car\t600000";
        for (String s : str.split("\n")) {
            String name = s.split("\t")[0];
            String key = s.split("\t")[2];
            String[] pathList = key.split(":");
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < pathList.length; i++) {
                sb.append(pathList[i]);
                if (i < pathList.length - 1) {
                    sb.append("_");
                }
            }
            String field = sb.toString().toUpperCase();
            System.out.println("/**");
            System.out.println(" * " + name);
            System.out.println(" **/");
            System.out.printf("public static final String %s = \"%s\";%n", field, key);
            System.out.println();
        }

//        System.out.println();
//        System.out.println(sbUnMatch.toString());
    }

    private static void method1() {
        for (File file : FileUtils.getAllFiles("/Users/chunyangli/Documents/GitHub/ShinhoAndroidUtils/app/src/main/java/com/shinho/android/utils")) {
            List<MethodInfo> infos = new ArrayList<>();
            MethodInfo info = new MethodInfo();
            ArrayList<String> strings = FileUtils.readToStringLines(file);
            for (String line : strings) {
                if (line.startsWith("     * ")) {
                    line = line.replace("*", "").trim();
                    if (line.startsWith("@")) continue;
                    info.name = line;
                } else if (line.startsWith("    public static")) {
                    if (!line.contains("(")) continue;
                    line = line.replace(" <T>", "")
                            .replace(" <T, E>", "");
                    line = line.trim().split(" ")[3];
                    line = line.substring(0, line.indexOf("("));
                    info.method = line;

                    infos.add(info);
                    info = new MethodInfo();
                }
            }

            System.out.println();
            System.out.println("* ### " + file.getName().replace(".java", ""));
            System.out.println("```");
            for (MethodInfo info2 : infos) {
                System.out.println(info2.method + " ：" + info2.name);
            }
            System.out.println("```");
        }
    }

    static class StoreSimilarInfo {
        public String similarScore;
        public String businessStatus;

        public String storeName;
        public String storeSiebleUid;
        public String storeDcloudUid;

        public String contactName;
        public String contactPhone;

        public String locationAdress;
        public String locationLongitude;
        public String locationLatitude;

        public String isStoreNameSimilar;
        public String isContactNameSimilar;
        public String isContactPhoneSimilar;
        public String isLocationSimilar;
    }

    static class ReviewDetail {
        public String reviewId;
        public String reviewType;
        public String reviewProgress;

        public String applyUserId;
        public String applyUserName;
        public String applyDate;

        public String similarStoreCount;

        public StoreDetail oldStore;
        public StoreDetail newStore;
    }

    static class ReviewProcess {
        public String reviewId;
        public String reviewType;
        public String reviewProgress;

        public String applyUserId;
        public String applyUserName;
        public String applyDate;

        public List<ReviewProcessNode> processNodes;
    }

    static class ReviewProcessNode {
        public String index;
        public String userSiebleUid;
        public String userDcloudUid;
        public String userName;

        public String approved;
        public String approvedDate;
        public String approvedSuggestion;
        public String rejectSuggestion;
    }

    static class ReviewFeedback {
        public String reviewId;
        public String approved;
        public String approvedSuggestion;
        public String rejectSuggestion;
        public List<SuggestionField> suggestionFields;
    }

    static class SuggestionField {
        public String modular;
        public String name;
        public String desc;
        public String suggestion;
    }

    static class StoreDetail {
        public StoreDetailBaseInfo baseInfo;
        public StoreDetailExtraInfo extraInfo;
        public StoreDetailSurroundingInfo surroundingInfo;
        public StoreDetailResourceInfo resourceInfo;
    }

    static class StoreDetailBaseInfo {
        public String storeName;
        public String storeSiebleUid;
        public String storeDcloudUid;

        public String businessStatus;
        public String address;
        public String addressDetail;

        public String contactName;
        public String contactPhone;
        public String contactType;

        public String locationAdress;
        public String locationLongitude;
        public String locationLatitude;
    }

    static class StoreDetailExtraInfo {
        public String storePhoto;
        public String storeType;
        public String storeSubType;
        public String storeDetailType;
        public String supplySource;
    }

    static class StoreDetailSurroundingInfo {
        public String storeLocation;
        public String infrastructure;
        public String passengerFlow;
        public String photo;
        public String restrictVanPass;
    }

    static class StoreDetailResourceInfo {
        public String openingDate;
        public String area;
        public String cashierCount;
        public String decorationCost;
        public String ceiling;
        public String lighting;
        public String corridor;
        public String shelvesQuality;
        public String hygiene;
        public String shelvesTotalCount;
        public String shelvesSingleCount;
        public String soySauceCount;
        public String sauceCount;
        public String oysterSauceCount;
        public String vinegarCount;
        public String cookingWineCount;
        public String shelvesPhoto;
        public String maxShelvesBoxCount;
        public String maxGroundBoxCount;
        public String maxCutBoxCount;
        public String allowStall;
        public String storePhoto;
    }

//    public static void main(String[] args) throws Exception {
////        	POST /tomato/play/geturl HTTP/1.1
////          timestamp	1591323257
////          sign	02f794ba40c435838a5294f3435d69f1e0692e83
////          ver	1.1.0
////          app	tmplayer
////          Content-Type	application/json; charset=utf-8
////          Content-Length	124
////          Host	p.fanqie.tv
////          Connection	Keep-Alive
////          Accept-Encoding	gzip
////          User-Agent	okhttp/3.11.0
//
////        System.out.println(getSign());
//
//        HashMap<String, Object> response = new HashMap<>();
//        response.put("code", 200);
//        response.put("msg", "OK");
//
//        ReviewDetail detail = new ReviewDetail();
//        detail.reviewId = "审核订单ID";
//        detail.reviewType = "number 审核类型，1-状态变更；2-信息修改；3-门店新增";
//        detail.reviewProgress = "number 审核进度，0-也待提交；1-DOM审核；2-DAdmin审核；3-审核通过；4-审核终止";
//        detail.applyUserId = "申请人用户ID";
//        detail.applyUserName = "申请人用户名";
//        detail.applyDate = "申请时间";
//        detail.similarStoreCount = "number 相似门店数量";
//
//        StoreDetail oldStoreDetail = new StoreDetail();
//        detail.oldStore = oldStoreDetail;
//        detail.newStore = new StoreDetail();
//        detail.newStore.baseInfo = new StoreDetailBaseInfo();
//        detail.newStore.baseInfo.addressDetail = "修改后的地址。newStore字段和oldStore一样，只有修改的才有值，否则都为null不返回";
//        detail.newStore.resourceInfo = new StoreDetailResourceInfo();
//        detail.newStore.resourceInfo.cookingWineCount = "修改后的料酒排面数。newStore字段和oldStore一样，只有修改的才有值，否则都为null不返回";
//
//        oldStoreDetail.baseInfo = new StoreDetailBaseInfo();
//        oldStoreDetail.baseInfo.storeName = "门店名称";
//        oldStoreDetail.baseInfo.storeSiebleUid = "门店sieble编码";
//        oldStoreDetail.baseInfo.storeDcloudUid = "门店Dcloud编码";
//        oldStoreDetail.baseInfo.businessStatus = "营业状况";
//        oldStoreDetail.baseInfo.address = "所在地区";
//        oldStoreDetail.baseInfo.addressDetail = "详细地址";
//        oldStoreDetail.baseInfo.contactName = "联系人";
//        oldStoreDetail.baseInfo.contactPhone = "联系电话";
//        oldStoreDetail.baseInfo.contactType = "联系人类型";
//        oldStoreDetail.baseInfo.locationAdress = "定位地址";
//        oldStoreDetail.baseInfo.locationLongitude = "double 定位经度";
//        oldStoreDetail.baseInfo.locationLatitude = "double 定位纬度";
//
//        oldStoreDetail.extraInfo = new StoreDetailExtraInfo();
//        oldStoreDetail.extraInfo.storePhoto = "门头照";
//        oldStoreDetail.extraInfo.storeType = "客户类型";
//        oldStoreDetail.extraInfo.storeSubType = "客户子类型";
//        oldStoreDetail.extraInfo.storeDetailType = "客户细分类型";
//        oldStoreDetail.extraInfo.supplySource = "客户供货源";
//
//        oldStoreDetail.surroundingInfo = new StoreDetailSurroundingInfo();
//        oldStoreDetail.surroundingInfo.storeLocation = "门店位于";
//        oldStoreDetail.surroundingInfo.infrastructure = "周边设施建设";
//        oldStoreDetail.surroundingInfo.passengerFlow = "周边客流";
//        oldStoreDetail.surroundingInfo.photo = "周边环境拍照";
//        oldStoreDetail.surroundingInfo.restrictVanPass = "周边限制厢货车通行";
//
//        oldStoreDetail.resourceInfo = new StoreDetailResourceInfo();
//        oldStoreDetail.resourceInfo.openingDate = "门店开业时间";
//        oldStoreDetail.resourceInfo.area = "营业面积";
//        oldStoreDetail.resourceInfo.cashierCount = "收银台数量";
//        oldStoreDetail.resourceInfo.decorationCost = "装修花费";
//        oldStoreDetail.resourceInfo.ceiling = "天花板";
//        oldStoreDetail.resourceInfo.lighting = "灯光";
//        oldStoreDetail.resourceInfo.corridor = "过道";
//        oldStoreDetail.resourceInfo.shelvesQuality = "货架质量";
//        oldStoreDetail.resourceInfo.hygiene = "卫生";
//        oldStoreDetail.resourceInfo.shelvesTotalCount = "货架总数";
//        oldStoreDetail.resourceInfo.shelvesSingleCount = "单节货架层数";
//        oldStoreDetail.resourceInfo.soySauceCount = "酱油排面数";
//        oldStoreDetail.resourceInfo.sauceCount = "酱排面数";
//        oldStoreDetail.resourceInfo.oysterSauceCount = "耗油排面数";
//        oldStoreDetail.resourceInfo.vinegarCount = "醋排面数";
//        oldStoreDetail.resourceInfo.cookingWineCount = "料酒排面数";
//        oldStoreDetail.resourceInfo.shelvesPhoto = "货架拍照";
//        oldStoreDetail.resourceInfo.maxShelvesBoxCount = "最大端架箱数";
//        oldStoreDetail.resourceInfo.maxGroundBoxCount = "最大地堆箱数";
//        oldStoreDetail.resourceInfo.maxCutBoxCount = "最大割箱箱数";
//        oldStoreDetail.resourceInfo.allowStall = "店外是否可摆摊";
//        oldStoreDetail.resourceInfo.storePhoto = "店内全景拍照";
//
////        response.put("data", detail);
//
//
//        StoreSimilarInfo storeSimilarInfo = new StoreSimilarInfo();
//        storeSimilarInfo.similarScore = "相似度分数";
//        storeSimilarInfo.storeName = "门店名称";
//        storeSimilarInfo.storeSiebleUid = "门店sieble编码";
//        storeSimilarInfo.storeDcloudUid = "门店Dcloud编码";
//        storeSimilarInfo.businessStatus = "营业状况";
//        storeSimilarInfo.contactName = "联系人";
//        storeSimilarInfo.contactPhone = "联系电话";
//        storeSimilarInfo.locationAdress = "定位地址";
//        storeSimilarInfo.locationLongitude = "double 定位经度";
//        storeSimilarInfo.locationLatitude = "double 定位纬度";
//        storeSimilarInfo.isStoreNameSimilar = "boolean 是否相似，对应storeName门店名称";
//        storeSimilarInfo.isContactNameSimilar = "boolean 是否相似，对应contactName联系人";
//        storeSimilarInfo.isContactPhoneSimilar = "boolean 是否相似，对应contactPhone联系电话";
//        storeSimilarInfo.isLocationSimilar = "boolean 是否相似，对应地址";
//
////        response.put("data", Arrays.asList(storeSimilarInfo));
//
//        ReviewFeedback feedback = new ReviewFeedback();
//        feedback.reviewId = "审核订单ID";
//        feedback.approved = "boolean 是否审核通过";
//        feedback.approvedSuggestion = "通过意见";
//        feedback.rejectSuggestion = "驳回意见";
//
//        List<SuggestionField> fields = new ArrayList<>();
//        feedback.suggestionFields = fields;
//        SuggestionField field = new SuggestionField();
//        field.modular = "错误字段所属模块  baseInfo=基础信息;extraInfo=额外信息;surroundingInfo=周边特征;resourceInfo=门店资源";
//        field.name = "location";
//        field.desc = "定位";
//        field.suggestion = "定位信息偏差";
//        fields.add(field);
//        field = new SuggestionField();
//        field.modular = "baseInfo";
//        field.name = "contactPhone";
//        field.desc = "联系电话";
//        field.suggestion = "老板更换了手机号";
//        fields.add(field);
//
////        response.put("data", feedback);
//
//        ReviewProcess process = new ReviewProcess();
//        process.reviewId = "审核订单ID";
//        process.reviewType = "number 审核类型，1-状态变更；2-信息修改；3-门店新增";
//        process.reviewProgress = "number 审核进度，0-也待提交；1-DOM审核；2-DAdmin审核；3-审核通过；4-审核终止";
//        process.applyUserId = "申请人用户ID";
//        process.applyUserName = "申请人用户名";
//        process.applyDate = "申请时间";
//        process.processNodes = new ArrayList<>();
//        ReviewProcessNode node = new ReviewProcessNode();
//        node.index = "number 结点操作顺序，0开始，从小到大";
//        node.approvedDate = "审核结点操作时间";
//        node.userSiebleUid = "SFA sieble系统人员ID。如果是DSR/DAdmin不用填这个";
//        node.userDcloudUid = "D系统人员ID。如果是DOM不用填这个";
//        node.userName = "人员名称";
//        node.approved = "boolean 是否审核通过";
//        node.approvedSuggestion = "通过意见";
//        node.rejectSuggestion = "驳回意见";
//        process.processNodes.add(node);
//
//        response.put("data", process);
////        System.out.println(new Gson().toJson(response));
//
//
//        // 	POST /wolong/play/geturl HTTP/1.1
//        //timestamp	1592796690
//        //sign	8fba481bba91efe0a5abc72297a657345a5143b8
//        //ver	2.0.6
//        //app	tmplayer
//        //Content-Type	application/json; charset=utf-8
//        //Content-Length	136
//        //Host	api.tgmmvip.com
//        //Connection	Keep-Alive
//        //Accept-Encoding	gzip
//        //User-Agent	okhttp/3.9.0
//        System.out.println(getSign());
//
//    }

    public static String getSign() {
        try {
            String url = "http://video.qdetyy.com/dianshiju/94307/yinmidejiaoluo01/yinmidejiaoluo01_720.m3u8";

            String time = "1592796690";
//            String key = "JxrP88TDyFvDhovK";
            String key = "hwJ2WxU7yGqjFssT"; // wl

            StringBuilder sb1 = new StringBuilder();
            sb1.append(url);
            sb1.append(time);
            sb1.append("2.0.6");
            String sign = getAES(sb1.toString(), key);

            StringBuilder sb2 = new StringBuilder();
            sb2.append(sign);
            sb2.append(url);
            sb2.append(time);
            sign = sb2.toString();
            return getHmacSHA1(sign, time);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getAES(String str, String str2) throws Exception {
        if (str2 == null || str2.length() != 16) {
            return null;
        }
        byte[] bytes = str2.getBytes();
        SecretKeySpec secretKeySpec = new SecretKeySpec(str2.getBytes(), "AES");
        Cipher instance = Cipher.getInstance("AES/CBC/PKCS5Padding");
        instance.init(1, secretKeySpec, new IvParameterSpec(bytes));
        byte[] doFinal = instance.doFinal(str.getBytes());
        StringBuilder stringBuilder = new StringBuilder();
        int length = doFinal.length;
        for (int i = 0; i < length; i++) {
            stringBuilder.append(String.format("%02x", new Object[]{Byte.valueOf(doFinal[i])}));
        }
        str = stringBuilder.toString();
        while (str.length() < 32) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("0");
            stringBuilder.append(str);
            str = stringBuilder.toString();
        }
        return str;

    }

    public static String getHmacSHA1(String str, String str2) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(str2.getBytes(), "HmacSHA1");
            Mac instance = Mac.getInstance("HmacSHA1");
            instance.init(secretKeySpec);
            byte[] doFinal = instance.doFinal(str.getBytes());
            StringBuilder stringBuilder = new StringBuilder();
            int length = doFinal.length;
            for (int i = 0; i < length; i++) {
                stringBuilder.append(String.format("%02x", new Object[]{Byte.valueOf(doFinal[i])}));
            }
            str = stringBuilder.toString();
            while (str.length() < 32) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("0");
                stringBuilder.append(str);
                str = stringBuilder.toString();
            }
            return str;
        } catch (NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
            return null;
        } catch (Exception e2) {
            System.err.println(e2.getMessage());
            return null;
        }
    }

    private static void replaceEnum() {
        String json = FileUtils.readToString(new File("temp" + File.separator + "enum.txt"), "utf-8");
        JsonElement parse = new JsonParser().parse(json);
        Map<String, String> map = new HashMap<>();
        for (JsonElement je : parse.getAsJsonObject().getAsJsonArray("data")) {
            JsonObject jo = je.getAsJsonObject();
            if (jo.get("no_use_item_system_value").isJsonNull()) {
                continue;
            }
            String oldValue = jo.get("no_use_item_system_value").getAsString().trim();
            String newValue = jo.get("system_value").getAsString().trim();
            if (oldValue.isEmpty()) continue;

            map.put(oldValue, newValue);
        }
        System.out.println(map.size());
        map.remove("-");

        List<File> files = FileUtils.getAllFiles("/Users/chunyangli/Documents/code/mobile-android/app/src");
        for (File file : files) {
            if (file.getName().endsWith(".java")) {
                String content = FileUtils.readToString(file);

                boolean replace = false;
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    if (content.contains(entry.getKey())) {
                        System.out.println(file.getAbsolutePath());
                        System.out.println(entry.getKey() + " -> " + entry.getValue());
                        replace = true;
                    }
                }
                if (replace) {
                    System.out.println("------------");
                }
            }
        }
    }

    private static void shelfPhoto() {
        String json = FileUtils.readToString(new File("temp" + File.separator + "photo.json"), "utf-8");
        JsonElement parse = new JsonParser().parse(json);
        int qingxie = 0;
        int guoliang = 0;
        int guoan = 0;
        int mohu = 0;

        int total = 0;
        for (JsonElement je : parse.getAsJsonObject().getAsJsonObject("data").getAsJsonArray("records")) {
            String userSiebleUid = je.getAsJsonObject().get("userSiebleUid").getAsString();
            if ("MS113838".equals(userSiebleUid)) {
                continue;
            }

            String bsPhoto = je.getAsJsonObject().get("bsPhoto").getAsString();
            total += bsPhoto.split(",").length;

            String errorInfo = je.getAsJsonObject().get("errorInfo").getAsString();
            String errorMsgList = new JsonParser().parse(errorInfo).getAsJsonObject().get("msg").getAsString();
            for (String error : errorMsgList.split(";")) {
                if (error.contains("倾斜")) qingxie++;
                else if (error.contains("过亮")) guoliang++;
                else if (error.contains("过暗")) guoan++;
                else if (error.contains("模糊")) mohu++;
            }
        }

        System.out.println("倾斜 " + qingxie);
        System.out.println("过亮 " + guoliang);
        System.out.println("过暗 " + guoan);
        System.out.println("模糊 " + mohu);
        System.out.println("错误总 " + (qingxie + guoliang + guoan + mohu));
        System.out.println("total " + total);
        System.out.println(1f * (qingxie + guoliang + guoan + mohu) / total);
    }


}
