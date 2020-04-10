package org.vaadin.haijian;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;

/**
 * 
 * @author Krunoslav Magazin
 * Oct 5, 2019
 */
public class CellValueTypeConverter {

    public void setValue(Cell cell, LocalDateTime localDateTime) {
        cell.setCellValue(convertLocalDateTimeToDate(localDateTime));
    }

    public void setValue(Cell cell, LocalDate localDateTime) {
        cell.setCellValue(convertLocalDateToDate(localDateTime));
    }

    public void setValue(Cell cell, Calendar localDateTime) {
        cell.setCellValue(localDateTime.getTime());
    }

    public Date convertLocalDateToDate(LocalDate dateToConvert) {
        return Date.from(dateToConvert.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public Date convertLocalDateTimeToDate(LocalDateTime dateToConvert) {
        return Date.from(dateToConvert.atZone(ZoneId.systemDefault()).toInstant());
    }

}
