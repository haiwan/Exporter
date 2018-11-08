package org.vaadin.haijian;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.server.InputStreamFactory;

public class Exporter {

    private Exporter(){}

    public static <T> InputStreamFactory exportAsExcel(Grid<T> grid){
        return () -> new ExcelFileBuilder<>(grid).build();
    }

    public static <T> InputStreamFactory exportAsCSV(Grid<T> grid){
        return () -> new CSVFileBuilder<>(grid).build();
    }
}
