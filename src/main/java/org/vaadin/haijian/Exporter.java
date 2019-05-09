package org.vaadin.haijian;

import java.util.Map;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.server.InputStreamFactory;

public class Exporter {

    private Exporter(){}

    public static <T> InputStreamFactory exportAsExcel(Grid<T> grid, Map<Grid.Column<T>, String> columnHeaders){
        return new ExcelFileBuilder<>(grid, columnHeaders)::build;
    }

    public static <T> InputStreamFactory exportAsCSV(Grid<T> grid, Map<Grid.Column<T>, String> columnHeaders){
        return new CSVFileBuilder<>(grid, columnHeaders)::build;
    }
}
