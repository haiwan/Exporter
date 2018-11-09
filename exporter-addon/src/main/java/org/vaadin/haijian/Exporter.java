package org.vaadin.haijian;

import com.vaadin.ui.Grid;

import java.io.InputStream;

public class Exporter {

    private Exporter(){}

    public static <T> InputStream exportAsExcel(Grid<T> grid){
        return new ExcelFileBuilder<>(grid).build();
    }

    public static <T> InputStream exportAsCSV(Grid<T> grid){
        return new CSVFileBuilder<>(grid).build();
    }
}
