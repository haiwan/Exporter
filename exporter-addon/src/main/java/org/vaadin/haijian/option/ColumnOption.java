package org.vaadin.haijian.option;

public class ColumnOption {
    private String columnName;
    private boolean upperCase = false;

    public String getColumnName() {
        return columnName;
    }

    public ColumnOption columnName(String columnName) {
        this.columnName = columnName;
        return this;
    }

    public boolean isUpperCase() {
        return upperCase;
    }

    public ColumnOption toUpperCase() {
        this.upperCase = true;
        return this;
    }
}
