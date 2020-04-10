package org.vaadin.haijian;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.LoggerFactory;
import org.vaadin.haijian.option.ColumnOption;
import org.vaadin.haijian.option.ExporterOption;

import com.vaadin.data.BeanPropertySet;
import com.vaadin.data.PropertyDefinition;
import com.vaadin.data.PropertySet;
import com.vaadin.data.ValueProvider;
import com.vaadin.data.provider.DataCommunicator;
import com.vaadin.data.provider.Query;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;

public abstract class FileBuilder<T> {
    private static final String TMP_FILE_NAME = "tmp";

    File file;
    private Grid<T> grid;
    private Collection<Grid.Column> columns;
    private PropertySet<T> propertySet;
    private boolean headerRowBuilt = false;
    private ExporterOption option;

    FileBuilder(Grid<T> grid, ExporterOption option) {
        this.grid = Objects.requireNonNull(grid);
        this.option = Objects.requireNonNull(option);
        columns = grid.getColumns().stream().filter(this::isColumnExportable).collect(Collectors.toList());
        if (columns.isEmpty()) {
            throw new ExporterException(
                    "No exportable column found, did you remember to set property name as the key for column");
        }

    }

    private boolean isColumnExportable(Grid.Column column) {
        if (column.isHidden()) {
            return false;
        }

        if (column.getId() == null) {
            return false;
        }

        if (column.getId().trim().isEmpty()) {
            return false;
        }

        return true;
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
        	if (getColumnOption(column).getHeaderProviderFunction().isPresent()) {
                onNewCell();
                buildColumnHeaderCell(getColumnOption(column).getHeaderProviderFunction().get().apply(column.getId()));
        	} else {
        		Optional<PropertyDefinition<T, ?>> propertyDefinition = propertySet.getProperty(column.getId());
                if (propertyDefinition.isPresent()) {
                    onNewCell();
                    buildColumnHeaderCell(getColumnHeader(propertyDefinition.get()));
                } else {
                    LoggerFactory.getLogger(this.getClass())
                            .warn(String.format("Column id %s is a property which cannot be found", column.getId()));
                }
        	}
        });

        headerRowBuilt = true;
    }

    private String getColumnHeader(PropertyDefinition<T, ?> propertyDefinition) {
        ColumnOption columnOption = option.getColumnOption(propertyDefinition.getName());
        String columnName = columnOption.getColumnName();
        if (columnName == null) {
            columnName = propertyDefinition.getCaption();
        }
        if (columnOption.isUpperCase()) {
            columnName = columnName.toUpperCase();
        }
        return columnName;
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

        Query streamQuery = new Query(0, grid.getDataProvider().size(new Query(filter)),
                grid.getDataCommunicator().getBackEndSorting(), grid.getDataCommunicator().getInMemorySorting(), null);
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
        }

        if (!headerRowBuilt) {
            buildHeaderRow();
            onNewRow();
        }
        columns.forEach(column -> {
        	Optional<ValueProvider<Object, Object>> optionalGetter = getColumnOption(column).getValueProviderFunction();
            if (optionalGetter.isPresent()) {
                Object value = optionalGetter.get().apply(item);
                onNewCell();
                buildCell(value);
            } else {
                Optional<PropertyDefinition<T, ?>> propertyDefinition = propertySet.getProperty(column.getId());
                if (propertyDefinition.isPresent()) {
                    onNewCell();
                    buildCell(propertyDefinition.get().getGetter().apply(item));
                } else {
                    throw new ExporterException(
                            "Property for column id: " + column.getId() + " cannot be found");
                }
            }
        });
    }

	private ColumnOption getColumnOption(Column column) {
		return option.getColumnOption(column.getId());
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
            LoggerFactory.getLogger(DataCommunicator.class).debug(
                    "Data provider {} has returned " + "parallel stream on 'fetch' call",
                    grid.getDataProvider().getClass());
            stream = stream.collect(Collectors.toList()).stream();
            assert !stream.isParallel();
        }
        return stream;
    }
}
