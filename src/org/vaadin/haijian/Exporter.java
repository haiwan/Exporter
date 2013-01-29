package org.vaadin.haijian;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.vaadin.haijian.filegenerator.FileBuilder;

import com.vaadin.data.Container;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Table;

public abstract class Exporter extends Button implements StreamSource {
    protected FileBuilder fileBuilder;
    private FileDownloader fileDownloader;

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
        initFileBuilder(container, visibleColumns);
        fileDownloader = new FileDownloader(new StreamResource(this,
                getDownloadFileName()));
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

    protected void initFileBuilder(Container container, Object[] visibleColumns) {
        fileBuilder = createFileBuilder(container);
        fileBuilder.setVisibleColumns(visibleColumns);
    }

    protected abstract FileBuilder createFileBuilder(Container container);

    protected abstract String getDownloadFileName();

    @Override
    public InputStream getStream() {
        try {
            return new FileInputStream(fileBuilder.getFile());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
