package com.jdbcly.engine;

import com.jdbcly.core.ESqlDataType;
import com.jdbcly.core.Utils;

/**
 * Date: 6/28/2020
 */
public interface CriteriaEvaluator {

    boolean evaluateUnsafe(Comparable o1, Comparable o2);

    default boolean evaluate(Comparable o1, Comparable o2) {
        try {
            return evaluateUnsafe(adapt(o1), adapt(o2));
        } catch (ClassCastException e) {
            throw new RuntimeException("Values cannot be compared: " + Utils.toString(o1) + ", " + Utils.toString(o2));
        }
    }

    default Comparable adapt(Comparable o) {
        if (o instanceof String) {
            return ((String) o).toLowerCase();
        } else if (o instanceof Number) {
            return (Comparable) Utils.convert(o, ESqlDataType.DOUBLE);
        }
        return o;
    }

    CriteriaEvaluator AND = new CriteriaEvaluator() {
        @Override
        public boolean evaluateUnsafe(Comparable o1, Comparable o2) {
            return o1.compareTo(true) == 0 && o2.compareTo(true) == 0;
        }

        @Override
        public String toString() {
            return "AND";
        }
    };

    CriteriaEvaluator OR = new CriteriaEvaluator() {
        @Override
        public boolean evaluateUnsafe(Comparable o1, Comparable o2) {
            return o1.compareTo(true) == 0 || o2.compareTo(true) == 0;
        }

        @Override
        public String toString() {
            return "OR";
        }
    };

    CriteriaEvaluator EQUAL = new CriteriaEvaluator() {
        @Override
        public boolean evaluateUnsafe(Comparable o1, Comparable o2) {
            return o1.compareTo(o2) == 0;
        }

        @Override
        public String toString() {
            return "EQUAL";
        }
    };

    CriteriaEvaluator NOT_EQUAL = new CriteriaEvaluator() {
        @Override
        public boolean evaluateUnsafe(Comparable o1, Comparable o2) {
            return o1.compareTo(o2) != 0;
        }

        @Override
        public String toString() {
            return "NOT_EQUAL";
        }
    };

    CriteriaEvaluator SMALLER = new CriteriaEvaluator() {
        @Override
        public boolean evaluateUnsafe(Comparable o1, Comparable o2) {
            return o1.compareTo(o2) < 0;
        }

        @Override
        public String toString() {
            return "SMALLER";
        }
    };

    CriteriaEvaluator SMALLER_EQUAL = new CriteriaEvaluator() {
        @Override
        public boolean evaluateUnsafe(Comparable o1, Comparable o2) {
            return o1.compareTo(o2) <= 0;
        }

        @Override
        public String toString() {
            return "SMALLER_EQUAL";
        }
    };

    CriteriaEvaluator BIGGER = new CriteriaEvaluator() {
        @Override
        public boolean evaluateUnsafe(Comparable o1, Comparable o2) {
            return o1.compareTo(o2) > 0;
        }

        @Override
        public String toString() {
            return "BIGGER";
        }
    };

    CriteriaEvaluator BIGGER_EQUAL = new CriteriaEvaluator() {
        @Override
        public boolean evaluateUnsafe(Comparable o1, Comparable o2) {
            return o1.compareTo(o2) >= 0;
        }

        @Override
        public String toString() {
            return "BIGGER_EQUAL";
        }
    };
}