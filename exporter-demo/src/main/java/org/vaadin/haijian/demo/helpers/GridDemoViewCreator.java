package org.vaadin.haijian.demo.helpers;

import com.vaadin.data.ValueProvider;
import com.vaadin.data.provider.CallbackDataProvider;
import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.LocalDateRenderer;
import org.vaadin.haijian.Exporter;
import org.vaadin.haijian.option.ColumnOption;
import org.vaadin.haijian.option.ExporterOption;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GridDemoViewCreator {
    final static PersonService service = new PersonService();


    public static Component createGridWithListDataProviderDemo(){
        return createGridDemo(false);
    }

    public static Component createGridWithLazyLoadingDemo(){
        return createGridDemo(true);
    }

    private static Component createGridDemo(boolean lazyLoading) {
        VerticalLayout result = new VerticalLayout();
        result.setMargin(false);
        final List<AgeGroup> groups = new ArrayList<>();
        groups.add(new AgeGroup(0, 18));
        groups.add(new AgeGroup(19, 26));
        groups.add(new AgeGroup(27, 40));
        groups.add(new AgeGroup(41, 100));

        final ComboBox<AgeGroup> filter = new ComboBox<>("Filter", groups);
        result.addComponent(filter);

        final Grid<Person> grid = new Grid<>();
        grid.setHeightByRows(10);
        grid.setWidth("100%");
        grid.addColumn(Person::getName).setCaption("Name").setId("name").setSortProperty("name");
        grid.addColumn(Person::getEmail).setCaption("Email").setId("email");
        grid.addColumn(Person::getAge).setCaption("Age").setId("age");
        grid.addColumn(Person::getBirthday).setRenderer(new LocalDateRenderer()).setCaption("Birthday").setId("birthday");

        result.addComponent(grid);

        Button downloadAsExcel = new Button("Download As Excel");
        Button downloadAsCSV = new Button("Download As CSV");

        Button downloadAsExcelWithCustomHeader = new Button("Download As Excel With Custom Header");
        Button downloadAsCSVWithCustomHeader = new Button("Download As CSV With Custom Header");

        StreamResource excelStreamResource = new StreamResource((StreamResource.StreamSource) () -> Exporter.exportAsExcel(grid), "my-excel.xlsx");
        FileDownloader excelFileDownloader = new FileDownloader(excelStreamResource);
        excelFileDownloader.extend(downloadAsExcel);

        StreamResource csvStreamResource = new StreamResource((StreamResource.StreamSource) () -> Exporter.exportAsCSV(grid), "my-csv.csv");
        FileDownloader csvFileDownloader = new FileDownloader(csvStreamResource);
        csvFileDownloader.extend(downloadAsCSV);


        ExporterOption exporterOption = new ExporterOption();
        exporterOption.getColumnOption("name").columnName("My Name").toUpperCase();
        exporterOption.getColumnOption("email").columnName("My Email");
        exporterOption.getColumnOption("age").headerProviderFunction(value -> "Current " + value + " x10");
        exporterOption.getColumnOption("age").valueProviderFunction(source -> {return (int) ((Person)source).getAge() * 10;});

        StreamResource excelStreamResourceWithCustomHeader = new StreamResource((StreamResource.StreamSource) () -> Exporter.exportAsExcel(grid, exporterOption), "my-excel-with-cutom-header.xlsx");
        FileDownloader excelFileDownloaderWithCustomHeader = new FileDownloader(excelStreamResourceWithCustomHeader);
        excelFileDownloaderWithCustomHeader.extend(downloadAsExcelWithCustomHeader);

        StreamResource csvStreamResourceWithCustomHeader = new StreamResource((StreamResource.StreamSource) () -> Exporter.exportAsCSV(grid, exporterOption), "my-csv-with-custom-header.csv");
        FileDownloader csvFileDownloaderWithCustomHeader = new FileDownloader(csvStreamResourceWithCustomHeader);
        csvFileDownloaderWithCustomHeader.extend(downloadAsCSVWithCustomHeader);


        result.addComponents(new HorizontalLayout(downloadAsExcel, downloadAsCSV, downloadAsExcelWithCustomHeader, downloadAsCSVWithCustomHeader));

        if(lazyLoading){
            setupLazyLoadingDataProviderForGrid(grid, filter);
        }else{
            setupListDataProviderForGrid(grid, filter);
        }
        return result;
    }


    private static void setupListDataProviderForGrid(Grid<Person> grid, ComboBox<AgeGroup> filter) {
        ListDataProvider<Person> listDataProvider = DataProvider.fromStream(service.getPersons(0, 100, null, Collections.emptyList()));
        grid.setDataProvider(listDataProvider);

        filter.addValueChangeListener(e -> {
            final AgeGroup value = e.getValue();
            ListDataProvider<Person> filteredDataProvider = DataProvider.fromStream(service.getPersons(0, 100, value, Collections.emptyList()));
            grid.setDataProvider(filteredDataProvider);
        });
    }

    private static void setupLazyLoadingDataProviderForGrid(Grid<Person> grid, ComboBox<AgeGroup> filter) {
        final CallbackDataProvider<Person, AgeGroup> dataProvider = DataProvider.fromFilteringCallbacks(
                q -> service.getPersons(q.getOffset(), q.getLimit(), q.getFilter().orElse(null), q.getSortOrders()),
                q -> service.countPersons(q.getOffset(), q.getLimit(), q.getFilter().orElse(null)));

        ConfigurableFilterDataProvider<Person, Void, AgeGroup> filterProvider = dataProvider.withConfigurableFilter();
        grid.setDataProvider(filterProvider);

        filter.addValueChangeListener(e -> {
            final AgeGroup value = e.getValue();
            filterProvider.setFilter(value);
        });
    }
}
