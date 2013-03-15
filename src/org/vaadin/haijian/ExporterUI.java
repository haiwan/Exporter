package org.vaadin.haijian;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

        Table sampleTable;
		try {
			sampleTable = createExampleTable();
			layout.addComponent(sampleTable);
	        ExcelExporter excelExporter = new ExcelExporter();
	        excelExporter.setDateFormat("yyyy-MM-dd");
	        excelExporter.setTableToBeExported(sampleTable);
	        excelExporter.setCaption("Export to Excel");
	        layout.addComponent(excelExporter);
	        CSVExporter csvExporter = new CSVExporter();
	        csvExporter.setCaption("Export to CSV");
	        csvExporter.setContainerToBeExported(sampleTable
	                .getContainerDataSource());
	        csvExporter.setVisibleColumns(sampleTable.getVisibleColumns());
	        layout.addComponent(csvExporter);
	        PdfExporter pdfExporter = new PdfExporter(sampleTable);
	        pdfExporter.setCaption("Export to PDF");
	        pdfExporter.setWithBorder(false);
	        layout.addComponent(pdfExporter);
	        
	        excelExporter.setDownloadFileName("demo-excel-exporter");
	        pdfExporter.setDownloadFileName("demo-pdf-exporter.pdf");
	        csvExporter.setDownloadFileName("demo-csv-exporter.csv");
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
        
    }

    private Table createExampleTable() throws UnsupportedOperationException, ParseException {
        /* Create the table with a caption. */
        Table table = new Table("This is my Table");

        /*
         * Define the names and data types of columns. The "default value"
         * parameter is meaningless here.
         */
        table.addContainerProperty("First Name", String.class, null);
        table.addContainerProperty("Last Name", String.class, null);
        table.addContainerProperty("Birthday", Date.class, null);

        /* Add a few items in the table. */
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        table.addItem(new Object[] { "Nicolaus", "Copernicus",
        		formatter.parse("1473-02-12") }, new Integer(1));
        table.addItem(new Object[] { "Tycho", "Brahe", formatter.parse("1473-01-12") },
                new Integer(2));
        table.addItem(new Object[] { "Giordano", "Bruno", formatter.parse("1546-03-09") },
                new Integer(3));
        table.addItem(new Object[] { "Galileo", "Galilei",formatter.parse("1548-04-09") },
                new Integer(4));
        table.addItem(new Object[] { "Johannes", "Kepler", formatter.parse("1564-06-07") },
                new Integer(5));
        table.addItem(new Object[] { "Isaac", "Newton", formatter.parse("1546-08-09") },
                new Integer(6));
        return table;
    }

}