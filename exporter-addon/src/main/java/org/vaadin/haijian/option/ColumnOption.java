package org.vaadin.haijian.option;

import java.util.Optional;

import com.vaadin.data.ValueProvider;
import com.vaadin.server.SerializableFunction;

public class ColumnOption {
    private String columnName;
    private boolean upperCase = false;

    private ValueProvider valueProvider;
    private SerializableFunction<String, String> headerProvider;

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

    public <ITEM, PROPERTY> void valueProviderFunction(ValueProvider<ITEM, PROPERTY> valueProvider) {
        this.valueProvider = valueProvider;
    }

    public <ITEM, PROPERTY> void headerProviderFunction(SerializableFunction<String, String> headerProvider) {
        this.headerProvider = headerProvider;
    }

    @SuppressWarnings("unchecked")
    public <ITEM, PROPERTY> Optional<ValueProvider<ITEM, PROPERTY>> getValueProviderFunction() {
        return Optional.ofNullable(valueProvider);
    }

    public Optional<SerializableFunction<String, String>> getHeaderProviderFunction() {
        return Optional.ofNullable(headerProvider);
    }
}
