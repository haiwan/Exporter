package org.vaadin.haijian.filegenerator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.vaadin.data.Container;
import com.vaadin.ui.Table;

public class CSVFileBuilder extends FileBuilder {
    private FileWriter writer;
    private int rowNr;
    private int colNr;

    public CSVFileBuilder(Container container) {
        super(container);
    }
    
    public CSVFileBuilder(Table table) {
        super(table);
    }

    @Override
    protected void resetContent() {
        try {
            colNr = 0;
            rowNr = 0;
            writer = new FileWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void buildCell(Object modelValue, Object presentationValue) {
        try {
        	if(modelValue == null){
        		writer.append("");
        	}else if(modelValue instanceof Calendar){
        		Calendar calendar = (Calendar) modelValue;
        		writer.append(formatDate(calendar.getTime()));
        	}else if(modelValue instanceof Date){
        		writer.append(formatDate((Date) modelValue));
        	}else {
        		writer.append(presentationValue.toString());
        	}
        } catch (IOException e) {
            e.printStackTrace();
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
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewRow() {
        if (rowNr > 0) {
            try {
                writer.append("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        rowNr++;
        colNr = 0;
    }

    @Override
    protected void onNewCell() {
        if (colNr > 0 && colNr < getNumberofColumns()) {
            try {
                writer.append(",");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        colNr++;
    }

    @Override
    protected void buildColumnHeaderCell(String header) {
        try {
            writer.append(header);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
