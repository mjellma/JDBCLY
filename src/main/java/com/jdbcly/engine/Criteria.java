package com.jdbcly.engine;

import com.jdbcly.core.SqlColumn;

/**
 * Date: 6/28/2020
 */
abstract public class Criteria {
    protected Criteria left, right;
    protected CriteriaEvaluator evaluator;

    private Criteria() {
    }

    protected Criteria(Criteria left, Criteria right, CriteriaEvaluator evaluator) {
        this.left = left;
        this.right = right;
        this.evaluator = evaluator;
    }

    public abstract Comparable evaluate();

    public static class Column extends Criteria {
        private SqlColumn column;

        public Column(SqlColumn column) {
            this.column = column;
        }

        public SqlColumn getColumn() {
            return column;
        }

        @Override
        public Comparable evaluate() {
            return column.getValue();
        }

        @Override
        public String toString() {
            return "COLUMN";
        }
    }

    public static class Value extends Criteria {
        private Comparable value;

        public Value(Comparable value) {
            this.value = value;
        }

        @Override
        public Comparable evaluate() {
            return value;
        }

        @Override
        public String toString() {
            return "VALUE";
        }
    }

    public static abstract class BinaryCriteria extends Criteria {
        public BinaryCriteria(Criteria left, Criteria right, CriteriaEvaluator evaluator) {
            super(left, right, evaluator);
        }

        @Override
        public Boolean evaluate() {
            return evaluator.evaluate(left.evaluate(), right.evaluate());
        }
    }

    public static class Comparison extends BinaryCriteria {
        public Comparison(Criteria left, Criteria right, CriteriaEvaluator evaluator) {
            super(left, right, evaluator);
        }

        @Override
        public String toString() {
            return "COMPARISON";
        }
    }

    public static class And extends BinaryCriteria {
        public And(Criteria left, Criteria right) {
            super(left, right, CriteriaEvaluator.AND);
        }

        @Override
        public String toString() {
            return "AND";
        }
    }

    public static class Or extends BinaryCriteria {
        public Or(Criteria left, Criteria right) {
            super(left, right, CriteriaEvaluator.OR);
        }

        @Override
        public String toString() {
            return "OR";
        }
    }
}
