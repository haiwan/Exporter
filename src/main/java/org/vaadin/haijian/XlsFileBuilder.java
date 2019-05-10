package org.vaadin.haijian;

import com.vaadin.flow.component.grid.Grid;

public class XlsFileBuilder<T> extends ExcelFileBuilder<T>{
    public XlsFileBuilder(Grid<T> grid) {
        super(grid);
    }
}
