import com.jdbcly.core.ESqlDataType;
import com.jdbcly.core.ResultItem;
import com.jdbcly.utils.JdbcUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 7/11/2020
 */
public class TypeDetectionTests extends TestBase {

    @Test
    void numberType() throws Exception {
        ESqlDataType type = JdbcUtils.determineNumberType(1);
        assertTrue(type == ESqlDataType.INTEGER);

        type = JdbcUtils.determineNumberType(1L);
        assertTrue(type == ESqlDataType.INTEGER);

        type = JdbcUtils.determineNumberType(1.00);
        assertTrue(type == ESqlDataType.INTEGER);

        type = JdbcUtils.determineNumberType(1.2);
        assertTrue(type == ESqlDataType.DOUBLE);

        type = JdbcUtils.determineNumberType(1.2f);
        assertTrue(type == ESqlDataType.DOUBLE);

        type = JdbcUtils.determineNumberType(145641564145L);
        assertTrue(type == ESqlDataType.DOUBLE); // TODO: 7/11/2020 temporary, until long is supported
    }

    @Test
    void stringNumberType() throws Exception {
        ESqlDataType type = JdbcUtils.determineType("1");
        assertTrue(type == ESqlDataType.INTEGER);

        type = JdbcUtils.determineType("1L");
        assertTrue(type == ESqlDataType.INTEGER);

        type = JdbcUtils.determineType("1.00");
        assertTrue(type == ESqlDataType.INTEGER);

        type = JdbcUtils.determineType("1.2");
        assertTrue(type == ESqlDataType.DOUBLE);

        type = JdbcUtils.determineType("1.2f");
        assertTrue(type == ESqlDataType.DOUBLE);

        type = JdbcUtils.determineType("145641564145");
        assertTrue(type == ESqlDataType.DOUBLE); // TODO: 7/11/2020 temporary, until long is supported
    }

    @Test
    void stringType() throws Exception {
        ESqlDataType type = JdbcUtils.determineType("1k");
        assertTrue(type == ESqlDataType.VARCHAR);

        type = JdbcUtils.determineType("ABC");
        assertTrue(type == ESqlDataType.VARCHAR);
    }

    @Test
    void basicTypeScan() throws Exception {
        List<ResultItem<?>> rows = createRows().subList(0, 1);
        ResultItem<ESqlDataType> types = JdbcUtils.determineColumnTypes(rows);
        assertTrue(types.get("Region") == ESqlDataType.VARCHAR);
        assertTrue(types.get("Units Sold") == ESqlDataType.DOUBLE);
        assertTrue(types.get("Total Cost") == ESqlDataType.INTEGER);
    }

    @Test
    void cascadingTypeScan() throws Exception {
        List<ResultItem<?>> rows = createRows().subList(0, 2);
        ResultItem<ESqlDataType> types = JdbcUtils.determineColumnTypes(rows);

        assertTrue(types.get("Region") == ESqlDataType.VARCHAR);
        assertTrue(types.get("Units Sold") == ESqlDataType.DOUBLE);
        assertTrue(types.get("Total Cost") == ESqlDataType.DOUBLE);


        rows = createRows();
        types = JdbcUtils.determineColumnTypes(rows);

        assertTrue(types.get("Region") == ESqlDataType.VARCHAR);
        assertTrue(types.get("Units Sold") == ESqlDataType.VARCHAR);
        assertTrue(types.get("Total Cost") == ESqlDataType.DOUBLE);
    }

    private List<ResultItem<?>> createRows() {
        List<ResultItem<?>> rows = new ArrayList<>();
        rows.add(createResultItem("Europe", 77.1, 140));
        rows.add(createResultItem("Europe", 71.0, 120.3));
        rows.add(createResultItem("Middle East", "test", 90));

        return rows;
    }

    private ResultItem<?> createResultItem(Comparable region, Comparable unitSold, Comparable totalCost) {
        ResultItem<Comparable> item = new ResultItem<>();
        item.put("Region", region);
        item.put("Units Sold", unitSold);
        item.put("Total Cost", totalCost);
        return item;
    }
}
