package org.vaadin.addons.demo;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import org.vaadin.addons.demo.helpers.GridDemoViewCreator;

import javax.servlet.annotation.WebServlet;

@Theme("demo")
@Title("MyComponent Add-on Demo")
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

        VerticalLayout withPageableGrid = new VerticalLayout();
        Component lazyGrid = GridDemoViewCreator.createGridWithLazyLoadingDemo();
        withPageableGrid.addComponent(new Label("Grid with Lazy loading data provider"));
        withPageableGrid.addComponent(lazyGrid);
        rootContent.addComponents(withNormalGrid, withPageableGrid);
    }
}
