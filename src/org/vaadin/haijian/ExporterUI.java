package org.vaadin.haijian;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * Main UI class
 */
@SuppressWarnings("serial")
public class ExporterUI extends UI {

    @Override
    protected void init(VaadinRequest request) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        setContent(layout);

        Table sampleTable = createExampleTable();
        layout.addComponent(sampleTable);
        ExcelExporter excelExporter = new ExcelExporter(sampleTable);
        excelExporter.setCaption("Export to Excel");
        layout.addComponent(excelExporter);
        PdfExporter pdfExporter = new PdfExporter(sampleTable);
        pdfExporter.setCaption("Export to PDF");
        pdfExporter.setWithBorder(false);
        layout.addComponent(pdfExporter);
    }

    private Table createExampleTable() {
        /* Create the table with a caption. */
        Table table = new Table("This is my Table");

        /*
         * Define the names and data types of columns. The "default value"
         * parameter is meaningless here.
         */
        table.addContainerProperty("First Name", String.class, null);
        table.addContainerProperty("Last Name", String.class, null);
        table.addContainerProperty("Year", Integer.class, null);

        /* Add a few items in the table. */
        table.addItem(new Object[] { "Nicolaus", "Copernicus",
                new Integer(1473) }, new Integer(1));
        table.addItem(new Object[] { "Tycho", "Brahe", new Integer(1546) },
                new Integer(2));
        table.addItem(new Object[] { "Giordano", "Bruno", new Integer(1548) },
                new Integer(3));
        table.addItem(new Object[] { "Galileo", "Galilei", new Integer(1564) },
                new Integer(4));
        table.addItem(new Object[] { "Johannes", "Kepler", new Integer(1571) },
                new Integer(5));
        table.addItem(new Object[] { "Isaac", "Newton", new Integer(1643) },
                new Integer(6));
        return table;
    }

}