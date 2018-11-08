package org.vaadin.haijian;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.vaadin.haijian.helpers.GridDemoViewCreator;

@Route("")
public class DemoView extends HorizontalLayout {

    public DemoView() {
        VerticalLayout withNormalGrid = new VerticalLayout();
        withNormalGrid.add(new Span("Grid With List data provider"));
        Component normalGrid = GridDemoViewCreator.createGridWithListDataProviderDemo();
        withNormalGrid.add(normalGrid);

        VerticalLayout withPageableGrid = new VerticalLayout();
        Component lazyGrid = GridDemoViewCreator.createGridWithLazyLoadingDemo();
        withPageableGrid.add(new Span("Grid with Lazy loading data provider"));
        withPageableGrid.add(lazyGrid);
        add(withNormalGrid, withPageableGrid);
    }
}
