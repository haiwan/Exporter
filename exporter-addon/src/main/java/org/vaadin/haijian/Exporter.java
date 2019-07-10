package org.vaadin.haijian;

import com.vaadin.ui.Grid;
import org.vaadin.haijian.option.ExporterOption;

import java.io.InputStream;

public class Exporter {

    private Exporter(){}

    public static <T> InputStream exportAsExcel(Grid<T> grid){
        return exportAsExcel(grid, new ExporterOption());
    }

    public static <T> InputStream exportAsXls(Grid<T> grid){
        return exportAsXls(grid, new ExporterOption());
    }

    public static <T> InputStream exportAsCSV(Grid<T> grid){
        return exportAsCSV(grid, new ExporterOption());
    }

    public static <T> InputStream exportAsExcel(Grid<T> grid, ExporterOption option){
        return new XlsxFileBuilder<>(grid, option).build();
    }

    public static <T> InputStream exportAsXls(Grid<T> grid, ExporterOption option){
        return new XlsFileBuilder<>(grid, option).build();
    }

    public static <T> InputStream exportAsCSV(Grid<T> grid, ExporterOption option){
        return new CSVFileBuilder<>(grid, option).build();
    }
}
