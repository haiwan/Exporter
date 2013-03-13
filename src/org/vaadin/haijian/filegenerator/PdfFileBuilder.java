package org.vaadin.haijian.filegenerator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.vaadin.data.Container;

public class PdfFileBuilder extends FileBuilder {
    private Document document;
    private PdfPTable table;
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 14,
            Font.BOLD);
    private boolean withBorder;

    public PdfFileBuilder(Container container) {
        super(container);
    }

    @Override
    protected void buildHeader() {
        if (getHeader() != null) {
            Paragraph title = new Paragraph(getHeader(), catFont);
            title.add(new Paragraph(" "));
            title.setAlignment(Element.ALIGN_CENTER);
            try {
                document.add(title);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void buildColumnHeaderCell(String header) {
        PdfPCell cell = new PdfPCell(new Phrase(header, subFont));
        if (!withBorder) {
            cell.setBorder(Rectangle.NO_BORDER);
        }
        table.addCell(cell);
    }

    @Override
    protected void buildCell(Object value) {
    	PdfPCell cell;
    	if(value == null){
    		cell = new PdfPCell(new Phrase(""));
    	}else if(value instanceof Calendar){
    		Calendar calendar = (Calendar) value;
    		cell = new PdfPCell(new Phrase(formatDate(calendar.getTime())));
    	}else if(value instanceof Date){
    		cell = new PdfPCell(new Phrase(formatDate((Date) value)));
    	}else {
    		cell = new PdfPCell(new Phrase(value.toString()));
    	}
        
        if (!withBorder) {
            cell.setBorder(Rectangle.NO_BORDER);
        }
        table.addCell(cell);
    }

    @Override
    protected String getFileExtension() {
        return ".pdf";
    }

    @Override
    protected void writeToFile() {
        try {
            document.add(table);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public boolean isWithBorder() {
        return withBorder;
    }

    public void setWithBorder(boolean withBorder) {
        this.withBorder = withBorder;
    }

    @Override
    protected void resetContent() {
        document = new Document();
        table = new PdfPTable(getNumberofColumns());
        try {
            PdfWriter.getInstance(document, new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document.open();
    }
}
