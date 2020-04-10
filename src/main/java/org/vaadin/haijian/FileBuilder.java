package org.vaadin.haijian;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.binder.BeanPropertySet;
import com.vaadin.flow.data.binder.PropertyDefinition;
import com.vaadin.flow.data.binder.PropertySet;
import com.vaadin.flow.data.provider.DataCommunicator;
import com.vaadin.flow.data.provider.Query;

public abstract class FileBuilder<T> {
    private static final String TMP_FILE_NAME = "tmp";

    File file;
    private Grid<T> grid;
    private Map<Grid.Column<T>, String> customColumnHeaders;
    private Collection<Grid.Column> columns;
    private PropertySet<T> propertySet;

    @SuppressWarnings("unchecked")
    FileBuilder(Grid<T> grid, Map<Grid.Column<T>, String> columnHeaders) {
        this.grid = grid;
        this.customColumnHeaders = columnHeaders;
        columns = grid.getColumns().stream().filter(this::isExportable).collect(Collectors.toList());
        try {
            Field field = Grid.class.getDeclaredField("propertySet");
            field.setAccessible(true);
            Object propertySetRaw = field.get(grid);
            if (propertySetRaw != null) {
                propertySet = (PropertySet<T>) propertySetRaw;
            }
            if (propertySet == null && columnHeaders == null) {
                throw new ExporterException("Create Grid with bean type or create Grid with adding columns and columns headers map.");
            }
        } catch (Exception e) {
            throw new ExporterException("couldn't read propertyset information from grid", e);
        }
        if (columns.isEmpty()) {
            throw new ExporterException("No exportable column found, did you remember to set property name as the key for column");
        }
    }

    private boolean isExportable(Grid.Column<T> column) {
        return column.isVisible() && column.getKey() != null && !column.getKey().isEmpty()
                && (propertySet == null || propertySet.getProperty(column.getKey()).isPresent());
    }

    InputStream build() {
        try {
            initTempFile();
            resetContent();
            buildFileContent();
            writeToFile();
            return new FileInputStream(file);
        } catch (Exception e) {
            throw new ExporterException("An error happened during exporting your Grid", e);
        }
    }

    private void initTempFile() throws IOException {
        if (file == null || file.delete()) {
            file = createTempFile();
        }
    }

    private void buildFileContent() {
        buildHeaderRow();
        buildRows();
        buildFooter();
    }

    protected void resetContent() {

    }

    private void buildHeaderRow() {
        onNewRow();
        if (customColumnHeaders == null) {
            columns.forEach(column -> {
                // when grid created with bean type
                if (propertySet != null) {
                    Optional<PropertyDefinition<T, ?>> propertyDefinition = propertySet.getProperty(column.getKey());
                    if (propertyDefinition.isPresent()) {
                        onNewCell();
                        buildColumnHeaderCell(propertyDefinition.get().getCaption());
                    } else {
                        LoggerFactory.getLogger(this.getClass()).warn(String.format("Column key %s is a property which cannot be found", column.getKey()));
                    }
                }
            });
        } else {
            columns.forEach(column -> {
                String columnHeader = customColumnHeaders.get(column);
                if (columnHeader != null) {
                    onNewCell();
                    buildColumnHeaderCell(columnHeader);
                } else {
                    LoggerFactory.getLogger(this.getClass()).warn(String.format("Column with key %s have not column header value defined in map", column.getKey()));
                }
            });
        }
    }
  }


    void buildColumnHeaderCell(String header) {

    }

    @SuppressWarnings("unchecked")
    private void buildRows() {
        Object filter = null;
        try {
            Method method = DataCommunicator.class.getDeclaredMethod("getFilter");
            method.setAccessible(true);
            filter = method.invoke(grid.getDataCommunicator());
        } catch (Exception e) {
            LoggerFactory.getLogger(this.getClass()).error("Unable to get filter from DataCommunicator");
        }

        Query streamQuery = new Query(0, grid.getDataProvider().size(new Query(filter)), grid.getDataCommunicator().getBackEndSorting(),
                grid.getDataCommunicator().getInMemorySorting(), null);
        Stream<T> dataStream = getDataStream(streamQuery);

        dataStream.forEach(t -> {
            buildRow(t);
        });
    }

    @SuppressWarnings("unchecked")
    private void buildRow(T item) {
        onNewRow();
        if (propertySet == null) {
            propertySet = (PropertySet<T>) BeanPropertySet.get(item.getClass());
            columns = columns.stream().filter(this::isExportable).collect(Collectors.toList());
        }

        columns.forEach(column -> {
            Optional<PropertyDefinition<T, ?>> propertyDefinition = propertySet.getProperty(column.getKey());
            if (propertyDefinition.isPresent()) {
                onNewCell();
                buildCell(propertyDefinition.get().getGetter().apply(item));
            } else {
                throw new ExporterException("Column key: " + column.getKey() + " is a property which cannot be found");
            }
        });
    }

    void onNewRow() {

    }

    void onNewCell() {

    }

    abstract void buildCell(Object value);

    void buildFooter() {

    }

    abstract String getFileExtension();

    private File createTempFile() throws IOException {
        return File.createTempFile(TMP_FILE_NAME, getFileExtension());
    }

    abstract void writeToFile();

    int getNumberOfColumns() {
        return columns.size();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Stream<T> getDataStream(Query newQuery) {
        Stream<T> stream = grid.getDataProvider().fetch(newQuery);
        if (stream.isParallel()) {
            LoggerFactory.getLogger(DataCommunicator.class).debug("Data provider {} has returned " + "parallel stream on 'fetch' call",
                    grid.getDataProvider().getClass());
            stream = stream.collect(Collectors.toList()).stream();
            assert !stream.isParallel();
        }
        return stream;
    }
}
