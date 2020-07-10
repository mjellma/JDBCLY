import com.jdbcly.core.ResultItem;
import com.jdbcly.engine.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Date: 7/10/2020
 */
public class EngineTests extends TestBase {

    @Test
    void rowSetLimitOffset() throws Exception {
        // limit only
        RowSet rowSet = createRowSet();
        rowSet.visit(new RowSetLimitOffsetVisitor(createStatement("SELECT * FROM TABLE LIMIT 3")));
        assertTrue(rowSet.size() == 3, "Incorrect number of rows returned.");
        rowSet.next();
        assertTrue(rowSet.getValue("Country").equals("Germany"));
        assertTrue(rowSet.getValue("Unit Price").equals(77.0));

        // limit offset
        rowSet = createRowSet();
        rowSet.visit(new RowSetLimitOffsetVisitor(createStatement("SELECT * FROM TABLE LIMIT 3 OFFSET 1")));
        assertTrue(rowSet.size() == 3, "Incorrect number of rows returned.");
        rowSet.next();
        assertTrue(rowSet.getValue("Country").equals("Denmark"));
        assertTrue(rowSet.getValue("Unit Price").equals(71.0));

        // limit offset -> limit + offset > rows size
        rowSet = createRowSet();
        rowSet.visit(new RowSetLimitOffsetVisitor(createStatement("SELECT * FROM TABLE LIMIT 3 OFFSET 3")));
        assertTrue(rowSet.size() == 2, "Incorrect number of rows returned.");
        rowSet.next();
        assertTrue(rowSet.getValue("Country").equals("Turkey"));
        assertTrue(rowSet.getValue("Unit Price").equals(45.22));
    }

    @Test
    void rowSetColumnsProjection() throws Exception {
        // *
        RowSet rowSet = createRowSet();
        rowSet.visit(new RowSetProjectionVisitor(createStatement("SELECT * FROM TABLE")));
        assertTrue(rowSet.getColumnCount() == 4, "Incorrect number of columns returned.");

        // columns projection
        rowSet = createRowSet();
        rowSet.visit(new RowSetProjectionVisitor(createStatement("SELECT Region, `Unit Price` FROM TABLE")));
        assertTrue(rowSet.getColumnCount() == 2, "Incorrect number of columns returned.");
    }

    @Test
    void rowSetOrderBy() throws Exception {
        // single order by, default direction
        RowSet rowSet = createRowSet();
        rowSet.visit(new RowSetOrderByVisitor(createStatement("SELECT * FROM TABLE ORDER BY Country")));
        rowSet.next();
        assertTrue(rowSet.getString("Country").equals("Azebaijan"));
        assertTrue(rowSet.getValue("Unit Price").equals(45.22));

        // single order by, desc
        rowSet = createRowSet();
        rowSet.visit(new RowSetOrderByVisitor(createStatement("SELECT * FROM TABLE ORDER BY Country DESC")));
        rowSet.next();
        assertTrue(rowSet.getString("Country").equals("Turkey"));
        assertTrue(rowSet.getValue("Unit Price").equals(45.22));

        // multiple order by, different directions
        rowSet = createRowSet();
        rowSet.visit(new RowSetOrderByVisitor(createStatement("SELECT * FROM TABLE ORDER BY Region DESC, Country ASC")));
        rowSet.next();
        assertTrue(rowSet.getString("Region").equals("Middle East"));
        assertTrue(rowSet.getString("Country").equals("Azebaijan"));
        assertTrue(rowSet.getValue("Unit Price").equals(45.22));
        rowSet.next();
        assertTrue(rowSet.getString("Region").equals("Middle East"));
        assertTrue(rowSet.getString("Country").equals("Jordan"));
        assertTrue(rowSet.getValue("Unit Price").equals(12.3));
    }

    @Test
    void rowSetCriteria() throws Exception {
        // single string equality
        RowSet rowSet = createRowSet();
        rowSet.visit(new RowSetCriteriaVisitor(createStatement("SELECT * FROM TABLE WHERE Country = 'Germany'")));
        assertTrue(rowSet.size() == 1);
        rowSet.next();
        assertTrue(rowSet.getString("Country").equals("Germany"));
        assertTrue(rowSet.getValue("Unit Price").equals(77.0));

        // multiple AND
        rowSet = createRowSet();
        rowSet.visit(new RowSetCriteriaVisitor(createStatement("SELECT * FROM TABLE WHERE Country = 'Germany' AND `Unit Price` = 20")));
        assertTrue(rowSet.size() == 0);
        rowSet = createRowSet();
        rowSet.visit(new RowSetCriteriaVisitor(createStatement("SELECT * FROM TABLE WHERE Country = 'Germany' AND `Unit Price` = 77")));
        assertTrue(rowSet.size() == 1);
        rowSet.next();
        assertTrue(rowSet.getString("Country").equals("Germany"));
        assertTrue(rowSet.getValue("Unit Price").equals(77.0));

        // multiple OR
        rowSet = createRowSet();
        rowSet.visit(new RowSetCriteriaVisitor(createStatement("SELECT * FROM TABLE WHERE Country = 'Germany' OR `Unit Price` != 45.22")));
        assertTrue(rowSet.size() == 3);

        // multiple mix
        rowSet = createRowSet();
        rowSet.visit(new RowSetCriteriaVisitor(createStatement("SELECT * FROM TABLE WHERE Country = 'Germany' OR (`Unit Price` >= 66.22 AND Country != 'Denmark')")));
        assertTrue(rowSet.size() == 1);
    }

    @Test
    void rowSetGroupBy() throws Exception {
        // single
        RowSet rowSet = createRowSet();
        rowSet.visit(new RowSetAggregateVisitor(createStatement("SELECT Region FROM TABLE GROUP BY Region")));
        assertTrue(rowSet.size() == 2);
        rowSet.next();
        assertTrue(rowSet.getString("Region").equals("Europe"));
        rowSet.next();
        assertTrue(rowSet.getString("Region").equals("Middle East"));

        // double, Azerbaijan and Turkey are merged
        rowSet = createRowSet();
        rowSet.visit(new RowSetAggregateVisitor(createStatement("SELECT Region FROM TABLE GROUP BY Region, `Unit Price`")));
        assertTrue(rowSet.size() == 4);
    }

    @Test
    void rowSetAggregateFunctions() throws Exception {
        // no grouping
        RowSet rowSet = createRowSet("Min");
        rowSet.visit(new RowSetAggregateVisitor(createStatement("SELECT MIN(`Unit Price`) FROM TABLE")));
        assertTrue(rowSet.size() == 5);
        rowSet.next();
        assertTrue(rowSet.getValue("Min").equals(12.3));

        rowSet = createRowSet("Max");
        rowSet.visit(new RowSetAggregateVisitor(createStatement("SELECT MAX(`Unit Price`) FROM TABLE")));
        rowSet.next();
        assertTrue(rowSet.getValue("Max").equals(77.0));

        rowSet = createRowSet("Sum");
        rowSet.visit(new RowSetAggregateVisitor(createStatement("SELECT SUM(`Unit Price`) FROM TABLE")));
        rowSet.next();
        assertTrue(rowSet.getValue("Sum").equals(250.74));

        rowSet = createRowSet("Count");
        rowSet.visit(new RowSetAggregateVisitor(createStatement("SELECT COUNT(`Unit Price`) FROM TABLE")));
        rowSet.next();
        assertTrue(rowSet.getValue("Count").equals(5L));

        // grouping by region
        rowSet = createRowSet("Min");
        rowSet.visit(new RowSetAggregateVisitor(createStatement("SELECT MIN(`Unit Price`) FROM TABLE GROUP BY Region")));
        assertTrue(rowSet.size() == 2);
        rowSet.next();
        assertTrue(rowSet.getValue("Min").equals(71.0));
        rowSet.next();
        assertTrue(rowSet.getValue("Min").equals(12.3));

        rowSet = createRowSet("Count");
        rowSet.visit(new RowSetAggregateVisitor(createStatement("SELECT COUNT(`Unit Price`) FROM TABLE GROUP BY Region")));
        assertTrue(rowSet.size() == 2);
        rowSet.next();
        assertTrue(rowSet.getValue("Count").equals(2L));
        rowSet.next();
        assertTrue(rowSet.getValue("Count").equals(3L));
    }

    private RowSet createRowSet(String... functions) {
        List<ResultItem<?>> rows = new ArrayList<>();
        rows.add(createResultItem("Europe", "Germany", 77.0, 140.0));
        rows.add(createResultItem("Europe", "Denmark", 71.0, 120.4));
        rows.add(createResultItem("Middle East", "Azebaijan", 45.22, 90.0));
        rows.add(createResultItem("Middle East", "Turkey", 45.22, 88.0));
        rows.add(createResultItem("Middle East", "Jordan", 12.3, 16.0));

        for (String function : functions) {
            for (ResultItem<?> row : rows) {
                row.put(function, null);
            }
        }

        List<String> labels = new ArrayList<>();
        labels.addAll(Arrays.asList(functions));
        labels.addAll(Arrays.asList("Region", "Country", "Unit Price", "Total Cost"));
        return RowSet.create(labels.toArray(new String[0]), rows);
    }

    private RowSet createRowSet() {
        return createRowSet(new String[0]);
    }

    private ResultItem<?> createResultItem(String region, String country, double unitPrice, double totalCost) {
        ResultItem<Comparable> item = new ResultItem<>();
        item.put("Region", region);
        item.put("Country", country);
        item.put("Unit Price", unitPrice);
        item.put("Total Cost", totalCost);
        return item;
    }
}
