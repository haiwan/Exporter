package org.vaadin.addons.demo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.annotation.WebServlet;

import org.vaadin.haijian.CSVExporter;
import org.vaadin.haijian.ExcelExporter;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

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
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        setContent(layout);
        
        final Table sampleTable;
        try {
            sampleTable = createExampleTable();
            Button addData = new Button( "Add More data");
            layout.addComponents( addData, sampleTable);
            final ExcelExporter excelExporter = new ExcelExporter();
            excelExporter.setDateFormat("yyyy-MM-dd");
            excelExporter.setTableToBeExported(sampleTable);
            excelExporter.setCaption("Export to Excel");
            layout.addComponent(excelExporter);
            
            final CSVExporter csvExporter = new CSVExporter();
            csvExporter.setCaption("Export to CSV");
            csvExporter.setContainerToBeExported(sampleTable
                                                 .getContainerDataSource());
            csvExporter.setVisibleColumns(sampleTable.getVisibleColumns());
            layout.addComponent(csvExporter);
            
            excelExporter.setDownloadFileName("demo-excel-exporter.xls");
            csvExporter.setDownloadFileName("demo-csv-exporter.csv");
            
            addData.addClickListener(new ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					String firstName = generateName("addF");
					String lastName = generateName("addL");
					sampleTable.addItem(new Object[] { firstName, lastName, new Date() },
							new Integer(++lastLine));
					
					excelExporter.setTableToBeExported(sampleTable);
		            csvExporter.setContainerToBeExported(sampleTable.getContainerDataSource());
				}
			});
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
    }
    
    private String generateName( String prefix ) {
    	String name;
    	
        double doubleName = Math.random();
        BigDecimal bd = new BigDecimal(doubleName);
        bd = bd.setScale(2, RoundingMode.HALF_UP );
        name = prefix + bd.toString();
    	
    	return name;
    }
    
    private int lastLine = 0;
    private Table createExampleTable() throws UnsupportedOperationException, ParseException {
        /* Create the table with a caption. */
        Table table = new Table("This is my Table");
        table.setImmediate(true);
        
        /*
         * Define the names and data types of columns. The "default value"
         * parameter is meaningless here.
         */
        table.addContainerProperty("First Name", String.class, null);
        table.addContainerProperty("Last Name", String.class, null);
        table.addContainerProperty("Birthday", Date.class, null);
        
        /* Add a few items in the table. */
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String firstName = generateName("loadF");
		String lastName = generateName("loadL");
		table.addItem(new Object[] { firstName, lastName, new Date() }, new Integer(++lastLine));
        table.addItem(new Object[] { "Nicolaus", "Copernicus",
            formatter.parse("1473-02-12") }, new Integer(++lastLine));
        table.addItem(new Object[] { "Tycho", "Brahe", formatter.parse("1473-01-12") },
                      new Integer(++lastLine));
        table.addItem(new Object[] { "Giordano", "Bruno", formatter.parse("1546-03-09") },
                      new Integer(++lastLine));
        table.addItem(new Object[] { "Galileo", "Galilei",formatter.parse("1548-04-09") },
                      new Integer(++lastLine));
        table.addItem(new Object[] { "Johannes", "Kepler", formatter.parse("1564-06-07") },
                      new Integer(++lastLine));
        table.addItem(new Object[] { "Isaac", "Newton", formatter.parse("1546-08-09") },
                      new Integer(++lastLine));
        firstName = generateName("loadF");
		lastName = generateName("loadL");
		table.addItem(new Object[] { firstName, lastName, new Date() }, new Integer(++lastLine));
		
        return table;
    }

}
