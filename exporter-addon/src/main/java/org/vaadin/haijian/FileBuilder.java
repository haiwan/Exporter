package org.vaadin.haijian;

import com.vaadin.data.BeanPropertySet;
import com.vaadin.data.PropertyDefinition;
import com.vaadin.data.PropertySet;
import com.vaadin.data.provider.DataCommunicator;
import com.vaadin.data.provider.Query;
import com.vaadin.ui.Grid;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class FileBuilder<T> {
    private static final String TMP_FILE_NAME = "tmp";

    File file;
    private Grid<T> grid;
    private Collection<Grid.Column> columns;
    private PropertySet<T> propertySet;
    private boolean headerRowBuilt = false;

    @SuppressWarnings("unchecked")
    FileBuilder(Grid<T> grid) {
        this.grid = grid;
        columns = grid.getColumns().stream().filter(this::isExportable).collect(Collectors.toList());
        if (columns.isEmpty()) {
            throw new ExporterException("No exportable column found, did you remember to set property name as the key for column");
        }
    }

    private boolean isExportable(Grid.Column column) {
        return !column.isHidden() && column.getId() != null && !column.getId().isEmpty()
                && (propertySet == null || propertySet.getProperty(column.getId()).isPresent());
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
        buildRows();
        buildFooter();
    }

    protected void resetContent() {

    }

    private void buildHeaderRow() {
        columns.forEach(column -> {
            Optional<PropertyDefinition<T, ?>> propertyDefinition = propertySet.getProperty(column.getId());
            if (propertyDefinition.isPresent()) {
                onNewCell();
                buildColumnHeaderCell(propertyDefinition.get().getCaption());
            } else {
                LoggerFactory.getLogger(this.getClass()).warn(String.format("Column key %s is a property which cannot be found", column.getId()));
            }
        });
        headerRowBuilt = true;
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
            onNewRow();
            buildRow(t);
        });
    }

    @SuppressWarnings("unchecked")
    private void buildRow(T item) {
        if (propertySet == null) {
            propertySet = (PropertySet<T>) BeanPropertySet.get(item.getClass());
            columns = columns.stream().filter(this::isExportable).collect(Collectors.toList());
        }
        if (!headerRowBuilt) {
            buildHeaderRow();
            onNewRow();
        }
        columns.forEach(column -> {
            Optional<PropertyDefinition<T, ?>> propertyDefinition = propertySet.getProperty(column.getId());
            if (propertyDefinition.isPresent()) {
                onNewCell();
                buildCell(propertyDefinition.get().getGetter().apply(item));
            } else {
                throw new ExporterException("Column key: " + column.getId() + " is a property which cannot be found");
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
