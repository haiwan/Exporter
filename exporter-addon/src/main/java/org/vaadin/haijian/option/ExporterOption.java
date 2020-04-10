package org.vaadin.haijian.option;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.ui.Grid.Column;

public class ExporterOption {
    private Map<String, ColumnOption> columnOptions;

    /**
     * If does not exist empty {@linkplain ColumnOption option} will be created for given ID.
     * 
     * @param columnKey use column {@link Column#getId() ID}
     */
    public ColumnOption getColumnOption(String columnKey) {
        getColumnOptions().putIfAbsent(columnKey, new ColumnOption());
        return getColumnOptions().get(columnKey);
    }

    /**
     * @return Column mapped by {@link Column#getId() ID}
     */
    private Map<String, ColumnOption> getColumnOptions() {
        if (columnOptions == null) {
            columnOptions = new HashMap<>();
        }
        return columnOptions;
    }
}
