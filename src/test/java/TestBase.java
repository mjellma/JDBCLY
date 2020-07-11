import com.jdbcly.core.SelectStatement;

/**
 * Date: 7/10/2020
 */
public class TestBase {

    void assertTrue(boolean b) {
        if (!b) {
            throw new RuntimeException("Condition failed.");
        }
    }

    void assertTrue(boolean b, String msg) {
        if (!b) {
            throw new RuntimeException("Condition failed: " + msg);
        }
    }

    SelectStatement createStatement(String sql) throws Exception {
        return SelectStatement.from(sql);
    }

    static class ExceptionShouldHaveBeenThrownException extends RuntimeException {
        public ExceptionShouldHaveBeenThrownException() {
            super("Should have thrown exception.");
        }
    }
}
