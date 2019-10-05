package org.vaadin.haijian;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Workbook;

public class WorkbookStyles {

    private final Map<Class<?>, CellStyle> stylesWithDataFormats;
    private CreationHelper createHelper;
    private Workbook workbook;

    public WorkbookStyles(Workbook workbook, DataFormats dataFormats) {
        this.workbook = workbook;
        this.stylesWithDataFormats = new HashMap<Class<?>, CellStyle>();

        createHelper = workbook.getCreationHelper();

        dataFormats.getTypeFormatsMap().forEach(this::createCellStyeWithDataFormat);
    }

    private void createCellStyeWithDataFormat(Class<?> clazz, String format) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat(format));
        stylesWithDataFormats.put(clazz, cellStyle);
    }

    public void setStyle(Cell cell, Class<?> clazz) {
        if (stylesWithDataFormats.containsKey(clazz)) {
            cell.setCellStyle(stylesWithDataFormats.get(clazz));
        }
    }
}
