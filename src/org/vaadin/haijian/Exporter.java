package org.vaadin.haijian;

import org.vaadin.haijian.filegenerator.ExcelFileBuilder;
import org.vaadin.haijian.filegenerator.FileBuilder;

import com.vaadin.data.Container;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Table;

public class Exporter extends Button {
    private FileBuilder fileBuilder;

    public Exporter(Table table) {
        this(table.getContainerDataSource(), table.getVisibleColumns());
        setHeader(table.getCaption());
        for (Object column : table.getVisibleColumns()) {
            String header = table.getColumnHeader(column);
            if (header != null) {
                setColumnHeader(column, header);
            }
        }
    }

    public Exporter(Container container) {
        this(container, null);
    }

    public Exporter(Container container, Object[] visibleColumns) {
        setCaption("Exporter");
        fileBuilder = new ExcelFileBuilder(container);
        fileBuilder.setVisibleColumns(visibleColumns);
        FileDownloader fileDownloader = new FileDownloader(new StreamResource(
                fileBuilder, "exported-excel.xls"));
        fileDownloader.extend(this);
    }

    public void setVisibleColumns(Object[] visibleColumns) {
        fileBuilder.setVisibleColumns(visibleColumns);
    }

    public void setColumnHeader(Object propertyId, String header) {
        fileBuilder.setColumnHeader(propertyId, header);
    }

    public void setHeader(String header) {
        fileBuilder.setHeader(header);
    }
}
