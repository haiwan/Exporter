package org.vaadin.haijian.demo;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;

import javax.servlet.annotation.WebServlet;

import org.vaadin.haijian.demo.helpers.GridDemoViewCreator;

@Theme("valo")
@Title("Exporter Add-on Demo")
@SuppressWarnings("serial")
public class DemoUI extends UI
{

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = DemoUI.class)
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {
        HorizontalLayout rootContent = new HorizontalLayout();
        rootContent.setSizeFull();
        setContent(rootContent);

        VerticalLayout withNormalGrid = new VerticalLayout();
        withNormalGrid.addComponent(new Label("Grid With List data provider"));
        Component normalGrid = GridDemoViewCreator.createGridWithListDataProviderDemo();
        withNormalGrid.addComponent(normalGrid);

        VerticalLayout withLazyLoadingGrid = new VerticalLayout();
        Component lazyGrid = GridDemoViewCreator.createGridWithLazyLoadingDemo();
        withLazyLoadingGrid.addComponent(new Label("Grid with Lazy loading data provider"));
        withLazyLoadingGrid.addComponent(lazyGrid);
        rootContent.addComponents(withNormalGrid, withLazyLoadingGrid);
    }
}
