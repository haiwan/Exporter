package org.vaadin.haijian;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.binder.BeanPropertySet;
import com.vaadin.flow.data.binder.PropertyDefinition;
import com.vaadin.flow.data.binder.PropertySet;
import com.vaadin.flow.data.provider.DataCommunicator;
import com.vaadin.flow.data.provider.Query;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class FileBuilder<T>{
    protected File file;
    private Grid<T> grid;
    private Collection<Grid.Column> columns;
    private String header;
    private Locale locale = Locale.getDefault();;
    private String dateFormatString = "MM/dd/yyyy hh:mm";
    private PropertySet<T> propertySet;

    public FileBuilder(Grid<T> grid) {
        this.grid = grid;
        columns = grid.getColumns().stream().filter(Grid.Column::isVisible).collect(Collectors.toList());
        boolean hasColumnWithKeyUndefined = columns.stream().anyMatch(column -> column.getKey()==null);
        if(hasColumnWithKeyUndefined){
            throw new RuntimeException("Please define a key for every visible column, key should be the property name");
        }
    }

    public InputStream build() {
        try {
            initTempFile();
            resetContent();
            buildFileContent();
            writeToFile();
            return new FileInputStream(file);
        } catch (Exception e) {
            throw new RuntimeException("An error happened during exporting your Grid", e);
        }
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

        /*
        grid.getHeaderRows().forEach(headerRow -> {
            onHeader();
            headerRow.getCells().forEach(cell -> {
                onNewCell();
                buildColumnHeaderCell(cell.g);
            });
        });

        if (columns.isEmpty()) {
            return;
        }
        onHeader();
        columns.forEach(column -> {
            String header
        });
        for (Object propertyId : visibleColumns) {
            String header = columnHeaderMap.get(propertyId);
            onNewCell();
            buildColumnHeaderCell(header);
        }*/
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
        Object filter = null;
        try {
            Method method = DataCommunicator.class.getDeclaredMethod("getFilter");
            method.setAccessible(true);
            filter = method.invoke(grid.getDataCommunicator());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Query streamQuery = new Query(0, grid.getDataProvider().size(new Query(filter)), grid.getDataCommunicator().getBackEndSorting(),
                grid.getDataCommunicator().getInMemorySorting(), null);
        Stream<T> stream = getDataStream(streamQuery);
        stream.forEach( t -> {
            onNewRow();
            buildRow(t);
        });
    }

    private void buildRow(T item) {
        if(propertySet==null){
            propertySet = (PropertySet<T>) BeanPropertySet.get(item.getClass());
        }
        columns.forEach(column -> {
            Optional<PropertyDefinition<T, ?>> propertyDefinition = propertySet.getProperty(column.getKey());
            if(propertyDefinition.isPresent()){
                onNewCell();
                buildCell(propertyDefinition.get().getGetter().apply(item));
            }else {
                throw new RuntimeException("Column key: "+column.getKey()+" is a property which cannot be found");
            }
        });
    }

    protected void onNewRow() {

    }

    protected void onNewCell() {

    }

    protected abstract void buildCell(Object value);

    protected void buildFooter() {

    }

    protected abstract String getFileExtension();

    protected String getFileName() {
        return "tmp";
    }

    protected File createTempFile() throws IOException {
        return File.createTempFile(getFileName(), getFileExtension());
    }

    protected abstract void writeToFile();

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    protected int getNumberofColumns() {
        return columns.size();
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

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Stream<T> getDataStream(Query newQuery) {
        Stream<T> stream = grid.getDataProvider().fetch(newQuery);
        if (stream.isParallel()) {
            LoggerFactory.getLogger(DataCommunicator.class)
                    .debug("Data provider {} has returned "
                                    + "parallel stream on 'fetch' call",
                            grid.getDataProvider().getClass());
            stream = stream.collect(Collectors.toList()).stream();
            assert !stream.isParallel();
        }
        return stream;
    }
}
