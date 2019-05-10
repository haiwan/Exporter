package org.vaadin.haijian;

import com.vaadin.ui.Grid;

public class XlsFileBuilder<T> extends ExcelFileBuilder<T>{
    XlsFileBuilder(Grid<T> grid) {
        super(grid);
    }
}
