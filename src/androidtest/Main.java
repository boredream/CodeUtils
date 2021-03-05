package androidtest;

import entity.AndroidTestAction;
import entity.AndroidTestAssertion;
import entity.AndroidTestInfo;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utils.OfficeUtils;
import utils.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws Exception {
        File file = new File("temp" + File.separator + "androidtest" + File.separator + "test.xlsx");

        XSSFWorkbook xssfSheets = OfficeUtils.openXlsx(file);

        if (xssfSheets != null) {
            XSSFSheet sheet1 = xssfSheets.getSheetAt(0);

            List<AndroidTestInfo> testInfos = parseAllTestInfo(sheet1);
            String s = generateTestCode(testInfos);
            System.out.println(s);
        }
    }

    private static String generateTestCode(List<AndroidTestInfo> testInfos) {
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtils.formatSingleLine(1, "@Rule"));
        sb.append(StringUtils.formatSingleLine(1, "public ActivityTestRule<XXXActivity> mActivityRule = new ActivityTestRule<>(XXXActivity.class, true, false);"));
        sb.append("\n");

        for (int i = 0; i < testInfos.size(); i++) {
            AndroidTestInfo testInfo = testInfos.get(i);
            List<AndroidTestAction> actions = testInfo.getActions();
            AndroidTestAssertion assertion = testInfo.getAssertion();

            String number = testInfo.getNumber().replace(".0", "");
            if (number.equals("-1")) {
                number = i + "";
            }
            String testMethodName = "test" + number;

            sb.append(StringUtils.formatSingleLine(1, "/**"));
            sb.append(StringUtils.formatSingleLine(1, " * " + testInfo.getTitle()));
            sb.append(StringUtils.formatSingleLine(1, " */"));
            sb.append(StringUtils.formatSingleLine(1, "@Test"));
            sb.append(StringUtils.formatSingleLine(1, "public void " + testMethodName + "() throws InterruptedException {"));
            sb.append(StringUtils.formatSingleLine(2, "Intent intent = new Intent();"));
            sb.append(StringUtils.formatSingleLine(2, "mActivityRule.launchActivity(intent);"));
            sb.append("\n");
            sb.append(StringUtils.formatSingleLine(2, "// actions"));
            for (AndroidTestAction action : actions) {
                StringBuilder sbAction = new StringBuilder();
                sbAction.append("onView(withContentDescription(\"" + action.getView() + "\"))");
                switch (action.getActionType()) {
                    case AndroidTestAction.TYPE_TYPE_TEXT:
                        if(action.isNotAction()) {
                            sbAction.append("; // do nothing");
                        } else {
                            sbAction.append(".perform(typeText(\"" + action.getText() + "\"), closeSoftKeyboard());");
                        }
                        break;
                    case AndroidTestAction.TYPE_CLICK:
                        sbAction.append(".perform(click());");
                        break;
                }
                sb.append(StringUtils.formatSingleLine(2, sbAction.toString()));
            }
            sb.append("\n");
            sb.append(StringUtils.formatSingleLine(2, "Thread.sleep(500);"));
            sb.append("\n");
            sb.append(StringUtils.formatSingleLine(2, "// assertions"));
            switch (assertion.getAssertionType()) {
                case AndroidTestAssertion.TYPE_SHOW_TOAST:
                    sb.append(StringUtils.formatSingleLine(2, "onView(withId(android.R.id.message))"));
                    sb.append(StringUtils.formatSingleLine(3, ".inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))"));
                    sb.append(StringUtils.formatSingleLine(3, ".check(matches(withText(\"" + assertion.getText() + "\")));"));
                    break;
                default:
                    sb.append(StringUtils.formatSingleLine(2, "// TODO write your assertion~ or waiting seconds for check result by yourself"));
                    sb.append(StringUtils.formatSingleLine(2, "Thread.sleep(3000);"));
                    break;
            }
            sb.append(StringUtils.formatSingleLine(1, "}"));
            sb.append("\n");
        }

        return sb.toString();
    }

    private static List<AndroidTestInfo> parseAllTestInfo(XSSFSheet sheet1) {
        List<AndroidTestInfo> testInfos = new ArrayList<>();

        for (int rowIndex = 0; rowIndex < sheet1.getPhysicalNumberOfRows(); rowIndex++) {
            Row row = sheet1.getRow(rowIndex);

            if (rowIndex == 0) {
                parseHeaderInfo(row);
            } else {
                AndroidTestInfo testInfo = parseTestInfo(row);
                testInfos.add(testInfo);
            }
        }

        return testInfos;
    }

    private static AndroidTestInfo parseTestInfo(Row row) {
        String num = getCellString(row.getCell(indexs.get(INDEX_NUMBER)));
        String title = getCellString(row.getCell(indexs.get(INDEX_TITLE)));
        String actions = getCellString(row.getCell(indexs.get(INDEX_ACTIONS)));
        String assertion = getCellString(row.getCell(indexs.get(INDEX_ASSERTION)));


        AndroidTestInfo testInfo = new AndroidTestInfo();
        testInfo.setNumber(num);
        testInfo.setTitle(title);
        testInfo.setActions(parseActions(actions));
        testInfo.setAssertion(parseAssertion(assertion));

        return testInfo;
    }

    // 编号
    private static final String INDEX_NUMBER = "number";
    // 测试项
    private static final String INDEX_TITLE = "title";
    // 操作步骤
    private static final String INDEX_ACTIONS = "actions";
    // 预期结果
    private static final String INDEX_ASSERTION = "assertion";
    // 数据列索引信息
    private static Map<String, Integer> indexs;

    private static void parseHeaderInfo(Row row) {
        indexs = new HashMap<>();

        for (int cellIndex = 0; cellIndex < row.getPhysicalNumberOfCells(); cellIndex++) {
            Cell cell = row.getCell(cellIndex);

            String cellValue = getCellString(cell);

            switch (cellValue) {
                case "编号":
                    indexs.put(INDEX_NUMBER, cellIndex);
                    break;
                case "测试项":
                    indexs.put(INDEX_TITLE, cellIndex);
                    break;
                case "操作步骤":
                    indexs.put(INDEX_ACTIONS, cellIndex);
                    break;
                case "预期结果":
                    indexs.put(INDEX_ASSERTION, cellIndex);
                    break;
            }
        }
    }

    private static List<AndroidTestAction> parseActions(String actionContent) {
        List<AndroidTestAction> actions = new ArrayList<>();

        Pattern pattern = Pattern.compile("([\\d]{1,2})、(【.*?】)(【.*?】)?(：[\\S]+[\\s]+)?");
        Matcher matcher = pattern.matcher(actionContent);

        while (matcher.find()) {
            int step = -1;
            if (matcher.group(1) != null) {
                step = Integer.parseInt(matcher.group(1));
            }

            boolean isNotAction = false;
            int actionType = -1;
            String actionTypeContent = matcher.group(2);
            if (actionTypeContent != null) {
                if(actionTypeContent.startsWith("【不")) {
                    isNotAction = true;
                    actionTypeContent = actionTypeContent.replaceFirst("不", "");
                }

                switch (actionTypeContent) {
                    case "【输入】":
                        actionType = AndroidTestAction.TYPE_TYPE_TEXT;
                        break;
                    case "【点击】":
                        actionType = AndroidTestAction.TYPE_CLICK;
                        break;
                }
            }

            String view = null;
            if (matcher.group(3) != null) {
                view = matcher.group(3).replace("【", "").replace("】", "");
            }

            String text = null;
            if (matcher.group(4) != null) {
                text = matcher.group(4).replaceFirst("：", "").trim();
            }

            AndroidTestAction action = new AndroidTestAction();
            action.setStep(step);
            action.setNotAction(isNotAction);
            action.setActionType(actionType);
            action.setView(view);
            action.setText(text);

            actions.add(action);
        }

        return actions;
    }

    private static AndroidTestAssertion parseAssertion(String assertionContent) {
        Pattern pattern = Pattern.compile("(【.*?】)(：[\\S\\s]+)?");
        Matcher matcher = pattern.matcher(assertionContent);

        if (matcher.find()) {
            int assertionType = -1;
            if (matcher.group(1) != null) {
                switch (matcher.group(1)) {
                    case "【提示】":
                        assertionType = AndroidTestAssertion.TYPE_SHOW_TOAST;
                        break;
                    case "【进入页面】":
                        assertionType = AndroidTestAssertion.TYPE_SHOW_ACTIVITY;
                        break;
                }
            }

            String text = null;
            if (matcher.group(2) != null) {
                text = matcher.group(2).replaceFirst("：", "").trim();
            }

            AndroidTestAssertion assertion = new AndroidTestAssertion();
            assertion.setAssertionType(assertionType);
            assertion.setText(text);

            return assertion;
        }
        return null;
    }

    private static String getCellString(Cell cell) {
        String cellValue;
        try {
            cellValue = cell.getStringCellValue();
        } catch (IllegalStateException e) {
            cellValue = cell.getNumericCellValue() + "";
        }
        return cellValue;
    }
}
