package org.vaadin.haijian;

import com.vaadin.ui.Grid;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.vaadin.haijian.option.ExporterOption;

public class XlsxFileBuilder<T> extends ExcelFileBuilder<T>{
    private static final String EXCEL_FILE_EXTENSION = ".xlsx";

    XlsxFileBuilder(Grid<T> grid, ExporterOption option) {
        super(grid, option);
    }

    @Override
    public String getFileExtension() {
        return EXCEL_FILE_EXTENSION;
    }

    @Override
    protected Workbook createWorkbook() {
        return new XSSFWorkbook();
    }
}
