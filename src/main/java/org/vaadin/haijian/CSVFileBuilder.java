package org.vaadin.haijian;

import com.vaadin.flow.component.grid.Grid;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class CSVFileBuilder<T> extends FileBuilder<T> {
    private FileWriter writer;
    private int rowNr;
    private int colNr;

    CSVFileBuilder(Grid<T> grid, Map<Grid.Column<T>, String> columnHeaders) {
        super(grid, columnHeaders);
    }

    @Override
    protected void resetContent() {
        try {
            colNr = 0;
            rowNr = 0;
            writer = new FileWriter(file);
        } catch (IOException e) {
            throw new ExporterException("Unable to reset content", e);
        }
    }

    @Override
    protected void buildCell(Object value) {
        try {
            if(value == null){
                writer.append("");
            }else if (value.toString().contains(",")) {
                writer.append("\"").append(value.toString()).append("\"");
            }else {
                writer.append(value.toString());
            }
        } catch (IOException e) {
            LoggerFactory.getLogger(this.getClass()).error("Error building cell "+value, e);
        }
    }

    @Override
    protected String getFileExtension() {
        return ".csv";
    }

    @Override
    protected void writeToFile() {
        try {
            writer.flush();
        } catch (IOException e) {
            throw new ExporterException("Failed to write to file", e);
        } finally {
            cleanupResource();
        }
    }

    private void cleanupResource(){
        try{
            if(writer!=null){
                writer.close();
            }
        }catch (IOException e){
            LoggerFactory.getLogger(this.getClass()).error("Unable to close the writer for CSV file", e);
        }
    }

    @Override
    protected void onNewRow() {
        if (rowNr > 0) {
            try {
                writer.append("\n");
            } catch (IOException e) {
                throw new ExporterException("Unable to create a new line", e);
            }
        }
        rowNr++;
        colNr = 0;
    }

    @Override
    protected void onNewCell() {
        if (colNr > 0 && colNr < getNumberOfColumns()) {
            try {
                writer.append(",");
            } catch (IOException e) {
                throw new ExporterException("Unable to create a new cell", e);
            }
        }
        colNr++;
    }

    @Override
    protected void buildColumnHeaderCell(String header) {
        try {
            writer.append(header);
        } catch (IOException e) {
            throw new ExporterException("Unable to build a header cell", e);
        }
    }

}
