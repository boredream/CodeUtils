package utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TraceUtils {

    static class Trace {
        public String dbId;
        public int distance;
        public long endTime;
        public boolean isDelete;
        public boolean isRecording;
        public String name;
        public long startTime;
        public boolean synced;
        public List<TracePoint> traceList;
    }

    static class TracePoint {
        public String dbId;
        public String extraData;
        public double latitude;
        public double longitude;
        public long time;
        public String traceRecordId;
    }

    static class TraceNew {
        public String name;
        public int distance;
        public long endTime;
        public long startTime;
        public List<TracePointNew> traceList;
    }

    static class TracePointNew {
        public double latitude;
        public double longitude;
        public long time;
    }

    public static void main(String[] args) {
        updateField();
//        String data = FileUtils.readToString(new File("temp/trace/trace_merge.json"), "utf-8");
//        List<Trace> traceList = new Gson().fromJson(data, new TypeToken<List<Trace>>() {
//        }.getType());
//        for (Trace trace : traceList) {
//            System.out.println(trace.name);
//        }
    }

    private static void updateField() {
        String data = FileUtils.readToString(new File("temp/trace/trace_merge.json"), "utf-8");
        List<TraceNew> traceList = new Gson().fromJson(data, new TypeToken<List<TraceNew>>() {
        }.getType());

        // 精简字段
        FileUtils.writeString2File(new Gson().toJson(traceList), new File("temp/trace/trace_new.json"), "utf-8");
    }

    private static void merge() {
        String data1 = FileUtils.readToString(new File("temp/trace/trace.json"), "utf-8");
        List<Trace> traceList1 = new Gson().fromJson(data1, new TypeToken<List<Trace>>() {
        }.getType());

        String data2 = FileUtils.readToString(new File("temp/trace/trace_old.json"), "utf-8");
        List<Trace> traceList2 = new Gson().fromJson(data2, new TypeToken<List<Trace>>() {
        }.getType());

        traceList1.addAll(traceList2);

        // 清洗数据 step1，缩减重复经纬度，合并文件
        for (Trace trace : traceList1) {
            List<TracePoint> newPointList = new ArrayList<>();
            for (TracePoint point : trace.traceList) {
                if (newPointList.size() > 0) {
                    TracePoint last = newPointList.get(newPointList.size() - 1);
                    if (point.latitude == last.latitude && point.longitude == last.longitude) {
                        // 移除重复经纬度，取最新时间
                        last.time = point.time;
                        continue;
                    }
                }
                newPointList.add(point);
            }
            trace.traceList = newPointList;
        }

        FileUtils.writeString2File(new Gson().toJson(traceList1), new File("temp/trace/trace_merge.json"), "utf-8");
    }

}
