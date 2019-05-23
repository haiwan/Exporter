package org.vaadin.haijian;

import com.vaadin.ui.Grid;
import org.vaadin.haijian.option.ExporterOption;

public class XlsFileBuilder<T> extends ExcelFileBuilder<T>{
    XlsFileBuilder(Grid<T> grid, ExporterOption option) {
        super(grid, option);
    }
}
