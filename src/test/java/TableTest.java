
import graduation.util.FileUtil;
import graduation.util.Parameter;
import org.junit.Test;
import tech.tablesaw.api.CategoricalColumn;
import tech.tablesaw.api.Table;
import tech.tablesaw.plotly.Plot;
import tech.tablesaw.plotly.api.LinePlot;
import tech.tablesaw.plotly.api.ScatterPlot;
import tech.tablesaw.plotly.components.Figure;
import tech.tablesaw.plotly.components.Layout;
import tech.tablesaw.plotly.traces.ScatterTrace;
import tech.tablesaw.table.TableSliceGroup;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static graduation.service.Figure.hitsFigure;

/**
 * 类描述
 *
 * @author yfk
 * @date 2021/4/8
 */
public class TableTest {

    @Test
    // 联合不同的csv文件
    public void testTableJoin() throws IOException {
        Table[] tables = new Table[2];
        for (int i = 0; i < 2; i++) {
            String dir = String.format("D:\\data\\temp\\");
            String path = dir + String.format("%d.csv", i + 1);
            tables[i] = Table.read().csv(path);
           // tables[i].sortAscendingOn(Parameter.HITSId);
        }
        Table table = tables[0];
        for (int i = 1; i < 2; i++) {
           // tables[i] = tables[i].removeColumns(tables[i].column("id"));

            // tables[i].sortAscendingOn(Parameter.HITSId);
            //table = table.joinOn("Pid").leftOuter(tables[i]);
            table = table.append(tables[i]);
        }
        table.write().csv(new File("D:\\data\\temp\\final.csv"));
    }

    @Test
    // 绘制 x-times;y-value, groupby-Pid 多重折线图
    public void drawLine() throws IOException {
        //D:\data\temp
        String path = "D:\\data\\temp\\final.csv";
        Table table = Table.read().csv(path);

//        table = table.dropWhere(table.numberColumn("value").isGreaterThan(0.8));
//        String title = "hits", dateColX = "times", yCol = "Value", groupCol="Pid";
//        TableSliceGroup tables = table.splitOn(new CategoricalColumn[]{table.categoricalColumn(groupCol)});
//        Layout layout = Layout.builder(title, dateColX, yCol).build();
//        ScatterTrace[] traces = new ScatterTrace[tables.size()];
//        for(int i = 0; i < tables.size(); ++i) {
//            List<Table> tableList = tables.asTableList();
//            Table t = ((Table)tableList.get(i)).sortOn(new String[]{dateColX});
//            traces[i] = ScatterTrace.builder(t.intColumn(dateColX), t.numberColumn(yCol)).showLegend(true).name(((Table)tableList.get(i)).name()).mode(ScatterTrace.Mode.LINE).build();
//        }
//        Figure figure = new Figure(layout, traces);

       // TimeSeriesPlot.create()

        Plot.show( ScatterPlot.create(String.format("hitsValue%d-%s", 1, path),
                table, "id", "Value"), FileUtil.defaultFile("1.html"));
    }
//  table = table.dropWhere(table.numberColumn("value").isGreaterThan(0.8));
    @Test
    public void draw()  {
        hitsFigure();
        //Figure;
    }

}
