package org.vaadin.haijian.filegenerator;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.ui.Table;

public abstract class FileBuilder implements Serializable {
    protected File file;
    public Container container;
    public Table table;
    private Object[] visibleColumns;
    private Map<Object, String> columnHeaderMap;
    private String header;
    private Locale locale = Locale.getDefault();;
    private String dateFormatString = "MM/dd/yyyy hh:mm";

    public FileBuilder() {

    }

    public FileBuilder(Container container) {
		setContainer(container);
    }
    
    public FileBuilder(Table table) {
        setTable(table);
    }

	public void setTable(Table table) {
		this.table = table;
		setContainer(table.getContainerDataSource());
	}

    public void setContainer(Container container) {
        this.container = container;
        columnHeaderMap = new HashMap<Object, String>();
        for (Object propertyId : container.getContainerPropertyIds()) {
            columnHeaderMap
                    .put(propertyId, propertyId.toString().toUpperCase());
        }
        if (visibleColumns == null) {
            visibleColumns = container.getContainerPropertyIds().toArray();
        }
    }

    public void setVisibleColumns(Object[] visibleColumns) {
        this.visibleColumns = visibleColumns;
    }

    public File getFile() {
        try {
            initTempFile();
            resetContent();
            buildFileContent();
            writeToFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    private void initTempFile() throws IOException {
        if (file != null) {
            file.delete();
        }
        file = createTempFile();
    }

    protected void buildFileContent() {
        buildHeader();
        buildColumnHeaders();
        buildRows();
        buildFooter();
    }

    protected void resetContent() {

    }

    protected void buildColumnHeaders() {
        if (visibleColumns.length == 0) {
            return;
        }
        onHeader();
        for (Object propertyId : visibleColumns) {
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
        if (visibleColumns.length == 0) {
            return;
        }
        for (Object propertyId : visibleColumns) {
            Property<?> property = container.getContainerProperty(itemId,
                    propertyId);
            
            Object modelValue = property != null ? property.getValue() : null;
    		Object presentationValue = modelValue;
           
    		Converter<String, Object> converter = table != null ? table.getConverter(propertyId) : null;
    		if (converter != null && modelValue != null) {
    			presentationValue = converter.convertToPresentation(property.getValue(), locale);
    		}
    		
            onNewCell();
            buildCell(modelValue, presentationValue);
        }
    }

    protected void onNewRow() {

    }

    protected void onNewCell() {

    }

    
    /**
     * Build the cell, with the provided value. If the implementation supports formatting specific 
     * classes, it should attempt to use modelValue. If modelValue has an unknown classes, it 
     * should rely on presentationValue, that will use Table's converters if available.
     * 
     * @param modelValue The model value
     * @param presentationValue The presentation value, using the table converter if available.
     */
    protected abstract void buildCell(Object modelValue, Object presentationValue);

    protected void buildFooter() {
        // TODO Auto-generated method stub

    }

    protected abstract String getFileExtension();

    protected String getFileName() {
        return "tmp";
    }

    protected File createTempFile() throws IOException {
        return File.createTempFile(getFileName(), getFileExtension());
    }

    protected abstract void writeToFile();

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
        return visibleColumns.length;
    }

	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	
	public void setDateFormat(String dateFormat) {
		this.dateFormatString = dateFormat;
	}
	
	protected String getDateFormatString(){
		return dateFormatString;
	}
	
	protected String formatDate(Date date){
		SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatString, locale);
		return dateFormat.format(date);
	}
}
