import tech.tablesaw.api.*;

import tech.tablesaw.columns.Column;
import tech.tablesaw.plotly.Plot;
import tech.tablesaw.plotly.api.ScatterPlot;


import java.io.IOException;
import java.util.List;

/**
 * 类描述
 *
 * @author yfk
 * @date 2021/3/18
 */
public class Visual {
    public static void main(String[] args) {
//        drawWin();
       // Column();
        drawCsv("D:\\logs\\watcher.csv");
        //tableTest();
    }
   static void Column(){
        double[] numbers = {1, 2, 3, 4};
        DoubleColumn nc = DoubleColumn.create("nc", numbers);
        System.out.println(nc.print());
       DoubleColumn nc2 = nc.multiply(4);
       System.out.println(nc2.print());
    }

    static void tableTest(){
        Table t = Table.create("name");
        IntColumn column = IntColumn.create("name");
        column.append(1);
        column.append(10);
        IntColumn star = IntColumn.create("num");
        star.append(5);
        star.append(10);

        t.addColumns(column,star);

//        Row data = new Row(t);
//       // data.
//        //List<String> list = data.columnNames();
//        data.setInt("name",10);
//        data.setInt("num",10);
//        t.addRow(data);
        Plot.show( ScatterPlot.create("Champagne prices by vintage",
                t, "name", "num"));

    }
    static public  void drawCsv(String path){
        Table wines = null;
        try {
            wines = Table.read().csv(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Table champagne =wines;
        Plot.show(
                ScatterPlot.create("Champagne prices by vintage",
                        champagne, "num", "times"));
    }
    static void drawWin(){
        Table wines = null;
        try {
            wines = Table.read().csv("D:\\31577\\桌面\\毕设\\project\\tablesaw-master\\data\\test_wines.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Table champagne =wines;
//        Plot.show(
//                ScatterPlot.create("Wine prices and ratings",
//                        champagne, "Mean Retail", "highest pro score", "wine type"));
        Plot.show(
                ScatterPlot.create("Champagne prices by vintage",
                        wines, "mean retail", "vintage"));
    }
//    public void test() {
//        //地址:http://echarts.baidu.com/doc/example/line5.html
//        EnhancedOption option = new EnhancedOption();
//        option.legend("高度(km)与气温(°C)变化关系");
//
//        option.toolbox().show(true).feature(
//                Tool.mark,
//                Tool.dataView,
//                new MagicType(Magic.line, Magic.bar),
//                Tool.restore,
//                Tool.saveAsImage);
//
//        option.calculable(true);
//        option.tooltip().trigger(Trigger.axis).formatter("Temperature : <br/>{b}km : {c}°C");
//
//        ValueAxis valueAxis = new ValueAxis();
//        valueAxis.axisLabel().formatter("{value} °C");
//        option.xAxis(valueAxis);
//
//        CategoryAxis categoryAxis = new CategoryAxis();
//        categoryAxis.axisLine().onZero(false);
//        categoryAxis.axisLabel().formatter("{value} km");
//        categoryAxis.boundaryGap(false);
//        categoryAxis.data(0, 10, 20, 30, 40, 50, 60, 70, 80);
//        option.yAxis(categoryAxis);
//
//        Line line = new Line();
//        line.smooth(true).name("高度(km)与气温(°C)变化关系")
//                .data(15, -50, -56.5, -46.5, -22.1, -2.5, -27.7, -55.7, -76.5)
//                .itemStyle().normal().lineStyle().shadowColor("rgba(0,0,0,0.4)");
//        option.series(line);
//        option.exportToHtml("line5.html");
//        option.view();
//    }
}
