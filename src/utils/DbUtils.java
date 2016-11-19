package utils;

import entity.ClassInfo;

import java.io.File;
import java.util.List;

public class DbUtils {
    public static void main(String[] args) {
        genDbCode();
    }

    private static void genDbCode() {
        List<File> entityFiles = FileUtils.getAllFiles(new File(
                "temp" + File.separator + "db" + File.separator + "entities"));

        for (File file : entityFiles) {
            ClassInfo info = ClassUtils.readClassInfo(file);

            genColumnCode(info);
            genTableCode(info);
            genCURDCode(info);

        }
    }

    private static void genTableCode(ClassInfo info) {
        StringBuilder sbTable = new StringBuilder();

        sbTable.append(StringUtils.formatSingleLine(0, "String sql = \"CREATE TABLE IF NOT EXISTS \""));
        sbTable.append(StringUtils.formatSingleLine(2,
                "+ DataModel." + info.className + ".TABLE_NAME + \"(\""));
        if (getPrimaryKey(info) == null) {
            sbTable.append(StringUtils.formatSingleLine(2,
                    "+ DataModel." + info.className + "._ID + \" INTEGER PRIMARY KEY AUTOINCREMENT,\""));
        }

        for (ClassInfo.ClassField field : info.fields) {
            String name = getColumnString(field);
            String type = ClassUtils.parsePri2DBType(field);
            if (field.isPrimaryKey) {
                type += " PRIMARY KEY";
            }
            sbTable.append(StringUtils.formatSingleLine(2,
                    "+ DataModel." + info.className + "." + name + " + \" " + type + ",\""));
        }
        sbTable.append(StringUtils.formatSingleLine(2, "+ \")\";"));
        System.out.println(sbTable.toString());
    }

    private static ClassInfo.ClassField getPrimaryKey(ClassInfo info) {
        for (ClassInfo.ClassField field : info.fields) {
            if (field.isPrimaryKey) {
                return field;
            }
        }
        return null;
    }

    private static void genColumnCode(ClassInfo info) {
        StringBuilder sbColumn = new StringBuilder();
        sbColumn.append(StringUtils.formatSingleLine(0,
                "public interface " + info.className + " extends BaseColumns {"));
        sbColumn.append(StringUtils.formatSingleLine(1,
                formatString("TABLE_NAME", StringUtils.camelTo_(info.className))));
        for (ClassInfo.ClassField field : info.fields) {
            String name = getColumnString(field);
            String value = getColumnString(field).toLowerCase();
            sbColumn.append(StringUtils.formatSingleLine(1, formatString(name, value)));
        }
        System.out.println(sbColumn.toString());
    }

    private static String formatString(String name, String value) {
        String str = "String %s = \"%s\";";
        return String.format(str, name, value);
    }

    private static void genCURDCode(ClassInfo info) {
        // add or update data
        StringBuilder sbAddOrUpdate = new StringBuilder();
        sbAddOrUpdate.append(StringUtils.formatSingleLine(1,
                "public void addOrUpdate" + info.className + "(" + info.className + " data) {"));
        sbAddOrUpdate.append(StringUtils.formatSingleLine(2,
                "SQLiteDatabase db = helper.getWritableDatabase();"));
        sbAddOrUpdate.append("\n");

        sbAddOrUpdate.append(StringUtils.formatSingleLine(2,
                "ContentValues value = new ContentValues();"));
        for (ClassInfo.ClassField field : info.fields) {
            String name = getColumnString(field);
            sbAddOrUpdate.append(StringUtils.formatSingleLine(2,
                    "value.put(DataModel." + info.className + "." + name + ", " + getDataGetMethod(field) + ");"));
        }
        sbAddOrUpdate.append("\n");

        ClassInfo.ClassField primaryKeyField = getPrimaryKey(info);
        if (primaryKeyField != null) {
            sbAddOrUpdate.append(StringUtils.formatSingleLine(2,
                    "int update = db.update(DataModel." + info.className + ".TABLE_NAME,"));
            sbAddOrUpdate.append(StringUtils.formatSingleLine(4,
                    "value,"));
            sbAddOrUpdate.append(StringUtils.formatSingleLine(4,
                    "DataModel." + info.className + "." + getColumnString(primaryKeyField) + " + \"=?\","));
            sbAddOrUpdate.append(StringUtils.formatSingleLine(4,
                    "new String[]{" + getDataGetMethod(primaryKeyField) + "});"));
            sbAddOrUpdate.append("\n");

            sbAddOrUpdate.append(StringUtils.formatSingleLine(2,
                    "if (update < 0) {"));
            sbAddOrUpdate.append(StringUtils.formatSingleLine(3,
                    "// 更新失败即代表无此数据，然后进行添加"));
            sbAddOrUpdate.append(StringUtils.formatSingleLine(3,
                    "db.insert(DataModel." + info.className + ".TABLE_NAME, null, value);"));
            sbAddOrUpdate.append(StringUtils.formatSingleLine(2,
                    "}"));
        } else {
            sbAddOrUpdate.append(StringUtils.formatSingleLine(2,
                    "db.insert(DataModel." + info.className + ".TABLE_NAME, null, value);"));
        }
        sbAddOrUpdate.append(StringUtils.formatSingleLine(1,
                "}"));
        System.out.println(sbAddOrUpdate.toString());


        // add data list
        StringBuilder sbAddList = new StringBuilder();
        sbAddList.append(StringUtils.formatSingleLine(1,
                "public void add" + info.className + "List(ArrayList<" + info.className + "> datas) {"));
        sbAddList.append(StringUtils.formatSingleLine(2,
                "SQLiteDatabase db = helper.getWritableDatabase();"));
        sbAddList.append("\n");

        sbAddList.append(StringUtils.formatSingleLine(2,
                "db.beginTransaction();"));
        sbAddList.append("\n");

        sbAddList.append(StringUtils.formatSingleLine(2,
                "String sql = \"INSERT INTO \" + DataModel." + info.className + ".TABLE_NAME + \" (\""));
        StringBuilder sbValue = new StringBuilder();
        sbValue.append("+ \"VALUES (");
        for (int i = 0; i < info.fields.size(); i++) {
            ClassInfo.ClassField field = info.fields.get(i);
            if (i == info.fields.size() - 1) {
                sbAddOrUpdate.append(StringUtils.formatSingleLine(4,
                        "+ DataModel." + info.className + "." + getColumnString(field) + " + \") \""));
                sbValue.append("?)\";");
            } else {
                // TODO: 2016/11/19 要考虑哪些字段要剔除
                sbAddOrUpdate.append(StringUtils.formatSingleLine(4,
                        "+ DataModel." + info.className + "." + getColumnString(field) + " + \", \""));
                sbValue.append("?, ");
            }
        }
        sbAddList.append(StringUtils.formatSingleLine(4, sbValue.toString()));
        sbAddList.append(StringUtils.formatSingleLine(2,
                "SQLiteStatement stmt = db.compileStatement(sql);"));
        sbAddList.append("\n");

        sbAddList.append(StringUtils.formatSingleLine(2, "// 事务批处理"));
        sbAddList.append(StringUtils.formatSingleLine(2, "for (" + info.className + " data : datas) {"));
        for (int i = 0; i < info.fields.size(); i++) {
            ClassInfo.ClassField field = info.fields.get(i);
            String bindMethod;
            switch (ClassUtils.parsePri2DBType(field)) {
                case "INTEGER":
                    bindMethod = "bindLong";
                    break;
                case "REAL":
                    bindMethod = "bindDouble";
                    break;
                case "TEXT":
                default:
                    bindMethod = "bindString";
                    break;
            }

            // TODO: 2016/11/19 有index的存在，要考虑哪些字段要剔除
            sbAddList.append(StringUtils.formatSingleLine(3,
                    "stmt." + bindMethod + "(" + (i + 1) + ", " + getDataGetMethod(field) + ");"));
        }
        sbAddList.append(StringUtils.formatSingleLine(3,
                "stmt.execute();"));
        sbAddList.append(StringUtils.formatSingleLine(3,
                "stmt.clearBindings();"));
        sbAddList.append(StringUtils.formatSingleLine(2,
                "}"));
        sbAddList.append("\n");

        sbAddList.append(StringUtils.formatSingleLine(2,
                "db.setTransactionSuccessful();"));
        sbAddList.append(StringUtils.formatSingleLine(2,
                "db.endTransaction();"));
        sbAddList.append(StringUtils.formatSingleLine(1,
                "}"));
        System.out.println(sbAddList.toString());


        // get data
        if (primaryKeyField != null) {
            // 有自定义主键，才提供根据主键获取对象方法
            StringBuilder sbGet = new StringBuilder();
            sbGet.append(StringUtils.formatSingleLine(1, "public " + info.className +
                    " get" + info.className + "(" + primaryKeyField.type + " key) {"));
            sbGet.append(StringUtils.formatSingleLine(2,
                    "SQLiteDatabase db = helper.getReadableDatabase();"));
            sbGet.append(StringUtils.formatSingleLine(2,
                    info.className + " data = null;"));
            sbGet.append(StringUtils.formatSingleLine(2,
                    "Cursor cursor = null;"));
            sbGet.append(StringUtils.formatSingleLine(2,
                    "try {"));
            sbGet.append(StringUtils.formatSingleLine(3,
                    "cursor = db.query(DataModel." + info.className + ".TABLE_NAME,"));
            sbGet.append(StringUtils.formatSingleLine(5,
                    "null,"));
            sbGet.append(StringUtils.formatSingleLine(5,
                    "DataModel." + info.className + "." + getColumnString(primaryKeyField) + " + \"=?\","));
            sbGet.append(StringUtils.formatSingleLine(5,
                    "new String[]{String.valueOf(key)},"));
            sbGet.append(StringUtils.formatSingleLine(5,
                    "null,"));
            sbGet.append(StringUtils.formatSingleLine(5,
                    "null,"));
            sbGet.append(StringUtils.formatSingleLine(5,
                    "null);"));

            sbGet.append(StringUtils.formatSingleLine(3,
                    "if (cursor != null && cursor.moveToFirst()) {"));
            sbGet.append(StringUtils.formatSingleLine(4,
                    "data = new " + info.className + "();"));
            sbGet.append(getDbSetDataStr(info, 4));
            sbGet.append(StringUtils.formatSingleLine(3,
                    "}"));
            sbGet.append(StringUtils.formatSingleLine(2,
                    "} finally {"));
            sbGet.append(StringUtils.formatSingleLine(3,
                    "if (cursor != null) cursor.close();"));
            sbGet.append(StringUtils.formatSingleLine(2,
                    "}"));
            sbGet.append(StringUtils.formatSingleLine(2,
                    "return data;"));
            sbGet.append(StringUtils.formatSingleLine(1,
                    "}"));
            System.out.println(sbGet.toString());
        }

        // TODO get data list
        StringBuilder sbGetList = new StringBuilder();
        sbGetList.append(StringUtils.formatSingleLine(1,
                "public ArrayList<" + info.className + "> get" + info.className + "List() {"));
        sbGetList.append(StringUtils.formatSingleLine(2,
                "SQLiteDatabase db = helper.getReadableDatabase();"));
        sbGetList.append(StringUtils.formatSingleLine(2,
                "ArrayList<" + info.className + "> datas = new ArrayList<>();"));
        sbGetList.append(StringUtils.formatSingleLine(2,
                "Cursor cursor = null;"));
        sbGetList.append(StringUtils.formatSingleLine(2,
                "try {"));
        sbGetList.append(StringUtils.formatSingleLine(3,
                "cursor = db.query(DataModel." + info.className + ".TABLE_NAME,"));
        sbGetList.append(StringUtils.formatSingleLine(5,
                "null,"));
        sbGetList.append(StringUtils.formatSingleLine(5,
                "null,"));
        sbGetList.append(StringUtils.formatSingleLine(5,
                "null,"));
        sbGetList.append(StringUtils.formatSingleLine(5,
                "null,"));
        sbGetList.append(StringUtils.formatSingleLine(5,
                "null,"));
        sbGetList.append(StringUtils.formatSingleLine(5,
                "null);"));

        sbGetList.append(StringUtils.formatSingleLine(3,
                "if (cursor != null && cursor.moveToFirst()) {"));
        sbGetList.append(StringUtils.formatSingleLine(4,
                "do {"));
        sbGetList.append(StringUtils.formatSingleLine(5,
                info.className + " data = new " + info.className + "();"));
        sbGetList.append(getDbSetDataStr(info, 5));
        sbGetList.append(StringUtils.formatSingleLine(5,
                "datas.add(data);"));
        sbGetList.append(StringUtils.formatSingleLine(4,
                "} while (cursor.moveToNext());"));
        sbGetList.append(StringUtils.formatSingleLine(3,
                "}"));
        sbGetList.append(StringUtils.formatSingleLine(2,
                "} finally {"));
        sbGetList.append(StringUtils.formatSingleLine(3,
                "if (cursor != null) cursor.close();"));
        sbGetList.append(StringUtils.formatSingleLine(2,
                "}"));
        sbGetList.append(StringUtils.formatSingleLine(2,
                "return datas;"));
        sbGetList.append(StringUtils.formatSingleLine(1,
                "}"));
        System.out.println(sbGetList.toString());


        // delete data list
        StringBuilder sbDeleteList = new StringBuilder();
        sbDeleteList.append(StringUtils.formatSingleLine(1, "public void delete" + info.className + "List() {"));
        sbDeleteList.append(StringUtils.formatSingleLine(2,
                "SQLiteDatabase db = helper.getWritableDatabase();"));
        sbDeleteList.append(StringUtils.formatSingleLine(2,
                "db.delete(DataModel." + info.className + ".TABLE_NAME, null, null);"));
        sbDeleteList.append(StringUtils.formatSingleLine(1,
                "}"));
        System.out.println(sbDeleteList.toString());

    }

    private static String getDbSetDataStr(ClassInfo info, int tableNum) {
        StringBuilder sbDbSetDataStr = new StringBuilder();
        for (int i = 0; i < info.fields.size(); i++) {
            ClassInfo.ClassField field = info.fields.get(i);
            String cursorGetStr;
            switch (field.type) {
                case "int":
                case "Integer":
                    cursorGetStr = "cursor.getInt(cursor.getColumnIndex(DataModel."
                            + info.className + "." + getColumnString(field) + "))";
                    break;
                case "long":
                case "Long":
                    cursorGetStr = "cursor.getLong(cursor.getColumnIndex(DataModel."
                            + info.className + "." + getColumnString(field) + "))";
                    break;
                case "float":
                case "Float":
                    cursorGetStr = "cursor.getFloat(cursor.getColumnIndex(DataModel."
                            + info.className + "." + getColumnString(field) + "))";
                    break;
                case "double":
                case "Double":
                    cursorGetStr = "cursor.getDouble(cursor.getColumnIndex(DataModel."
                            + info.className + "." + getColumnString(field) + "))";
                    break;
                case "boolean":
                case "Boolean":
                    cursorGetStr = "cursor.getInt(cursor.getColumnIndex(DataModel."
                            + info.className + "." + getColumnString(field) + ")) == 1";
                    break;
                case "String":
                default:
                    cursorGetStr = "cursor.getString(cursor.getColumnIndex(DataModel."
                            + info.className + "." + getColumnString(field) + "))";
                    break;
            }
            sbDbSetDataStr.append(StringUtils.formatSingleLine(tableNum,
                    "data." + String.format(field.getSetMethod(), cursorGetStr) + ";"));
        }
        return sbDbSetDataStr.toString();
    }

    private static String getDataGetMethod(ClassInfo.ClassField field) {
        String value = "data." + field.getGetMethod();
        if (field.type.equals("boolean") || field.type.equals("Boolean")) {
            value += " ? 1 : 0";
        }
        return value;
    }

    private static String getColumnString(ClassInfo.ClassField field) {
        return StringUtils.camelTo_(field.name).toUpperCase();
    }
}
