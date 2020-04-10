package org.vaadin.haijian.helpers;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.CallbackDataProvider;
import com.vaadin.flow.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.server.StreamResource;
import org.vaadin.haijian.Exporter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GridDemoViewCreator {
    final static PersonService service = new PersonService();

    public static Component createGridWithBeanTypeDemo() {
        return createGridBeanTypeDemo(false);
    }

    public static Component createGridWithBeanTypeCustomHeadersDemo() {
        return createGridBeanTypeDemo(true);
    }

    public static Component createGridWithListDataProviderDemo() {
        return createGridDemo(false);
    }

    public static Component createGridWithLazyLoadingDemo() {
        return createGridDemo(true);
    }

    private static Component createGridBeanTypeDemo(boolean isCustomHeaders) {
        VerticalLayout result = new VerticalLayout();
        final List<AgeGroup> groups = new ArrayList<>();
        groups.add(new AgeGroup(0, 18));
        groups.add(new AgeGroup(19, 26));
        groups.add(new AgeGroup(27, 40));
        groups.add(new AgeGroup(41, 100));

        final ComboBox<AgeGroup> filter = new ComboBox<>("Filter", groups);
        result.add(filter);

        final Grid<Person> grid = new Grid<>(Person.class);
        grid.setPageSize(10);
        result.setHorizontalComponentAlignment(FlexComponent.Alignment.STRETCH, grid);

        result.add(grid);

        setupListDataProviderForGrid(grid, filter);

        // custom columns names
        Map<Column<Person>, String> customHeaderCaptions = new HashMap<Grid.Column<Person>, String>();
        customHeaderCaptions.put(grid.getColumnByKey("name"), "Full name");
        customHeaderCaptions.put(grid.getColumnByKey("email"), "E-mail");
        customHeaderCaptions.put(grid.getColumnByKey("age"), "Old");
        customHeaderCaptions.put(grid.getColumnByKey("birthday"), "Day of birth");

        Anchor downloadAsExcel = null;

        if (isCustomHeaders) {
            downloadAsExcel = new Anchor(new StreamResource("my-excel.xls", Exporter.exportAsExcel(grid, customHeaderCaptions)), "Download As Excel");
        } else {
            downloadAsExcel = new Anchor(new StreamResource("my-excel.xls", Exporter.exportAsExcel(grid)), "Download As Excel");
        }
        Anchor downloadAsCSV = new Anchor(new StreamResource("my-csv.csv", Exporter.exportAsCSV(grid)), "Download As CSV");
        result.add(new HorizontalLayout(downloadAsExcel, downloadAsCSV));

        return result;
    }

    private static Component createGridDemo(boolean lazyLoading) {
        VerticalLayout result = new VerticalLayout();
        final List<AgeGroup> groups = new ArrayList<>();
        groups.add(new AgeGroup(0, 18));
        groups.add(new AgeGroup(19, 26));
        groups.add(new AgeGroup(27, 40));
        groups.add(new AgeGroup(41, 100));

        final ComboBox<AgeGroup> filter = new ComboBox<>("Filter", groups);
        result.add(filter);

        final Grid<Person> grid = new Grid<>();
        grid.setPageSize(10);
        result.setHorizontalComponentAlignment(FlexComponent.Alignment.STRETCH, grid);
        grid.addColumn(Person::getName).setHeader("Name").setKey("name").setSortProperty("name");
        grid.addColumn(Person::getEmail).setHeader("Email").setKey("email");
        grid.addColumn(Person::getAge).setHeader("Age").setKey("age");
        grid.addColumn(new LocalDateRenderer<>(Person::getBirthday)).setHeader("Birthday").setKey("birthday");

        result.add(grid);

        if (lazyLoading) {
            setupLazyLoadingDataProviderForGrid(grid, filter);
        } else {
            setupListDataProviderForGrid(grid, filter);
        }

        // custom columns names
        Map<Column<Person>, String> customHeaderCaptions = new HashMap<Grid.Column<Person>, String>();
        customHeaderCaptions.put(grid.getColumnByKey("name"), "Full name");
        customHeaderCaptions.put(grid.getColumnByKey("email"), "E-mail");
        customHeaderCaptions.put(grid.getColumnByKey("age"), "Old");
        customHeaderCaptions.put(grid.getColumnByKey("birthday"), "Day of birth");

        Anchor downloadAsExcel = new Anchor(new StreamResource("my-excel.xls", Exporter.exportAsExcel(grid, customHeaderCaptions)), "Download As Excel");
        Anchor downloadAsCSV = new Anchor(new StreamResource("my-csv.csv", Exporter.exportAsCSV(grid, customHeaderCaptions)), "Download As CSV");
        result.add(new HorizontalLayout(downloadAsExcel, downloadAsCSV));

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
