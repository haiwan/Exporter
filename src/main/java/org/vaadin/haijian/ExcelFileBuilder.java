package org.vaadin.haijian;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.grid.Grid;

/**
 * 
 * Modified Oct 5, 2019 by Krunoslav Magazin - added data formats and type
 * conversion
 * 
 */
public class ExcelFileBuilder<T> extends FileBuilder<T> {
    private static final String EXCEL_FILE_EXTENSION = ".xls";

    private Workbook workbook;
    private Sheet sheet;
    private int rowNr;
    private int colNr;
    private Row row;
    private Cell cell;
    private CellStyle boldStyle;

    private DataFormats dataFormats;
    private CellValueTypeConverter converter;
    private WorkbookStyles styles;

    ExcelFileBuilder(Grid<T> grid, Map<Grid.Column<T>, String> columnHeaders) {
        super(grid, columnHeaders);
        this.dataFormats = new TypeDataFormats();
        this.converter = new CellValueTypeConverter();
    }

    ExcelFileBuilder(Grid<T> grid, Map<Grid.Column<T>, String> columnHeaders, DataFormats dataFormats) {
        this(grid, columnHeaders);
        this.dataFormats = dataFormats;
    }

    ExcelFileBuilder(Grid<T> grid, Map<Grid.Column<T>, String> columnHeaders, CellValueTypeConverter converter) {
        this(grid, columnHeaders);
        this.converter = converter;
    }

    ExcelFileBuilder(Grid<T> grid, Map<Grid.Column<T>, String> columnHeaders, DataFormats dataFormats,
            CellValueTypeConverter converter) {
        this(grid, columnHeaders, dataFormats);
        this.converter = converter;
    }

    @Override
    public String getFileExtension() {
        return EXCEL_FILE_EXTENSION;
    }

    @Override
    protected void writeToFile() {
        try {
            workbook.write(new FileOutputStream(file));
        } catch (Exception e) {
            LoggerFactory.getLogger(this.getClass()).error("Error writing excel file", e);
        }
    }

    @Override
    protected void onNewRow() {
        row = sheet.createRow(rowNr);
        rowNr++;
        colNr = 0;
    }

    @Override
    protected void onNewCell() {
        cell = row.createCell(colNr);
        colNr++;
    }

    @Override
    protected void buildCell(Object value) {
        if (value == null) {
            cell.setCellType(Cell.CELL_TYPE_BLANK);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
            cell.setCellType(Cell.CELL_TYPE_BOOLEAN);
        } else if (value instanceof Number) {
            buildNumericCell((Number) value);
        } else if (value instanceof Calendar) {
            converter.setValue(cell, (Calendar) value);
        } else if (value instanceof Date) {
            Date date = (Date) value;
            cell.setCellValue(date);
        } else if (value instanceof LocalDate) {
            converter.setValue(cell, (LocalDate) value);
        } else if (value instanceof LocalDateTime) {
            converter.setValue(cell, (LocalDateTime) value);
        } else {
            cell.setCellValue(value.toString());
            cell.setCellType(Cell.CELL_TYPE_STRING);
        }

        styles.setStyle(cell, value.getClass());

    }

    protected void buildNumericCell(Number value) {
        cell.setCellType(Cell.CELL_TYPE_NUMERIC);

        if (value instanceof BigDecimal) {
            cell.setCellValue(((BigDecimal) value).doubleValue());
        } else {
            cell.setCellValue(value.doubleValue());
        }

        styles.setStyle(cell, value.getClass());

    }

    @Override
    protected void buildColumnHeaderCell(String header) {
        buildCell(header);
        cell.setCellStyle(getBoldStyle());
    }

    private CellStyle getBoldStyle() {
        if (boldStyle == null) {
            Font bold = workbook.createFont();
            bold.setBoldweight(Font.BOLDWEIGHT_BOLD);

            boldStyle = workbook.createCellStyle();
            boldStyle.setFont(bold);
        }
        return boldStyle;
    }

    @Override
    protected void buildFooter() {
        for (int i = 0; i < getNumberOfColumns(); i++) {
            sheet.autoSizeColumn(i);
        }
    }

    @Override
    protected void resetContent() {
        workbook = new HSSFWorkbook();
        sheet = workbook.createSheet();
        colNr = 0;
        rowNr = 0;
        row = null;
        cell = null;
        boldStyle = null;

        createCellStyleDataFormats();
    }

    private void createCellStyleDataFormats() {
        styles = new WorkbookStyles(workbook, dataFormats);
    }
}
