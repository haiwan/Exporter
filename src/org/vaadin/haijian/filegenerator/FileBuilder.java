package org.vaadin.haijian.filegenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.server.StreamResource.StreamSource;

public abstract class FileBuilder implements StreamSource {
    private File file;
    public Container container;
    private Object[] visibleColums;
    private Map<Object, String> columnHeaderMap;
    private String header;

    public FileBuilder(Container container) {
        this.container = container;
        columnHeaderMap = new HashMap<Object, String>();
        for (Object propertyId : container.getContainerPropertyIds()) {
            columnHeaderMap
                    .put(propertyId, propertyId.toString().toUpperCase());
        }
        if (visibleColums == null) {
            visibleColums = container.getContainerPropertyIds().toArray();
        }
    }

    public void setVisibleColumns(Object[] visibleColums) {
        this.visibleColums = visibleColums;
    }

    public File getFile() {
        buildFileContent();
        prepareFileForDownload();
        return file;
    }

    private void buildFileContent() {
        buildHeader();
        buildColumnHeaders();
        buildRows();
        buildFooter();
    }

    private void buildColumnHeaders() {
        if (visibleColums.length == 0) {
            return;
        }
        onHeader();
        for (Object propertyId : visibleColums) {
            String header = columnHeaderMap.get(propertyId);
            onNewCell();
            buildColumnHeaderCell(header);
        }
    }

    protected void onHeader() {
        onNewRow();
    }

    protected void buildColumnHeaderCell(String header) {

    }

    protected void buildHeader() {
        // TODO Auto-generated method stub

    }

    private void buildRows() {
        if (container == null || container.getItemIds().isEmpty()) {
            return;
        }
        for (Object itemId : container.getItemIds()) {
            onNewRow();
            buildRow(itemId);
        }
    }

    private void buildRow(Object itemId) {
        if (visibleColums.length == 0) {
            return;
        }
        for (Object propertyId : visibleColums) {
            Property<?> property = container.getContainerProperty(itemId,
                    propertyId);
            onNewCell();
            buildCell(property == null ? null : property.getValue());
        }
    }

    protected void onNewRow() {

    }

    protected void onNewCell() {

    }

    protected abstract void buildCell(Object value);

    protected void buildFooter() {
        // TODO Auto-generated method stub

    }

    protected abstract String getFileExtension();

    protected String getFileName() {
        return "tmp";
    }

    protected abstract void writeToFile(FileOutputStream fos)
            throws IOException;

    private void prepareFileForDownload() {
        try {
            file = File.createTempFile(getFileName(), getFileExtension());
            FileOutputStream fos = new FileOutputStream(file);
            writeToFile(fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public InputStream getStream() {
        buildFileContent();
        prepareFileForDownload();
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setColumnHeader(Object propertyId, String header) {
        columnHeaderMap.put(propertyId, header);
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    protected int getNumberofColumns() {
        return visibleColums.length;
    }
}
