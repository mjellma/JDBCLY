import com.jdbcly.core.SelectStatement;
import com.jdbcly.engine.Criteria;
import org.junit.jupiter.api.Test;

/**
 * Date: 7/10/2020
 */
public class StatementTests extends TestBase {

    @Test
    void statementSelectStarNoException() throws Exception {
        createStatement("SELECT * FROM TABLE");
    }

    @Test
    void statementFunctionInProjection() throws Exception {
        SelectStatement statement = createStatement("SELECT COUNT(*) FROM TABLE");
        assertTrue(statement.getProjection().size() == 1);
        assertTrue(statement.getProjection().get(0).getName().equalsIgnoreCase("Count"));
    }

    @Test
    void statementColumnProjectionLimitOffset() throws Exception {
        SelectStatement statement = createStatement("SELECT Col1, Col2 FROM TABLE Limit 4");
        assertTrue(statement.getProjection().size() == 2);
        assertTrue(statement.getProjection().get(0).getName().equalsIgnoreCase("Col1"));
        assertTrue(statement.getProjection().get(1).getName().equalsIgnoreCase("Col2"));
        assertTrue(statement.getLimit() == 4);
        assertTrue(statement.getOffset() == 0);

        statement = createStatement("SELECT Col1, Col2 FROM TABLE Limit 4 OFFSET 55");
        assertTrue(statement.getLimit() == 4);
        assertTrue(statement.getOffset() == 55);
    }

    @Test
    void statementCriteria() throws Exception {
        SelectStatement statement = createStatement("SELECT * FROM TABLE WHERE Country = 'Germany' OR (`Unit Price` >= 66.22 AND Country != 'Denmark')");
        assertTrue(statement.getCriteria() != null);
        assertTrue(statement.getCriteria() instanceof Criteria.Or);
        Criteria.Or or = (Criteria.Or) statement.getCriteria();
        assertTrue(or.getLeft() instanceof Criteria.Comparison);
        assertTrue(or.getRight() instanceof Criteria.And);
        Criteria.And and = (Criteria.And) or.getRight();
        assertTrue(and.getLeft() instanceof Criteria.Comparison);
        assertTrue(and.getRight() instanceof Criteria.Comparison);
    }

    @Test
    void statementColumnOrderBy() throws Exception {
        SelectStatement statement = createStatement("SELECT * FROM TABLE ORDER BY Col1, Col2 DESC, Col3 ASC");
        assertTrue(statement.getOrderBy().size() == 3);
        assertTrue(statement.getOrderBy().get(0).getExpression().getName().equalsIgnoreCase("Col1"));
        assertTrue(statement.getOrderBy().get(0).isAsc());
        assertTrue(statement.getOrderBy().get(1).getExpression().getName().equalsIgnoreCase("Col2"));
        assertTrue(!statement.getOrderBy().get(1).isAsc());
        assertTrue(statement.getOrderBy().get(2).getExpression().getName().equalsIgnoreCase("Col3"));
        assertTrue(statement.getOrderBy().get(2).isAsc());
    }

    @Test
    void statementColumnGroupBy() throws Exception {
        SelectStatement statement = createStatement("SELECT Col1, Col2 FROM TABLE GROUP BY Col1, Col2");
        assertTrue(statement.getGroupBy().size() == 2);
        assertTrue(statement.getGroupBy().get(0).getExpression().getName().equalsIgnoreCase("Col1"));
        assertTrue(statement.getGroupBy().get(1).getExpression().getName().equalsIgnoreCase("Col2"));
    }

    @Test
    void unGroupedColumnsInProjection() throws Exception {
        boolean asserted = false;
        try {
            createStatement("SELECT * FROM TABLE GROUP BY Region, `Unit Price`");
        } catch (Exception e) {
            asserted = true;
        }
        assertTrue(asserted, "Should have thrown exception");

        asserted = false;
        try {
            createStatement("SELECT Region, Country FROM TABLE GROUP BY Region, `Unit Price`");
        } catch (Exception e) {
            asserted = true;
        }
        assertTrue(asserted, "Should have thrown exception");
    }
}
