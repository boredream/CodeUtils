package polygon;

import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.operation.buffer.BufferOp;
import org.locationtech.jts.operation.buffer.BufferParameters;
import org.locationtech.jts.operation.polygonize.Polygonizer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class JtsUtils {

    public static void main(String[] args) {
        // 创建线
        ArrayList<LineString> lineList = new ArrayList<>();
        lineList.add(createLine("1-1,1-2,2-2,2-1,1.1-1"));
//        lineList.add(createLine("1.2-1.3,1.2-2.3,2.2-2.3,2.2-1.3,1.2-1.3"));
        lineList.add(createLine("3-3,4-3,4-4,3-4,3-3"));

//        // 先 line-buffer 后 merge
//        ArrayList<Geometry> lineBufferList = new ArrayList<>();
//        for (LineString line : lineList) {
//            Geometry polygon = createLineBuffer(line);
//            lineBufferList.add(polygon);
//        }
//        Geometry merge = lineBufferList.get(0)
//                .union(lineBufferList.get(1));

        // 先 merge 后 line-buffer
        GeometryFactory geometryFactory = new GeometryFactory();
        MultiLineString multiLineString = geometryFactory.createMultiLineString(lineList.toArray(new LineString[lineList.size()]));
        Geometry merge = createLineBuffer(multiLineString);

        ArrayList<Polygon> polygonList = new ArrayList<>();
        if(merge instanceof Polygon) {
            polygonList.add((Polygon) merge);
        } else if(merge instanceof MultiPolygon) {
            for (int i = 0; i < merge.getNumGeometries(); i++) {
                Geometry geometry = merge.getGeometryN(i);
                polygonList.add((Polygon) geometry);
            }
        }

        // 绘制
        JTSExample jts = new JTSExample(lineList, polygonList);
        jts.draw();
    }

    private static Geometry createLineBuffer(Geometry line) {
        BufferParameters bufferParams = new BufferParameters();
        bufferParams.setEndCapStyle(BufferParameters.CAP_SQUARE);
        bufferParams.setJoinStyle(BufferParameters.JOIN_BEVEL);
        BufferOp bufferOp = new BufferOp(line, bufferParams);
        double width = 0.1; // 缓冲区宽度为 0.5
        return bufferOp.getResultGeometry(width);
    }

    private static LineString createLine(String lineStr) {
        String[] split = lineStr.split(",");
        ArrayList<Coordinate> coordinates = new ArrayList<>();
        for (String coordinateStr : split) {
            double x = Double.parseDouble(coordinateStr.split("-")[0]);
            double y = Double.parseDouble(coordinateStr.split("-")[1]);
            coordinates.add(new Coordinate(x, y));
        }
        Coordinate[] coords = coordinates.toArray(new Coordinate[coordinates.size()]);
        GeometryFactory factory = new GeometryFactory();
        return factory.createLineString(coords);
    }

    static class JTSExample extends JPanel {

        private ArrayList<LineString> lineList;
        private ArrayList<Polygon> polygonList;

        public JTSExample(ArrayList<LineString> lineList, ArrayList<Polygon> polygonList) {
            this.lineList = lineList;
            this.polygonList = polygonList;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // 绘制原始线
            drawLine(g2d);

            // 绘制多边形
            drawPolygon(g2d);
        }

        private void drawPolygon(Graphics2D g2d) {
            if(polygonList == null) {
                return;
            }

            for (Polygon polygon : polygonList) {
                // 遍历多边形的所有环，绘制线
                for (int i = 0; i < polygon.getNumInteriorRing() + 1; i++) {
                    Coordinate[] coords;
                    if (i == 0) {
                        coords = polygon.getExteriorRing().getCoordinates();
                    } else {
                        coords = polygon.getInteriorRingN(i - 1).getCoordinates();
                    }

                    int[] xPoints = new int[coords.length];
                    int[] yPoints = new int[coords.length];
                    for (int j = 0; j < coords.length; j++) {
                        // 将坐标转换为屏幕坐标
                        xPoints[j] = (int) (coords[j].x * 100);
                        yPoints[j] = (int) (coords[j].y * 100);
                    }

                    g2d.setColor(Color.RED);
                    g2d.drawPolygon(xPoints, yPoints, coords.length);
                }

                // 遍历所有内环，绘制面
                for (int i = 0; i < polygon.getNumInteriorRing(); i++) {
                    Coordinate[] coords = polygon.getInteriorRingN(i).getCoordinates();
                    int[] xPoints = new int[coords.length];
                    int[] yPoints = new int[coords.length];
                    for (int j = 0; j < coords.length; j++) {
                        // 将坐标转换为屏幕坐标
                        xPoints[j] = (int) (coords[j].x * 100);
                        yPoints[j] = (int) (coords[j].y * 100);
                    }
                    g2d.setColor(new Color(255, 0, 0, 50));
                    g2d.fillPolygon(xPoints, yPoints, coords.length);
                }
            }
        }

        private void drawLine(Graphics2D g2d) {
            g2d.setColor(Color.BLUE);
            for (LineString line : lineList) {
                int[] xPoints = new int[line.getNumPoints()];
                int[] yPoints = new int[line.getNumPoints()];
                for (int i = 0; i < line.getNumPoints(); i++) {
                    xPoints[i] = (int) (line.getCoordinateN(i).x * 100);
                    yPoints[i] = (int) (line.getCoordinateN(i).y * 100);
                }
                g2d.drawPolyline(xPoints, yPoints, line.getNumPoints());
            }
        }

        public void draw() {
            // 创建绘图窗口，并将多边形添加到面板中
            JFrame frame = new JFrame("JTS Example");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 800);
            frame.add(new JTSExample(lineList, polygonList));
            frame.setVisible(true);
        }
    }

}
