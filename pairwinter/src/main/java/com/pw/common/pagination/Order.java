package com.pw.common.pagination;

/**
 * Created with IntelliJ IDEA.
 * User: damon
 * Date: 4/15/14
 * Time: 5:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class Order {
    private String column;
    private Direction direction;

    public Order() {
    }

    public Order(String column, Direction direction) {
        this.column = column;
        this.direction = direction;
    }

    @Override
    public String toString() {
        return column + " " + this.direction;
    }

    public enum Direction{
        ASC,DESC
    }
}
